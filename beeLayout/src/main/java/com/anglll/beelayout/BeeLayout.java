package com.anglll.beelayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;

public class BeeLayout extends FrameLayout {

    private boolean hasBorder;
    private float borderWidth;
    private int borderColor;
    private float spaceWidth;
    private float roundCorner;

    private BeeAdapter adapter;
    private ArrayList<BeeViewHolder> viewHolders = new ArrayList<>();

    public BeeLayout(Context context) {
        super(context);
        init(context, null);
    }

    public BeeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BeeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BeeLayout);
        hasBorder = a.getBoolean(R.styleable.BeeLayout_beeHasBorder, false);
        borderWidth = a.getDimension(R.styleable.BeeLayout_beeBorderWidth, 10f);
        borderColor = a.getColor(R.styleable.BeeLayout_beeBorderColor, Color.RED);
        spaceWidth = a.getDimension(R.styleable.BeeLayout_beeSpaceWidth, 10f);
        roundCorner = a.getFloat(R.styleable.BeeLayout_beeRoundCorner, 0.1f);
        if (roundCorner < 0 || roundCorner > 1)
            roundCorner = 0.1f;
        a.recycle();
    }

    public void setAdapter(BeeAdapter adapter) {
        this.adapter = adapter;
        removeAllViews();
        viewHolders.clear();
        for (int i = 0; i < adapter.getItemCount(); i++) {
            BeeItemView beeItemView = new BeeItemView(getContext());
            beeItemView.setBorderColor(borderColor);
            beeItemView.setBorderWidth(borderWidth);
            beeItemView.hasBorder(hasBorder);
            beeItemView.setSpaceWidth(spaceWidth);
            beeItemView.setRoundCorner(roundCorner);
            BeeViewHolder holder =
                    this.adapter.onCreateViewHolder(beeItemView, adapter.getItemViewType(i));
            if (holder == null) {
                Log.e("beeLayout", "BeeViewHolder can not null!");
                return;
            }
            viewHolders.add(holder);
            adapter.onBindViewHolder(holder, i);
            beeItemView.addView(holder.itemView);
            addView(beeItemView);
        }
        this.adapter.setViewHolders(viewHolders);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed)
            layoutChildren(left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        int parentSize = Math.min(MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight(),
                MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom());
        int childMaxSize = (int) (parentSize / (2 * Math.sin(60 * Math.PI / 180) + 1));

        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).getLayoutParams().width = childMaxSize;
            getChildAt(i).getLayoutParams().height = childMaxSize;
        }
        setMeasuredDimension(Math.min(parentWidth, parentHeight), Math.min(parentWidth, parentHeight));
    }

    private void layoutChildren(int left, int top, int right, int bottom) {
        final int count = getChildCount();
        int parentWidth = getWidth();
        int parentHeight = getHeight();
        for (int i = 0; i < Math.min(count, 7); i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                int width = child.getMeasuredWidth();
                int height = child.getMeasuredHeight();
                int childLeft;
                int childTop;
                if (i == 6) {
                    childLeft = -width / 2 + parentWidth / 2;
                    childTop = -height / 2 + parentHeight / 2;
                } else {
                    double r = Math.min(width, height) / 2 * Math.sin(60 * Math.PI / 180) * 2;
                    childLeft = (int) (r * Math.cos(i * 60 * Math.PI / 180) - width / 2 + parentWidth / 2);
                    childTop = (int) (r * Math.sin(i * 60 * Math.PI / 180) - height / 2 + parentHeight / 2);
                }
                child.layout(childLeft, childTop, childLeft + width, childTop + height);
            }
        }

//            for (int i = 0; i < count - 1; i++) {
//                final View child = getChildAt(i);
//                if (child.getVisibility() != GONE) {
//                    final int width = child.getMeasuredWidth();
//                    final int height = child.getMeasuredHeight();
//                    int childLeft;
//                    int childTop;
//                    double angle = i * 60 * Math.PI / 180;
//                    int r = width * 17 / 18;
//                    childLeft = getWidth() / 2 - (int) (r * Math.sin(angle)) - r / 2;
//                    childTop = getWidth() / 2 - (int) (r - r * Math.cos(angle)) + r / 2;
//
//                    child.layout(childLeft, childTop, childLeft + width, childTop + height);
//                }
//            }
//            final View child = getChildAt(6);
//            if (child.getVisibility() != GONE) {
//                final int width = child.getMeasuredWidth();
//                final int height = child.getMeasuredHeight();
//                int childLeft;
//                int childTop;
//                int r = width * 17 / 18;
//                childLeft = getWidth() / 2 - r / 2;
//                childTop = getWidth() / 2 - r / 2;
//                child.layout(childLeft, childTop, childLeft + width, childTop + height);
//            }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }
}
