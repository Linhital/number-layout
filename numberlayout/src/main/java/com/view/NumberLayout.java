package com.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.view.config.NumberConfig;
import com.view.utils.BigDecUtil;

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
        LayoutParams lp = (LayoutParams) view.getLayoutParams();
        int offsetX = 0;
        switch (modeW) {
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
                    childWidth = (int) ((width - p - margin - 2 * config.radius) / config.getHorizontalMultiple());
                }
                offsetX = (int) getOffset(childWidth);
                if (offsetX < 0)
                    offsetX = 0;

                //当提示的小点
                if (!config.singleHorizontalSide) {
                    offsetX = 2 * offsetX;
                }
                config.setOffsetX(offsetX);
                break;
        }

        config.width = childWidth;

        switch (modeH) {
            case MeasureSpec.AT_MOST:
                int offsetY = (int) (config.radius - (1 - config.getHorizontalMultiple()) * childHeight / 2);
                if (offsetY < 0)
                    offsetY = 0;
                if (!config.singleVerticalSide)
                    offsetY = 2 * offsetY;

                config.setOffsetY(offsetY);
                height = childHeight + lp.topMargin + lp.bottomMargin + getPaddingTop() + getPaddingBottom() + offsetY;
                break;
            case MeasureSpec.EXACTLY:
                break;

        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        layoutChild(l, t, r, b);

//        View view = getChildAt(0);
//        MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();
//        int left = getPaddingLeft() + lp.leftMargin;
//        int top = getPaddingTop() + lp.topMargin;
//        int rightParent = r - l - getPaddingRight() - lp.rightMargin;
//        int bottomParent = b - t - getPaddingBottom() - lp.bottomMargin;
//        int right = left + view.getMeasuredWidth();
//        int bottom = top + view.getMeasuredHeight();
//        if (rightParent > right) {
//            left = (rightParent - right) / 2 + left;
//            right = (rightParent - right) / 2 + right;
//        }
//
//        if (bottomParent > bottom) {
//            top = (bottomParent - bottom) / 2 + top;
//            bottom = (bottomParent - bottom) / 2 + top;
//        }
//        if (config.singleHorizontalSide) {
//            if ((config.direction & 1) == 1)
//                left += config.getOffsetX();
//            if ((config.direction & 4) == 4)
//                right = right - config.getOffsetX();
//        } else {
//            left += config.getOffsetX();
//            right = right - config.getOffsetX();
//        }
//        if (config.singleVerticalSide) {
//            if ((config.direction & 2) == 2)
//                top += config.getOffsetY();
//            if ((config.direction & 8) == 8)
//                bottom = bottom - config.getOffsetY();
//        } else {
//            top += config.getOffsetY();
//            bottom = bottom - config.getOffsetY();
//        }
//        view.layout(left, top, right, bottom);
//
//
//        int centerX = (left + right) / 2;
//        int centerY = (top + bottom) / 2;
//        int direction = config.direction;
//        if (config.direction == 15)
//            direction = 0;
//        if ((direction & 1) == 1)
//            centerX = (int) (centerX - (centerX - left) * config.getVerticalMultiple());
//
//        if ((direction & 2) == 2)
//            centerY = (int) (centerY - (centerY - top) * config.getHorizontalMultiple());
//
//        if ((direction & 4) == 4)
//            centerX = (int) (centerX + (rightParent - centerX) * config.getVerticalMultiple());
//
//        if ((direction & 8) == 8)
//            centerY = (int) (centerY + (bottomParent - centerY) * config.getHorizontalMultiple());
//
//
//        View cue = getChildAt(1);
//        if (config.isVisible) {
//            cue.layout((int) (centerX - config.radius), (int) (centerY - config.radius), (int) (centerX + config.radius), (int) (centerY + config.radius));
//        } else {
//            removeView(cue);
//        }
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

        int w = config.width;//子view的宽
        int h = view.getMeasuredHeight();//子view的高
        BigDecUtil util = new BigDecUtil();
        float mul = 1f;
//        if (w > width - config.getOffsetX()) {
//            mul = (float) util.getDivideValue(width - config.getOffsetX(), w);
//        }
        if (h > height - config.getOffsetY()) {
            int m = (int) util.getDivideValue(height - config.getOffsetY(), h);
            if (m < mul)
                mul = m;
        }
//        w = (int) util.getMultiplyValue(w, mul, Integer.class);
        h = (int) util.getMultiplyValue(h, mul, Integer.class);
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
        } else {

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
        } else {

        }
        left = centerX - w / 2;
        right = centerX + w / 2;
        top = centerY - h / 2;
        bottom = centerY + h / 2;
        view.layout(left, top, left + config.width, bottom);

        left = left + lp.leftMargin + getPaddingLeft();
        right = right - lp.rightMargin - getPaddingRight();
        top = top + lp.topMargin + getPaddingTop();
        bottom = bottom + lp.bottomMargin + getPaddingBottom();
//        view.layout(left, top, left + config.width, bottom);
        if (left < areaL) {
            left = areaL;
        }
        if (right > areaR)
            right = areaR;
        if (top < areaT)
            top = areaT;
        if (bottom > areaB)
            bottom = areaB;
//        view.layout(left, top, left + config.width, bottom);

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
