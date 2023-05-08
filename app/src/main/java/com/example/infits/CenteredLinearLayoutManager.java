package com.example.infits;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CenteredLinearLayoutManager extends LinearLayoutManager {

    public CenteredLinearLayoutManager(Context context) {
        super(context);
    }

    public CenteredLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int scrolled = super.scrollVerticallyBy(dy, recycler, state);
        int overScroll = scrolled % getHeight();

        // Recycle views that went out of bounds
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (getDecoratedBottom(child) < 0) {
                removeAndRecycleView(child, recycler);
            } else if (getDecoratedTop(child) > getHeight()) {
                removeAndRecycleView(child, recycler);
            }
        }

        // Calculate the new scroll position
        int newScroll = scrolled - overScroll;
        if (newScroll == -getHeight()) {
            newScroll = 0;
            offsetChildrenVertical(getHeight());
        } else if (newScroll == getHeight()) {
            newScroll = 0;
            offsetChildrenVertical(-getHeight());
        }
        return newScroll;
    }
}