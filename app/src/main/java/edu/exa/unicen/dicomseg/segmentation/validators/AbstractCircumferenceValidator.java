package edu.exa.unicen.dicomseg.segmentation.validators;

import android.graphics.Point;
import android.graphics.PointF;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.exa.unicen.dicomseg.coordinatesystem.PolarUtils;
import edu.exa.unicen.dicomseg.segmentation.Segmentation;
import edu.exa.unicen.dicomseg.segmentation.SegmentationType;

public abstract class AbstractCircumferenceValidator implements SegmentationValidator {

    protected static Map<String, String> errors = new HashMap<String, String>();

    public abstract Boolean compare(float interpolatedY, float distance, SegmentationType segType);

    @Override
    public Boolean validate(List<Point> points, Segmentation toCompare, int refX, int refY) {
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
    public Map<String, String> errors() {
        return errors;
    }

    @Override
    public void resetErrors() {
        errors = new HashMap<String, String>();
    }

}
