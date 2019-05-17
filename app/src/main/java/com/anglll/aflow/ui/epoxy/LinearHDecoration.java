package com.anglll.aflow.ui.epoxy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.anglll.aflow.R;

/**
 * Created by yuan on 2017/12/4 0004.
 */

public class LinearHDecoration extends RecyclerView.ItemDecoration {

    private final int dividerWidth;
    private final Paint dividerPaint;

    public LinearHDecoration(Context context) {
        dividerPaint = new Paint();
        dividerPaint.setColor(context.getResources().getColor(R.color.grey_light));
        dividerWidth = context.getResources().getDimensionPixelSize(R.dimen.flow_card_divider);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            outRect.left = dividerWidth;
        }
    }
}
