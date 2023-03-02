package com.example.infits;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CenteredLinearLayoutManager extends LinearLayoutManager {


    public CenteredLinearLayoutManager(Context context) {
        super(context);
        setOrientation(LinearLayoutManager.HORIZONTAL);
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int scrolled = super.scrollHorizontallyBy(dx, recycler, state);
        int midpoint = getWidth() / 2;
        float d0 = 0;
        float d1 = 0;
        float s0 = 0;
        float s1 = 0;
        boolean isScrollingTowardsStart = dx < 0;

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            float childMidpoint = (getDecoratedRight(child) + getDecoratedLeft(child)) / 2f;
            float d = Math.abs(midpoint - childMidpoint);
            float scale = 1 - (d / getWidth());
            if (scale < 0) {
                scale = 0;
            }

            if (isScrollingTowardsStart) {
                if (getDecoratedRight(child) < 0) {
                    d0 = d;
                    s0 = scale;
                } else if (getDecoratedRight(child) == 0) {
                    s1 = scale;
                    d1 = d;
                }
            } else {
                if (getDecoratedLeft(child) > getWidth()) {
                    d0 = d;
                    s0 = scale;
                } else if (getDecoratedLeft(child) == getWidth()) {
                    s1 = scale;
                    d1 = d;
                }
            }
        }

        float swipedPercentage = scrolled / (float) getWidth();

        if (swipedPercentage < 0.5f) {
            View child = getChildAt(0);
            if (isScrollingTowardsStart) {
                float scaleFactor = (1 - swipedPercentage * 2) * s0 + swipedPercentage * 2 * s1;
                float translationX = -d0 * (1 - swipedPercentage * 2) - d1 * swipedPercentage * 2;
                child.setScaleX(scaleFactor);
                child.setScaleY(scaleFactor);
                child.setTranslationX(translationX);
            } else {
                float scaleFactor = (1 - swipedPercentage * 2) * s1 + swipedPercentage * 2 * s0;
                float translationX = d0 * swipedPercentage * 2 + d1 * (1 - swipedPercentage * 2);
                child.setScaleX(scaleFactor);
                child.setScaleY(scaleFactor);
                child.setTranslationX(translationX);
            }
        } else {
            View child = getChildAt(1);
            if (isScrollingTowardsStart) {
                float scaleFactor = (1 - (swipedPercentage - 0.5f) * 2) * s1 + (swipedPercentage - 0.5f) * 2 * s0;
                float translationX = -d1 * (1 - (swipedPercentage - 0.5f) * 2) - d0 * (swipedPercentage - 0.5f) * 2;
                child.setScaleX(scaleFactor);
                child.setScaleY(scaleFactor);
                child.setTranslationX(translationX);
            } else {
                float scaleFactor = (1 - (swipedPercentage - 0.5f) * 2) * s0 + (swipedPercentage - 0.5f) * 2 * s1;
                float translationX = d1 * (swipedPercentage - 0.5f) * 2 * s1;
                float translationY = d1 * (swipedPercentage - 0.5f) * 2 + d0 * (1 - (swipedPercentage - 0.5f) * 2);
                child.setScaleX(scaleFactor);
                child.setScaleY(scaleFactor);
                child.setTranslationX(translationX);
            }
        }
        return scrolled;
    }
}