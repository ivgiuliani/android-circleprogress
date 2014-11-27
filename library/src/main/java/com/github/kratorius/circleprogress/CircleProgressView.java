package com.github.kratorius.circleprogress;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Simple progress bar shown as a circle.
 */
public class CircleProgressView extends View {
    private final static int DEFAULT_THICKNESS = 20;
    private final static int DEFAULT_VALUE = 0;
    private final static int DEFAULT_START_ANGLE = -90;
    private final static int DEFAULT_COLOR = Color.DKGRAY;
    private final static int DEFAULT_TEXT_SIZE = 45;
    private final static int DEFAULT_START_ANIMATION_DURATION = 500;

    // keep in sync with values in attrs.xml
    private final static int ANIM_NONE = 0;
    private final static int ANIM_ROLL = 1;
    private final static int ANIM_FADE_IN = 2;
    private final static int ANIM_INCREMENTAL = 3;
    private final static int ANIM_THICKNESS_EXPAND = 4;

    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final Path mPath = new Path();
    private final RectF mDrawRect = new RectF();

    private int mThickness;
    private int mColor;
    private int mValue;
    private int mStartAngle;

    private String mText;
    private int mTextColor;
    private int mTextSize;

    private int mStartAnimation;
    private int mStartAnimationDuration;

    public CircleProgressView(Context context) {
        this(context, null, 0);
    }

    public CircleProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressView);
        try {
            assert a != null;
            mThickness = a.getDimensionPixelSize(R.styleable.CircleProgressView_circleProgressThickness, DEFAULT_THICKNESS);
            mColor = a.getColor(R.styleable.CircleProgressView_circleProgressColor, DEFAULT_COLOR);
            mTextColor = a.getColor(R.styleable.CircleProgressView_circleProgressTextColor, mColor);
            mValue = a.getInteger(R.styleable.CircleProgressView_circleProgressValue, DEFAULT_VALUE);
            mStartAngle = a.getInteger(R.styleable.CircleProgressView_circleProgressStartAngle, DEFAULT_START_ANGLE);
            mTextSize = a.getDimensionPixelSize(R.styleable.CircleProgressView_circleProgressTextSize, DEFAULT_TEXT_SIZE);
            mText = a.getString(R.styleable.CircleProgressView_circleProgressText);
            mStartAnimation = a.getInteger(R.styleable.CircleProgressView_circleProgressStartAnimation, ANIM_NONE);
            mStartAnimationDuration = a.getInteger(R.styleable.CircleProgressView_circleProgressStartAnimationDuration, DEFAULT_START_ANIMATION_DURATION);
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
        final String text = getText();

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

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // currently animations are ignored on devices that don't support
        // them natively
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return;
        }

        ObjectAnimator animator;
        switch (mStartAnimation) {
            case ANIM_ROLL:
                animator = ObjectAnimator.ofInt(this, "startAngle", mStartAngle - 180, mStartAngle);
                animator.setInterpolator(new DecelerateInterpolator());
                break;
            case ANIM_FADE_IN:
                animator = ObjectAnimator.ofFloat(this, "alpha", 0, 1);
                break;
            case ANIM_INCREMENTAL:
                animator = ObjectAnimator.ofInt(this, "value", 0, mValue);
                animator.setInterpolator(new DecelerateInterpolator());
                break;
            case ANIM_THICKNESS_EXPAND:
                animator = ObjectAnimator.ofInt(this, "thickness", 0, mThickness);
                animator.setInterpolator(new DecelerateInterpolator());
                break;
            default:
                animator = null;
        }

        if (animator != null) {
            animator.setDuration(mStartAnimationDuration);
            animator.start();
        }
    }

    /**
     * Sets the value of the progress circle.
     * @param value value in percentage of the progress bar. This must be
     *              in the 0-100 range (both included).
     */
    public void setValue(int value) {
        if (value < 0 || value > 100) {
            throw new IllegalArgumentException(
                    String.format("Value was %d but must be in the 0-100 range", value)
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
        if (thickness < 0) {
            throw new IllegalArgumentException(
                    String.format("Thickness was %d but must be positive", thickness)
            );
        }
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
     * Sets the text inside the circle progress bar.<br/>
     * If the text is {@code null}, the view will show the current value
     * inside the circle.
     * @param text input text or null to show the current value
     */
    public void setText(String text) {
        mText = text;
        invalidate();
    }

    /**
     * Returns the current text inside the circle progress bar.
     * @return text inside the circle progress bar or null if there's no
     *         text set
     */
    public String getText() {
        if (mText == null) {
            return Integer.toString(mValue);
        }
        return mText;
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
