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
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

public class BeeImageView extends AppCompatImageView {
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

    public BeeImageView(Context context) {
        super(context);
        init(context, null);
    }

    public BeeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BeeImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        strokePaint.setColor(borderColor);
    }

    public void setBorderWidth(float borderWidth) {
        this.borderWidth = borderWidth;
        strokePaint.setStrokeWidth(borderWidth);
        //边框宽度
        strokeR = (float) (borderWidth / 2 / Math.sin(60 * Math.PI / 180)); }

    public void setHasBorder(boolean hasBorder) {
        this.hasBorder = hasBorder;
    }

    public void setRoundCorner(float roundCorner) {
        this.roundCorner = roundCorner;
    }

    public void setSpaceWidth(float spaceWidth) {
        this.spaceWidth = spaceWidth;
    }

    private void init(Context context, AttributeSet attrs) {

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BeeImageView);
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
    protected void onDraw(Canvas canvas) {

        pathCollection.reset();

        int parentTrueSize = Math.min(getWidth(), getHeight());
        int parentSize = Math.min(getWidth() - getPaddingLeft() - getPaddingRight(),
                getHeight() - getPaddingTop() - getPaddingBottom());
        int childMaxSize = (int) (parentSize / (2 * Math.sin(60 * Math.PI / 180) + 1));

        float blockR = childMaxSize / 2 - strokeR - spaceWidth / 2;
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
                if (j == 0) {
                    path.moveTo((float) (blockR * Math.cos((30 + j * 60) * Math.PI / 180) + x),
                            (float) (blockR * Math.sin((30 + j * 60) * Math.PI / 180) + y));
                } else {
                    path.lineTo((float) (blockR * Math.cos((30 + j * 60) * Math.PI / 180) + x),
                            (float) (blockR * Math.sin((30 + j * 60) * Math.PI / 180) + y));
                }
            }
            path.close();
            pathCollection.addPath(path);
        }

        strokePaint.setPathEffect(new CornerPathEffect(blockR * roundCorner));
        zonePaint.setPathEffect(new CornerPathEffect(blockR * roundCorner));

        canvas.saveLayer(roundRect, zonePaint, Canvas.ALL_SAVE_FLAG);
        canvas.drawPath(pathCollection, zonePaint);
        canvas.saveLayer(roundRect, paint, Canvas.ALL_SAVE_FLAG);
        super.onDraw(canvas);
        canvas.restore();
        if (hasBorder)
            canvas.drawPath(pathCollection, strokePaint);
    }
}
