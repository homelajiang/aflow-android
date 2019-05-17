package com.anglll.aflow.utils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class DefaultDecoration extends RecyclerView.ItemDecoration {

    private final Paint decorationPaint = new Paint();

    private final int decorationHeight;

    private final int decorationLeft;
    private final int decorationRight;

    private final int decorationColor;

    public static final int COLOR_NO = -1;

    public DefaultDecoration(int width, int height, int color) {
        this.decorationColor = color;
        decorationHeight = height;
        decorationLeft = width;
        decorationRight = width;
        if (color >= 0)
            decorationPaint.setColor(color);
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildViewHolder(view).getLayoutPosition() == 0) {
            outRect.set(decorationLeft, decorationHeight, decorationRight, decorationHeight);
        } else {
            outRect.set(decorationLeft, 0, decorationRight, decorationHeight);
        }

    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (decorationColor == COLOR_NO)
            return;
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            float top = view.getBottom();
            float bottom = view.getBottom() + decorationHeight;
            if (i == 0)
                c.drawRect(left, 0, right, decorationHeight, decorationPaint);
            c.drawRect(left, top, right, bottom, decorationPaint);
        }
    }
}
