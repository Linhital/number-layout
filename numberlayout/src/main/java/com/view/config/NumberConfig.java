package com.view.config;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.view.numberlayout.R;

public class NumberConfig {
    public Context mContext;
    public boolean singleVerticalSide;
    public boolean singlehorizontalSide;
    public float scaleCenter;
    public int direction;
    public float radius;
    public int textColor;
    public int backGroundColor;
    private int offsetX;
    private int offsetY;
    private RectF rectF;
    private Paint textPaint;
    private Paint backPaint;

    public void init(Context context, AttributeSet attrs) {
        mContext = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NumberLayout);
        singleVerticalSide = a.getBoolean(R.styleable.NumberLayout_single_vertical_side, false);
        singlehorizontalSide = a.getBoolean(R.styleable.NumberLayout_single_horizontal_side, false);
        scaleCenter = a.getFloat(R.styleable.NumberLayout_scale_center, 1);
        direction = a.getInt(R.styleable.NumberLayout_direction, 15);
        radius = a.getDimension(R.styleable.NumberLayout_cue_radius, 20);
        textColor = a.getColor(R.styleable.NumberLayout_cue_textColor, Color.GRAY);
        backGroundColor = a.getColor(R.styleable.NumberLayout_cue_backgrounColor, Color.WHITE);
        a.recycle();
        if (direction == 15)
            scaleCenter = 0;

    }


    public void initTextRect(int left, int top, int right, int bottom) {
        rectF = new RectF(left, top, right, bottom);
        getTextPaint();
    }

    public int getOffsetX() {
        if (!singlehorizontalSide) {
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
}
