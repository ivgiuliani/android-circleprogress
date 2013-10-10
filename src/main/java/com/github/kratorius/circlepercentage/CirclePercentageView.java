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
            throw new IllegalArgumentException(
                    String.format("Start value was %d but must be in the 0-100 range (both included)", value)
            );
        }
        mValue = value;
        invalidate();
    }

    /**
     * Returns the current value of the progress circle.
     * @return current progress circle's value
     */
    public int getValue() {
        return mValue;
    }

    /**
     * Sets the thickness of the of the circle progress bar.
     * @param thickness thickness of the of the circle progress bar
     */
    public void setThickness(int thickness) {
        mThickness = thickness;
        mPaint.setStrokeWidth(thickness);
        invalidate();
    }

    /**
     * Returns the current thickness of the circle progress bar.
     * @return thickness of the circle progress bar
     */
    public int getThickness() {
        return mThickness;
    }

    /**
     * Sets the color of the circle progress bar.
     * @param color color of the circle progress bar.
     */
    public void setColor(int color) {
        mColor = color;
        mPaint.setColor(mColor);
        invalidate();
    }

    /**
     * Returns the current color of the circle progress bar.
     * @return color of the circle progress bar
     */
    public int getColor() {
        return mColor;
    }

    /**
     * Sets the color of the text inside circle progress bar.
     * @param color text color
     */
    public void setTextColor(int color) {
        mTextColor = color;
        mTextPaint.setColor(mTextColor);
        invalidate();
    }

    /**
     * Returns the color of the text inside the circle progress bar.
     * @return color of the circle progress bar
     */
    public int getTextColor() {
        return mTextColor;
    }

    /**
     * Sets the size of the text inside the circle progress bar.
     * @param size text size
     */
    public void setTextSize(int size) {
        mTextSize = size;
        mTextPaint.setTextSize(size);
        invalidate();
    }

    /**
     * Returns the size of the text inside the circle progress bar.
     * @return text size
     */
    public int getTextSize() {
        return mTextSize;
    }

    /**
     * Sets the start angle of the progress circle. This corresponds to the point
     * in the circle where the drawing starts.<br />
     * The angle {@code 0} corresponds to the rightmost point on the circle (if you
     * take a clock as reference, it'd be the 15 mins point).<br />
     * If the start angle is negative or >= 360, the start angle is treated as start
     * angle modulo 360.
     * @param angle angle in degrees
     */
    public void setStartAngle(int angle) {
        mStartAngle = angle;
        invalidate();
    }

    /**
     * Returns the current start angle.
     * @return start angle
     */
    public int getStartAngle() {
        return mStartAngle;
    }
}
