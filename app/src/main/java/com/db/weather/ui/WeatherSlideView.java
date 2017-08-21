package com.db.weather.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.view.View;

import com.db.R;
import com.db.weather.WeatherImageUtil;

/**
 * 作者：lyy on 2017/6/23 11:37
 */

public class WeatherSlideView extends View {

    private float startX;
    private float startY;
    //整个view的高度
    private float mAllHeight;
    //整个view去掉尖角部分之后矩形的高度
    private float mHeight;
    private float mWidth;
    private float mCornerWidth;
    private float mCornerHeight;
    private float borderWidth;
    //尖角距离曲线的高度
    private float marginHeight = 15;

    private int borderColor;
    private int backgroundColor;

    //当前尖角对应的纵坐标
    private int currentCornerY;

    //内容距离边框的距离
    private float contentMargin = 10;

    private int textDefaultWidth = 43;

    private int textCurrentWidth = textDefaultWidth;

    private Paint mPaint = new Paint();

    private Paint textPaint = new Paint();

    private Path mPath = new Path();

    public WeatherSlideView(Context context) {
        super(context);
    }

    public WeatherSlideView(Context context, float startX, float startY, float mHeight, float mWidth,
                            float mCornerWidth, float mCornerHeight, float borderWidth, int borderColor,
                            int backgroundColor) {
        super(context);
        this.startX = startX;
        this.startY = startY;
        this.mWidth = mWidth;
        this.mAllHeight = mHeight;
        this.mHeight = mHeight - mCornerHeight;
        this.mCornerWidth = mCornerWidth;
        this.mCornerHeight = mCornerHeight;
        this.borderWidth = borderWidth;
        this.borderColor = borderColor;
        this.backgroundColor = backgroundColor;

        mPaint.setColor(borderColor);
        mPaint.setStrokeWidth(borderWidth);
        mPaint.setAntiAlias(true);
    }

    public void setParameter(float startX, float startY, float mHeight, float mWidth, float mCornerWidth,
                             float mCornerHeight, float borderWidth, int borderColor, int backgroundColor) {

        this.startX = startX;
        this.startY = startY - mHeight - marginHeight;
        this.currentCornerY = (int) startY;
        this.mWidth = mWidth;
        this.mAllHeight = mHeight;
        this.mHeight = mHeight - mCornerHeight;
        this.mCornerWidth = mCornerWidth;
        this.mCornerHeight = mCornerHeight;
        this.borderWidth = borderWidth;
        this.borderColor = borderColor;
        this.backgroundColor = backgroundColor;

        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(getResources().getColor(R.color.color_white_4));
        this.mPaint.setStyle(Paint.Style.FILL);
        this.mPaint.setStrokeWidth(borderWidth);

        this.textPaint.setAntiAlias(true);
        this.textPaint.setColor(borderColor);
        this.textPaint.setStyle(Paint.Style.FILL);
        this.textPaint.setStrokeWidth(1);

    }


    public void draw(Canvas canvas, String temperature, String src, int weatherCode) {

        textCurrentWidth = textDefaultWidth * (src.length() - 1);
//        mWidth = mWidth + textCurrentWidth;

        drawBackground(canvas);

        drawContent(canvas, temperature + "\u00b0", src, weatherCode);
    }

    private void drawContent(Canvas canvas, String text, String src, int weatherCode) {
        textPaint.setTextSize(35);
        int bitmapSize = 50;
        int textStartX = (int) (startX + contentMargin * 2);
        int textStartY = (int) (startY + mHeight / 2 + textPaint.getTextSize() / 2);

        Bitmap bitmap = WeatherImageUtil.getImg(getResources(), weatherCode);

        canvas.drawText(text, startX + contentMargin, startY + mHeight / 2 + textPaint.getTextSize() / 2, textPaint);
        canvas.drawText(src, startX + mWidth - 45 - textCurrentWidth, startY + mHeight / 2 + textPaint.getTextSize() / 2, textPaint);

        Rect rect = new Rect((int) (textStartX + textPaint.measureText(text)),
                (int) (startY + mHeight / 2 - bitmapSize / 2),
                (int) (textStartX + textPaint.measureText(text) + bitmapSize),
                (int) (startY + mHeight / 2 - bitmapSize / 2 + bitmapSize));
        canvas.drawBitmap(bitmap, null, rect, textPaint);
    }

    private void drawBackground(Canvas canvas) {
        mPath.moveTo(startX, startY);
        mPath.lineTo(startX + mWidth, startY);

        mPath.lineTo(startX + mWidth, startY + mHeight);

        mPath.lineTo(startX + mWidth / 2 + mCornerWidth / 2, startY + mHeight);

        mPath.lineTo(startX + mWidth / 2, startY + mAllHeight);

        mPath.lineTo(startX + mWidth / 2 - mCornerWidth / 2, startY + mHeight);

        mPath.lineTo(startX, startY + mHeight);

        mPath.lineTo(startX, startY);

        canvas.drawPath(mPath, mPaint);

        mPath.reset();
    }
}
