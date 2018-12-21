package com.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
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
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int modeW = MeasureSpec.getMode(widthMeasureSpec);
        int modeH = MeasureSpec.getMode(heightMeasureSpec);
        View view = getChildAt(0);

        measureChildWithMargins(view, widthMeasureSpec, 0, heightMeasureSpec, 0);
        int childWidth = view.getMeasuredWidth();
        int childHeight = view.getMeasuredHeight();
        Log.i("life1", this + ":" + childWidth + ":" + childHeight);
        LayoutParams lp = (LayoutParams) view.getLayoutParams();
        int offsetX = 0;
        switch (modeW) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                // 当小红点超出子View的宽高时，计算超出的偏移量
                offsetX = (int) getOffset(childWidth);
                if (offsetX < 0)
                    offsetX = 0;

                //当提示的小点
                if (!config.singleHorizontalSide) {
                    offsetX = 2 * offsetX;
                }
                config.setOffsetX(offsetX);
                width = childWidth + lp.leftMargin + lp.rightMargin + getPaddingLeft() + getPaddingRight() + offsetX;
                break;
            case MeasureSpec.EXACTLY:
                int p = getPaddingLeft() + getPaddingRight();
                int margin = lp.leftMargin + lp.rightMargin;
                int result = (int) getOffset(childWidth);
                result = result + p + margin + childWidth;
                if (result > width) {
                    int r = (int) config.radius;
                    childWidth = config.util.getDivideValue(width - p - margin - 2 * r, config.getHorizontalMultiple()).intValue();
                }
                offsetX = (int) getOffset(childWidth);
                if (offsetX < 0)
                    offsetX = 0;

                //当提示的小点
                if (!config.singleHorizontalSide) {
                    offsetX = 2 * offsetX;
                }
                config.setOffsetX(offsetX);

                childHeight = (int) ((float) childWidth / view.getMeasuredWidth() * view.getMeasuredHeight());

                break;
        }

        config.width = childWidth;
        int offsetY = 0;
        switch (modeH) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                offsetY = (int) (config.radius - (1 - config.getHorizontalMultiple()) * childHeight / 2);
                if (offsetY < 0)
                    offsetY = 0;
                if (!config.singleVerticalSide)
                    offsetY = 2 * offsetY;

                config.setOffsetY(offsetY);
                height = childHeight + lp.topMargin + lp.bottomMargin + getPaddingTop() + getPaddingBottom() + offsetY;
                break;
            case MeasureSpec.EXACTLY:
                int p = getPaddingTop() + getPaddingBottom();
                int margin = lp.topMargin + lp.bottomMargin;
                int result = (int) getOffset(childHeight);
                result = result + p + margin + childHeight;
                if (result > height) {
                    int r = (int) config.radius;
                    childHeight = config.util.getDivideValue(height - p - margin - 2 * r, config.getVerticalMultiple()).intValue();
                }
                offsetY = (int) getOffset(childHeight);
                if (offsetY < 0)
                    offsetY = 0;

                //当提示的小点
                if (!config.singleHorizontalSide) {
                    offsetY = 2 * offsetY;
                }
                config.setOffsetY(offsetY);

                if (modeW == MeasureSpec.AT_MOST) {
                    childWidth = (int) ((float) childHeight / view.getMeasuredHeight() * view.getMeasuredWidth());
                    // 当小红点超出子View的宽高时，计算超出的偏移量
                    offsetX = (int) getOffset(childWidth);
                    if (offsetX < 0)
                        offsetX = 0;

                    //当提示的小点
                    if (!config.singleHorizontalSide) {
                        offsetX = 2 * offsetX;
                    }
                    config.setOffsetX(offsetX);
                    width = childWidth + lp.leftMargin + lp.rightMargin + getPaddingLeft() + getPaddingRight() + offsetX;
                }
                break;
        }

        config.height = childHeight;
        Log.i("life2", this + ":" + width + ":" + height);
        setMeasuredDimension(width, height);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        layoutChild(l, t, r, b);
    }

    private float getOffset(int size) {
        float result = (float) config.util.getAddValue(1, -config.getVerticalMultiple(), Float.class);
        result = (float) config.util.getMultiplyValue(result, size / 2);
        result = (float) config.util.getAddValue(config.radius, -result);
        return result;
    }

    private void layoutChild(int l, int t, int r, int b) {
        int width = r - l;// 该控件的宽
        int height = b - t;//该控件的高

        View view = getChildAt(0);
        MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();
        int left, right, top, bottom;//子view的坐标点
        int centerX, centerY;// 子view的中心点
        int areaL = getPaddingLeft();
        int areaR = width - getPaddingRight();
        int areaT = getPaddingTop();
        int areaB = height - getPaddingBottom();

        int widthR = view.getMeasuredWidth();
        int heightR = view.getMeasuredHeight();
        int w = config.width;//子view的宽
        int h = config.height;//子view的高
        if (w > width)
            w = width;
        if (h > height)
            h = height;

        float mul = 1f;
        if (widthR > w)
            mul = config.util.getDivideValue(w, widthR).floatValue();

        if (heightR > h) {
            float m = config.util.getDivideValue(h, heightR).floatValue();
            if (mul < m) {
                h = config.util.getMultiplyValue(heightR, mul).intValue();
            } else {
                w = config.util.getMultiplyValue(widthR, m).intValue();
            }
        }

        centerX = width / 2;
        centerY = height / 2;
        if (config.singleHorizontalSide) {
            if ((config.direction & 1) == 1) {//1代表靠左，2的0次方
                if (centerX - w / 2 - config.getOffsetX() < areaL)
                    centerX = areaL + w / 2 + config.getOffsetX();
            }
            if ((config.direction & 4) == 4) {//4代表靠右，2的2次方
                if (centerX + w / 2 + config.getOffsetX() > areaR)
                    centerX = areaR - w / 2 - config.getOffsetX();
            }
        }

        if (config.singleVerticalSide) {
            if ((config.direction & 2) == 2) {//2代表靠上
                if (centerY - h / 2 - config.getOffsetY() < areaR) {
                    centerY = areaT + h / 2 + config.getOffsetY();
                }
            }
            if ((config.direction & 8) == 8) {
                if (centerY + h / 2 + config.getOffsetY() > areaB) {
                    centerY = areaB - h / 2 - config.getOffsetY();
                }
            }
        }
        left = centerX - w / 2;
        right = centerX + w / 2;
        top = centerY - h / 2;
        bottom = centerY + h / 2;
        view.layout(left, top, right, bottom);

        centerX = (left + right) / 2;
        centerY = (top + bottom) / 2;
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
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, config.radius, config.getLinePaint());
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, config.radius - 2, config.getBackPaint());
                canvas.drawText(config.text, config.getRectF().centerX(), config.getCenterY(), config.getTextPaint());
            } else {
                config.setBackGroundColor(Color.TRANSPARENT);
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, config.radius, config.getBackPaint());
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
    }
}
