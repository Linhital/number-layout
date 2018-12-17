package com.view.config;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.view.numberlayout.R;
import com.view.utils.BigDecUtil;

public class NumberConfig {
    public Context mContext;
    public boolean singleVerticalSide;//如果垂直方向超出空间，是否上下两边都需要超出空间
    public boolean singleHorizontalSide;//如果水平方向超出空间，是否左右两边都需要超出空间
    public float scaleCenter;//提示view原点在连线上的的位置
    public int direction;//提示view所在的象限
    public float radius;//提示view的半径
    public int textColor;//提示view中的字体颜色
    public int backGroundColor;//提示view的背景颜色
    private int offsetX;//提示view与子view水平方向的偏移量
    private int offsetY;//提示view与子view垂直方向的偏移量
    private boolean isLine;//是否为提示添加圆圈
    public String text = "";//提示字
    public float clipVertical;
    public float clipHorizontal;
    public boolean isVisible;

    private RectF rectF;//字体居中所需的辅助长方形
    private Paint textPaint;//提示view中的字体画笔
    private Paint backPaint;//提示view中的背景画笔
    BigDecUtil util = new BigDecUtil();

    public void init(Context context, AttributeSet attrs) {
        mContext = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NumberLayout);
        singleVerticalSide = a.getBoolean(R.styleable.NumberLayout_single_vertical_side, false);
        singleHorizontalSide = a.getBoolean(R.styleable.NumberLayout_single_horizontal_side, false);
        scaleCenter = a.getFloat(R.styleable.NumberLayout_scale_center, 1);
        direction = a.getInt(R.styleable.NumberLayout_direction, 15);
        radius = a.getDimension(R.styleable.NumberLayout_cue_radius, 20);
        textColor = a.getColor(R.styleable.NumberLayout_cue_textColor, Color.GRAY);
        backGroundColor = a.getColor(R.styleable.NumberLayout_cue_backgrounColor, Color.WHITE);
        isLine = a.getBoolean(R.styleable.NumberLayout_circle_line, false);
        text = a.getString(R.styleable.NumberLayout_text);
        clipVertical = a.getFloat(R.styleable.NumberLayout_clip_vertical, 1);
        clipHorizontal = a.getFloat(R.styleable.NumberLayout_clip_horizontal, 1);
        isVisible = a.getBoolean(R.styleable.NumberLayout_is_visible, true);
        a.recycle();
        if (direction == 15)
            scaleCenter = 0;
        if (text == null)
            text = "";

    }


    public void initTextRect(int left, int top, int right, int bottom) {
        rectF = new RectF(left, top, right, bottom);
        getTextPaint();
    }

    public int getOffsetX() {
        if (!singleHorizontalSide) {
            return offsetX / 2;
        }
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetY() {
        if (!singleVerticalSide)
            return offsetY / 2;
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public Paint getTextPaint() {
        if (textPaint == null) {
            textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            textPaint.setTextSize(radius);
            textPaint.setStyle(Paint.Style.FILL);
            textPaint.setTextAlign(Paint.Align.CENTER);
        }
        textPaint.setColor(textColor);
        return textPaint;
    }

    public int getCenterY() {
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
        int baseLineY = (int) (rectF.centerY() - top / 2 - bottom / 2);//基线中间点的y轴计算公式
        return baseLineY;
    }

    public void setTextPaint(Paint textPaint) {
        this.textPaint = textPaint;
    }

    public Paint getBackPaint() {
        if (backPaint == null) {
            backPaint = new Paint();
            backPaint.setColor(Color.GRAY);
            backPaint.setStyle(Paint.Style.FILL);
            //设置抗锯齿
            backPaint.setAntiAlias(true);
        }
        backPaint.setColor(backGroundColor);
        return backPaint;
    }

    public void setBackPaint(Paint backPaint) {
        this.backPaint = backPaint;
    }

    public RectF getRectF() {
        return rectF;
    }

    public Paint getLinePaint() {
        if (isLine) {
            return getTextPaint();
        } else
            return getBackPaint();
    }

    public float getVerticalMultiple() {
        return (float) util.getMultiplyValue(clipVertical, scaleCenter);
    }

    public float getHorizontalMultiple() {
        return (float) util.getMultiplyValue(clipHorizontal, scaleCenter);
    }

    public void setSingleVerticalSide(boolean singleVerticalSide) {
        this.singleVerticalSide = singleVerticalSide;
    }

    public void setSingleHorizontalSide(boolean singleHorizontalSide) {
        this.singleHorizontalSide = singleHorizontalSide;
    }

    public void setScaleCenter(float scaleCenter) {
        this.scaleCenter = scaleCenter;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setBackGroundColor(int backGroundColor) {
        this.backGroundColor = backGroundColor;
    }

    public void setLine(boolean line) {
        isLine = line;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setClipVertical(float clipVertical) {
        this.clipVertical = clipVertical;
    }

    public void setClipHorizontal(float clipHorizontal) {
        this.clipHorizontal = clipHorizontal;
    }
}
