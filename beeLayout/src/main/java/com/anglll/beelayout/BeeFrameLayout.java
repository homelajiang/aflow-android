package com.anglll.beelayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

public class BeeFrameLayout extends FrameLayout {

    private Paint paint = new Paint();
    private final Paint zonePaint = new Paint();
    private RectF roundRect = new RectF();
    Path pathCollection = new Path();


    private int borderColor = Color.RED;
    private float borderWidth = 10f;
    private boolean hasBorder = false;
    private float roundCorner = 0.1f;
    private float spaceWidth = 10f;
    private Paint strokePaint;
    private float strokeR;

    public BeeFrameLayout(Context context) {
        super(context);
        init(context, null);
    }

    public BeeFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BeeFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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

        zonePaint.setAntiAlias(true);
        zonePaint.setColor(0xFFFFFFFF);

        strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint.setStrokeWidth(borderWidth);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setColor(borderColor);
        strokeR = (float) (borderWidth / 2 / Math.sin(60 * Math.PI / 180));

        paint.setStrokeWidth(3);
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        int parentTrueSize = Math.min(parentHeight, parentWidth);
        roundRect.set(0, 0, parentTrueSize, parentTrueSize);
        setMeasuredDimension(parentTrueSize, parentTrueSize);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        pathCollection.reset();

        int parentTrueSize = Math.min(getWidth(), getHeight());//layout的实际大小
        int parentSize = Math.min(getWidth() - getPaddingLeft() - getPaddingRight(),
                getHeight() - getPaddingTop() - getPaddingBottom());//layout用来绘制children的大小
        int childMaxSize = (int) (parentSize / (2 * Math.sin(60 * Math.PI / 180) + 1));//子view的大小

        float blockR = childMaxSize / 2 - strokeR - spaceWidth / 2;
        float left = 0, top = 0, right = 0, bottom = 0;
        for (int i = 0; i < 7; i++) {
            int x;
            int y;
            if (i == 6) {
                x = parentTrueSize / 2;
                y = parentTrueSize / 2;
            } else {
                x = (int) (parentTrueSize / 2 + childMaxSize * Math.sin(60 * Math.PI / 180) * Math.cos(i * 60 * Math.PI / 180));
                y = (int) (parentTrueSize / 2 - childMaxSize * Math.sin(60 * Math.PI / 180) * Math.sin(i * 60 * Math.PI / 180));
            }
            Path path = new Path();
            for (int j = 0; j < 7; j++) {
                float x1 = (float) (blockR * Math.cos((30 + j * 60) * Math.PI / 180) + x);
                float y1 = (float) (blockR * Math.sin((30 + j * 60) * Math.PI / 180) + y);
                if (j == 0) {
                    path.moveTo(x1, y1);
                } else {
                    path.lineTo(x1, y1);
                }
                if (i == 0 && j == 0) {
                    right = x1;
                }
                if (i == 1 && j == 4) {
                    top = y1;
                }
                if (i == 3 && j == 2) {
                    left = x1;
                }
                if (i == 4 && j == 1) {
                    bottom = y1;
                }
            }
            path.close();
            pathCollection.addPath(path);
        }

        //裁剪背景
        int save = canvas.saveLayer(left, top, right, bottom, null, Canvas.ALL_SAVE_FLAG);

        super.dispatchDraw(canvas);

        strokePaint.setPathEffect(new CornerPathEffect(blockR * roundCorner));
        zonePaint.setPathEffect(new CornerPathEffect(blockR * roundCorner));

        zonePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawPath(pathCollection, zonePaint);
        if (hasBorder)
            canvas.drawPath(pathCollection, strokePaint);
        canvas.restoreToCount(save);
        zonePaint.setXfermode(null);
    }
}
