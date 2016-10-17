package edu.unicen.project.dicomseg.segmentation.validators;

import android.graphics.Point;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;

import edu.unicen.project.dicomseg.coordinatesystem.PolarUtils;
import edu.unicen.project.dicomseg.segmentation.Segmentation;
import edu.unicen.project.dicomseg.segmentation.SegmentationType;

public abstract class AbstractCircumferenceValidator implements SegmentationValidator {

    protected static List<String> errors = new ArrayList<String>();

    public abstract Boolean compare(float interpolatedY, float distance, SegmentationType segType);

    @Override
    public Boolean validate(List<Point> points, Segmentation toCompare, int refX, int refY) {
        errors = new ArrayList<String>();
        if (!points.isEmpty() && (toCompare != null)) {
            List<PointF> polarPoints = PolarUtils.getPolarPoints(points, refX, refY);
            List<PointF> toComparePolarPoints = PolarUtils.getPolarPoints(toCompare.getPoints(), refX, refY);
            for (PointF p: polarPoints) {
                List<PointF> closestPoints = PolarUtils.getClosestDegreePoints(toComparePolarPoints, p.y);
                if (!closestPoints.isEmpty()) {
                    if (closestPoints.size() == 2) {
                        PointF inf = closestPoints.get(0);
                        PointF sup = closestPoints.get(1);
                        float interpolatedDistance = PolarUtils.getInterpolatedDistance(inf, sup, p.y);
                        System.out.println(inf.x + " < " + interpolatedDistance + " < " + sup.x);
                        if (!compare(interpolatedDistance, p.x, toCompare.getType())) {
                            return Boolean.FALSE;
                        }
                    }
                }
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public List<String> errors() {
        return errors;
    }

}
