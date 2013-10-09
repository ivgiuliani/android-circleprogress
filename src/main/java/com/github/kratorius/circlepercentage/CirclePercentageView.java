package com.github.kratorius.circlepercentage;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Simple percentage bar shown inside a circle.
 */
public class CirclePercentageView extends View {
    private final static int DEFAULT_THICKNESS = 20;
    private final static int DEFAULT_VALUE = 0;
    private final static int DEFAULT_START_ANGLE = -90;
    private final static int DEFAULT_COLOR = Color.DKGRAY;
    private final static int DEFAULT_TEXT_SIZE = 45;

    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final Path mPath = new Path();
    private final RectF mDrawRect = new RectF();

    private int mThickness;
    private int mColor;
    private int mValue;
    private int mStartAngle;

    private int mTextColor;
    private int mTextSize;

    public CirclePercentageView(Context context) {
        this(context, null, 0);
    }

    public CirclePercentageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CirclePercentageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CirclePercentageView);
        try {
            assert a != null;
            mThickness = a.getDimensionPixelSize(R.styleable.CirclePercentageView_thickness, DEFAULT_THICKNESS);
            mColor = a.getColor(R.styleable.CirclePercentageView_color, DEFAULT_COLOR);
            mTextColor = a.getColor(R.styleable.CirclePercentageView_textColor, mColor);
            mValue = a.getInteger(R.styleable.CirclePercentageView_value, DEFAULT_VALUE);
            mStartAngle = a.getInteger(R.styleable.CirclePercentageView_startAngle, DEFAULT_START_ANGLE);
            mTextSize = a.getDimensionPixelSize(R.styleable.CirclePercentageView_textSize, DEFAULT_TEXT_SIZE);
        } finally {
            a.recycle();
        }

        setValue(mValue);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mColor);
        mPaint.setStrokeWidth(mThickness);

        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // rescale [0-100] in [0-360]
        final int degrees = (int) ((mValue / 100.) * 360);
        final int width = getWidth() - getPaddingRight();
        final int height = getHeight() - getPaddingBottom();
        final String text = String.format("%d%%", mValue);

        mPath.reset();

        mDrawRect.top = getPaddingTop() + mThickness;
        mDrawRect.left = getPaddingLeft() + mThickness;
        mDrawRect.bottom = height - mThickness;
        mDrawRect.right = width - mThickness;

        mPath.addArc(mDrawRect, mStartAngle, degrees);
        canvas.drawPath(mPath, mPaint);

        int textX = getWidth() / 2;
        int textY = (int) ((getHeight() / 2) - ((mTextPaint.descent() + mTextPaint.ascent()) / 2.));

        canvas.drawText(text, textX, textY, mTextPaint);
    }


    public void setValue(int value) {
        if (value < 0 || value > 100) {
            throw new IllegalArgumentException("Start value in the 0-100 range (both included)");
        }
        mValue = value;
        invalidate();
    }

    public int getValue() {
        return mValue;
    }

    public void setThickness(int thickness) {
        mThickness = thickness;
        mPaint.setStrokeWidth(thickness);
        invalidate();
    }

    public int getThickness() {
        return mThickness;
    }

    public void setColor(int color) {
        mColor = color;
        mPaint.setColor(mColor);
        invalidate();
    }

    public int getColor() {
        return mColor;
    }

    public void setTextColor(int color) {
        mTextColor = color;
        mTextPaint.setColor(mTextColor);
        invalidate();
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextSize(int size) {
        mTextSize = size;
        mTextPaint.setTextSize(size);
        invalidate();
    }

    public int getTextSize() {
        return mTextSize;
    }

    public void setStartAngle(int angle) {
        mStartAngle = angle;
    }

    public int getStartAngle() {
        return mStartAngle;
    }
}
