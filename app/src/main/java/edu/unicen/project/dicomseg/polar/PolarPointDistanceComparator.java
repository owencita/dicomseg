package edu.unicen.project.dicomseg.polar;

import android.graphics.PointF;

import java.util.Comparator;

public class PolarPointDistanceComparator implements Comparator<PointF> {

    @Override
    public int compare(PointF lhs, PointF rhs) {
        Float lhsFloat = new Float(lhs.x);
        Float rhsFloat = new Float(rhs.x);
        return lhsFloat.compareTo(rhsFloat);
    }
}
