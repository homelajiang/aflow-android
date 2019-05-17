package com.anglll.aflow.ui.discovery;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.anglll.aflow.R;
import com.anglll.aflow.ui.epoxy.models.FeaturesModel;

/**
 * Created by yuan on 2017/12/4 0004.
 */

public class DiscoveryDecoration extends RecyclerView.ItemDecoration {

    private final int dividerHeight;
    private final Paint dividerPaint;

    public DiscoveryDecoration(Context context) {
        dividerPaint = new Paint();
        dividerPaint.setColor(context.getResources().getColor(R.color.grey_light));
        dividerHeight = context.getResources().getDimensionPixelSize(R.dimen.flow_card_divider);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            float top = view.getBottom();
            float bottom = view.getBottom() + dividerHeight;
            c.drawRect(left, top, right, bottom, dividerPaint);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int childCount = parent.getChildCount();
        for(int i=0;i<childCount;i++){
            RecyclerView.ViewHolder viewHolder = parent.getChildViewHolder(view);
        }
        outRect.bottom = dividerHeight;
    }
}
