package com.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.view.config.NumberConfig;

public class NumberLayout extends FrameLayout {
    private NumberConfig config = new NumberConfig();

    public NumberLayout(Context context) {
        this(context, null);
    }

    public NumberLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        config.init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() == 1 && config.isVisible) {
            CueView view = new CueView(config.mContext);
            addView(view);
        } else if (getChildCount() != 2 && config.isVisible) {
            throw new RuntimeException("child's count is wrong");
        }
        int w = getMeasuredWidth();
        int h = getMeasuredHeight();
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int modeW = MeasureSpec.getMode(widthMeasureSpec);
        int modeH = MeasureSpec.getMode(heightMeasureSpec);

        switch (modeW) {
            case MeasureSpec.AT_MOST:
                measureChildren(widthMeasureSpec, heightMeasureSpec);
                View view = getChildAt(0);
                int childWidth = view.getMeasuredWidth();
                int childHeight = view.getMeasuredHeight();

                LayoutParams lp = (LayoutParams) view.getLayoutParams();
                // 当小红点超出子View的宽高时，计算超出的偏移量
                int offsetX = (int) (config.radius - (1 - config.getVerticalMultiple()) * childWidth / 2);
                int offsetY = (int) (config.radius - (1 - config.getHorizontalMultiple()) * childHeight / 2);
                if (offsetX < 0)
                    offsetX = 0;
                if (offsetY < 0)
                    offsetY = 0;

                //当提示的小点
                if (!config.singleHorizontalSide) {
                    offsetX = 2 * offsetX;
                }

                if (!config.singleVerticalSide) {
                    offsetY = 2 * offsetY;
                }

                config.setOffsetX(offsetX);
                config.setOffsetY(offsetY);

                width = childWidth + lp.leftMargin + lp.rightMargin + getPaddingLeft() + getPaddingRight() + offsetX;
                height = childHeight + lp.topMargin + lp.bottomMargin + getPaddingTop() + getPaddingBottom() + offsetY;
                break;
            case MeasureSpec.EXACTLY:
                if (width > height)
                    width = height;
                else
                    height = width;
                break;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View view = getChildAt(0);
        LayoutParams lp = (LayoutParams) view.getLayoutParams();
        int left = getPaddingLeft() + lp.leftMargin;
        int top = getPaddingTop() + lp.topMargin;
        int right = r - l - getPaddingRight() - lp.rightMargin;
        int bottom = b - t - getPaddingBottom() - lp.bottomMargin;
        if (config.singleHorizontalSide) {
            if ((config.direction & 1) == 1)
                left += config.getOffsetX();
            if ((config.direction & 4) == 4)
                right = right - config.getOffsetX();
        } else {
            left += config.getOffsetX();
            right = right - config.getOffsetX();
        }
        if (config.singleVerticalSide) {
            if ((config.direction & 2) == 2)
                top += config.getOffsetY();
            if ((config.direction & 8) == 8)
                bottom = bottom - config.getOffsetY();
        } else {
            top += config.getOffsetY();
            bottom = bottom - config.getOffsetY();
        }
        view.layout(left, top, right, bottom);


        int centerX = (left + right) / 2;
        int centerY = (top + bottom) / 2;
        int direction = config.direction;
        if (config.direction == 15)
            direction = 0;
        if ((direction & 1) == 1)
            centerX = (int) (centerX - (centerX - left) * config.getVerticalMultiple());

        if ((direction & 2) == 2)
            centerY = (int) (centerY - (centerY - top) * config.getHorizontalMultiple());

        if ((direction & 4) == 4)
            centerX = (int) (centerX + (right - centerX) * config.getVerticalMultiple());

        if ((direction & 8) == 8)
            centerY = (int) (centerY + (bottom - centerY) * config.getHorizontalMultiple());


        View cue = getChildAt(1);
        if (config.isVisible) {
            cue.layout((int) (centerX - config.radius), (int) (centerY - config.radius), (int) (centerX + config.radius), (int) (centerY + config.radius));
        } else {
            removeView(cue);
        }
    }

    private class CueView extends View {

        public CueView(Context context) {
            this(context, null);
        }

        public CueView(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public CueView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            setMeasuredDimension((int) (config.radius * 2), (int) (config.radius * 2));
            config.initTextRect(0, 0, (int) (config.radius * 2), (int) (config.radius * 2));
        }

        @Override
        protected void onDraw(Canvas canvas) {
            if (config.isVisible) {
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, config.getLinePaint());
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2 - 3, config.getBackPaint());
                canvas.drawText(config.text, config.getRectF().centerX(), config.getCenterY(), config.getTextPaint());
            } else {
                config.setBackGroundColor(Color.TRANSPARENT);
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2 - 3, config.getBackPaint());
            }
        }
    }


    public void setSingleVerticalSide(boolean singleVerticalSide) {
        config.singleVerticalSide = singleVerticalSide;
        requestLayout();
    }

    public void setSingleHorizontalSide(boolean singleHorizontalSide) {
        config.singleHorizontalSide = singleHorizontalSide;
        requestLayout();
    }

    public void setScaleCenter(float scaleCenter) {
        config.scaleCenter = scaleCenter;
        requestLayout();
    }

    public void setDirection(int direction) {
        config.direction = direction;
        requestLayout();
    }

    public void setRadius(float radius) {
        config.radius = radius;
        requestLayout();
    }

    public void setTextColor(int textColor) {
        config.textColor = textColor;
        invalidate();
    }

    public void setBackGroundColor(int backGroundColor) {
        config.backGroundColor = backGroundColor;
        invalidate();
    }

    public void setLine(boolean line) {
        config.setLine(line);
        invalidate();
    }

    public void setText(String text) {
        if (text == null)
            throw new NullPointerException();
        config.text = text;
        invalidate();
    }

    public void setClipVertical(float clipVertical) {
        config.clipVertical = clipVertical;
        requestLayout();
    }

    public void setClipHorizontal(float clipHorizontal) {
        config.clipHorizontal = clipHorizontal;
        requestLayout();
    }

    public void setVisible(boolean visible) {
        config.isVisible = visible;
        requestLayout();
//        if (visible)
//            invalidate();
    }
}
