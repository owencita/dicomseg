package edu.exa.unicen.dicomseg.snakes;

import android.graphics.Point;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class Snake {

	// Points of the snake
	public List<Point> snake;

	// Length of the snake (euclidean distance)
	private double snakelength = 0;

	// Size of the image (and of the 2 arrays below)
	private int width=0, height = 0;
	
	// Gradient value (modulus)
	private int[][] gradient;
	
	// Gradient flow (modulus)
	private int[][] flow;
	
	// 3x3 neighborhood used to compute energies
	private double[][] e_uniformity = new double[3][3];
	private double[][] e_curvature  = new double[3][3];
	private double[][] e_flow       = new double[3][3];
	private double[][] e_inertia    = new double[3][3];

	// Auto add/remove points to the snake according to distance between points
	public boolean AUTOADAPT = true;
	public int AUTOADAPT_LOOP = 10;
	public int AUTOADAPT_MINLEN = 8;
	public int AUTOADAPT_MAXLEN = 16;

	// Maximum number of iterations (if no convergence)
	public int MAXITERATION = 1000;

	// Coefficients for the 4 energy functions
	public double alpha;
    public double beta;
    public double gamma;
    public double delta;

	// alpha = coefficient for uniformity (high => force equals distance between points)
	// beta  = coefficient for curvature  (high => force smooth curvature)
	// gamma  = coefficient for flow      (high => force gradient attraction)
	// delta  = coefficient for intertia  (high => get stuck to gradient)
	
	/**
	 * Constructor
	 *
	 * @param width,height size of the image and of the 2 following arrays
	 * @param gradient gradient (modulus)
	 * @param flow gradient flow (modulus)
	 * @param points initial points of the snake
	 */

    public Snake(int width, int height, int[][] gradient, int[][] flow, List<Point> points, double[] coefficients) {
		// set coefficients from preferencies
		alpha = coefficients[0];
		beta = coefficients[1];
		gamma = coefficients[2];
		delta = coefficients[3];

        this.snake = Lists.newArrayList(points);
		this.gradient = gradient;
		this.flow = flow;
		this.width = width;
		this.height = height;
	}

	/**
	 * Main loop
	 * 
	 * @return the number of iterations performed
	 */
	public int snake() {

		int loop = 0;

		while (step() && loop < MAXITERATION) {
			// auto adapt the number of points in the snake
			if (AUTOADAPT && (loop % AUTOADAPT_LOOP) == 0) {
				removeOverlappingPoints(AUTOADAPT_MINLEN);
				addMissingPoints(AUTOADAPT_MAXLEN);
			}
			loop++;
		}

		// rebuild using spline interpolation
		if (AUTOADAPT) {
            rebuild(AUTOADAPT_MAXLEN);
        }
		
		return loop;
	}

	/**
	 * update the position of each point of the snake
	 *
	 * @return true if the snake has changed, otherwise false.
	 */
	private boolean step() {
		boolean changed=false;
		Point p = new Point(0,0);

		// compute length of original snake (used by method: f_uniformity)
		this.snakelength = getSnakeLength();

		// compute the new snake
		List<Point> newsnake = new ArrayList<Point>(snake.size());

		// for each point of the previous snake
		for(int i=0; i<snake.size(); i++) {
			Point prev = snake.get((i+snake.size()-1) % snake.size());
			Point cur  = snake.get(i);
			Point next = snake.get((i+1) % snake.size());

			// compute all energies
			for(int dy=-1;dy<=1;dy++) {
				for(int dx=-1;dx<=1;dx++) {
                    p.set(cur.x+dx, cur.y+dy);
					e_uniformity[1+dx][1+dy] = f_uniformity(prev,next,p);
					e_curvature[1+dx][1+dy]  = f_curvature(prev,p,next);
					e_flow[1+dx][1+dy]       = f_gflow(cur,p);
					e_inertia[1+dx][1+dy]    = f_inertia(cur,p);
				}
			}

			// normalize energies
			normalize(e_uniformity);
			normalize(e_curvature);
			normalize(e_flow);
			normalize(e_inertia);

			// find the point with the minimum sum of energies
			double emin = Double.MAX_VALUE, e = 0;
			int x = 0, y = 0;
			for (int dy=-1; dy<=1; dy++) {
				for (int dx=-1; dx<=1; dx++) {
					e = 0;
					e+= alpha * e_uniformity[1+dx][1+dy]; // internal energy
					e+= beta  * e_curvature[1+dx][1+dy];  // internal energy
					e+= gamma * e_flow[1+dx][1+dy];       // external energy
					e+= delta * e_inertia[1+dx][1+dy];    // external energy
					
					if (e < emin) { emin = e; x = cur.x+dx; y = cur.y+dy; }
				}
			}

			// boundary check
			if (x < 1) x = 1;
			if (x >= (this.width-1)) x = this.width-2;
			if (y < 1) y = 1;
			if (y >= (this.height-1)) y = this.height-2;

			// compute the returned value
			if (x!=cur.x || y!=cur.y) changed=true;

			// create the point in the new snake
			newsnake.add(new Point(x,y));
		}
		
		// new snake becomes current
		this.snake=newsnake;

		return changed;
	}

	// normalize energy matrix
	private void normalize(double[][] array3x3) {
		double sum = 0;
		for (int i=0; i<3; i++)
			for (int j=0; j<3; j++)
				sum+=Math.abs(array3x3[i][j]);

		if (sum == 0) return;

		for (int i=0; i<3; i++)
			for (int j=0; j<3; j++)
				array3x3[i][j]/=sum;
	}

	private double getSnakeLength() {
		// total length of snake
		double length=0;
		for (int i=0; i<snake.size(); i++) {
			Point cur = snake.get(i);
			Point next = snake.get((i+1) % snake.size());
			length+= distance2D(cur, next);
		}
		return length;
	}

	private double distance2D(Point A, Point B) {
		int ux = A.x-B.x;
		int uy = A.y-B.y;
		double un = ux*ux+uy*uy;
		return Math.sqrt(un);
	}

	// ************************** ENERGY FUNCTIONS **************************

	private double f_uniformity(Point prev, Point next, Point p) {
		// length of previous segment
		double un = distance2D(prev, p);

		// measure of uniformity
		double avg = snakelength / snake.size();
		double dun = Math.abs(un-avg);

		// elasticity energy
		return dun*dun;
	}

	private double f_curvature(Point prev, Point p, Point next) {
		int ux = p.x-prev.x;
		int uy = p.y-prev.y;
		double un = Math.sqrt(ux*ux+uy*uy);

		int vx = p.x-next.x;
		int vy = p.y-next.y;
		double vn = Math.sqrt(vx*vx+vy*vy);

		if (un == 0 || vn == 0) return 0;

		double cx = (vx+ux)/(un*vn);
		double cy = (vy+uy)/(un*vn);

		// curvature energy
		double cn = cx*cx+cy*cy;
		return cn;
	}

	private double f_gflow(Point cur, Point p) {
		// gradient flow
		int dcur = this.flow[cur.x][cur.y];
		int dp   = this.flow[p.x][p.y];
		double d = dp-dcur;
		return d;
	}

	private double f_inertia(Point cur, Point p) {
		double d = distance2D(cur, p);
		double g = this.gradient[cur.x][cur.y];
		double e = g*d;
		return e;
	}

	// ************************** AUTOADAPT **************************

	// rebuild the snake using cubic spline interpolation
	private void rebuild(int space) {

		// pre-compute length(i) = length of the snake from start to point #i
		double[] clength = new double[snake.size()+1];
		clength[0]=0;
		for (int i=0; i<snake.size(); i++) {
			Point cur = snake.get(i);
			Point next = snake.get((i+1) % snake.size());
			clength[i+1] = clength[i] + distance2D(cur, next);
		}

		// compute number of points in the new snake
		double total = clength[snake.size()];
		int nmb = (int)(0.5+total/space);

		// build a new snake
		List<Point> newsnake = new ArrayList<Point>(snake.size());
		for (int i=0,j=0; j<nmb; j++) {
			// current length in the new snake
			double dist = (j*total)/nmb;

			// find corresponding interval of points in the original snake
			while (!(clength[i] <= dist && dist < clength[i+1])) i++;

			// get points (P-1,P,P+1,P+2) in the original snake
			Point prev = snake.get((i+snake.size()-1)%snake.size());
			Point cur = snake.get(i);
			Point next = snake.get((i+1)%snake.size());
			Point next2 = snake.get((i+2)%snake.size());

			// do cubic spline interpolation
			double t =  (dist-clength[i])/(clength[i+1]-clength[i]);
			double t2 = t*t, t3=t2*t;
			double c0 =  1*t3;
			double c1 = -3*t3 +3*t2 +3*t + 1;
			double c2 =  3*t3 -6*t2 + 4;
			double c3 = -1*t3 +3*t2 -3*t + 1;
			double x = prev.x*c3 + cur.x*c2 + next.x* c1 + next2.x*c0;
			double y = prev.y*c3 + cur.y*c2 + next.y* c1 + next2.y*c0;
			Point newPoint = new Point((int)(0.5+x/6), (int)(0.5+y/6));

			// add computed point to the new snake
			newsnake.add(newPoint);
		}
		this.snake = newsnake;
	}


	private void removeOverlappingPoints(int minlen) {
		// for each point of the snake
		for (int i=0; i<snake.size(); i++) {
			Point cur = snake.get(i);

			// check the other points (right half)
			for (int di=1+snake.size()/2; di>0; di--) {
				Point end = snake.get((i+di) % snake.size());
				double dist = distance2D(cur,end);

				// if the two points are to close...
				if ( dist > minlen ) continue;

				// ... cut the "loop" part og the snake
				for (int k=0; k<di; k++) snake.remove((i+1) % snake.size());
				break;
			}
		}
	}

	private void addMissingPoints(int maxlen) {
		// for each point of the snake
		for (int i=0; i<snake.size(); i++) {
			Point prev  = snake.get((i+snake.size()-1)%snake.size());
			Point cur   = snake.get(i);
			Point next  = snake.get((i+1) % snake.size());
			Point next2  = snake.get((i+2) % snake.size());

			// if the next point is to far then add a new point
			if (distance2D(cur,next) > maxlen ) {

				// precomputed Uniform cubic B-spline for t=0.5
				double c0=0.125/6.0, c1=2.875/6.0, c2=2.875/6.0, c3=0.125/6.0;
				double x = prev.x*c3 + cur.x*c2 + next.x* c1 + next2.x*c0;
				double y = prev.y*c3 + cur.y*c2 + next.y* c1 + next2.y*c0;
				Point newPoint = new Point( (int)(0.5+x), (int)(0.5+y) );

				snake.add(i+1, newPoint); i--;
			}
		}
	}

}
