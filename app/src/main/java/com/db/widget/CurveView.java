package com.db.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.db.R;
import com.db.util.TransformUtil;
import com.db.weather.mvp.model.bean.FutureWeatherBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：lyy on 2017/6/27 16:02
 */

public class CurveView extends View {

    private static final float LINE_SMOOTHNESS = 0.16f;

    protected float density;

    private Paint linePaint = new Paint();

    private Paint pointPaint = new Paint();

    private Paint textPaint = new Paint();

    private Path path = new Path();

    private Path path2 = new Path();

    private int index = 0;

    private float pointRadius = 5;

    private float rawYTop = 45;

    private float rawYBottom = 25;

    //画曲线区域的真实高度
    private float curveHeight = Float.NaN;

    //曲线区域以外的高度
    private float curveExceptHeight = Float.NaN;

    //文字距离点的距离
    private float textMargin = 25;

    private List<FutureWeatherBean.WeatherBean.FutureBean> list = new ArrayList<>();

    public CurveView(Context context) {
        super(context);
    }

    public CurveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.density = context.getResources().getDisplayMetrics().density;
        prepareLinePaint();
    }

    public void setIndex(int index) {
        this.index = index;
        invalidate();
    }

    public void setData(List<FutureWeatherBean.WeatherBean.FutureBean> list) {
        if (this.list != null && list != null)
            this.list = list;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (list != null && list.size() > 0) {
            drawHeightPath(canvas, list);
            drawLowPath(canvas, list);
            drawPoint(canvas, list);
        }
    }

    private void drawPoint(Canvas canvas, List<FutureWeatherBean.WeatherBean.FutureBean> list) {

        float currentPointHighX = Float.NaN;
        float currentPointHighY = Float.NaN;
        float currentPointLowX = Float.NaN;
        float currentPointLowY = Float.NaN;

        int lowValue = list.get(index).getLow();
        int highValue = list.get(index).getHigh();

        String lowValueText = lowValue + "\u00b0";
        String highValueText = highValue + "\u00b0";

        curveExceptHeight = (textPaint.getTextSize() + textMargin) * 2;
        curveHeight = getHeight() - curveExceptHeight;

        currentPointHighX = currentPointLowX = this.getWidth() / 2;
        currentPointLowY = TransformUtil.temperatureTransformRawY(rawYTop, rawYBottom, lowValue, curveHeight,curveExceptHeight);
        currentPointHighY = TransformUtil.temperatureTransformRawY(rawYTop, rawYBottom, highValue,curveHeight,curveExceptHeight);

        canvas.drawCircle(currentPointLowX, currentPointLowY, pointRadius, pointPaint);
        canvas.drawCircle(currentPointHighX, currentPointHighY, pointRadius, pointPaint);

        drawText(canvas, currentPointHighX, currentPointHighY - textMargin, highValueText);
        drawText(canvas, currentPointLowX, currentPointLowY + textMargin + textPaint.measureText(lowValueText), lowValueText);
    }

    private void drawText(Canvas canvas, float startX, float startY, String text) {
        canvas.drawText(text, startX-textPaint.measureText(text)/2, startY, textPaint);
    }

    private void drawHeightPath(Canvas canvas, List<FutureWeatherBean.WeatherBean.FutureBean> list) {

        final int lineSize = list.size();
        float prePreviousPointX = Float.NaN;
        float prePreviousPointY = Float.NaN;
        float previousPointX = Float.NaN;
        float previousPointY = Float.NaN;
        float currentPointX = Float.NaN;
        float currentPointY = Float.NaN;
        float nextPointX = Float.NaN;
        float nextPointY = Float.NaN;
        float nextNPointX = Float.NaN;
        float nextNPointY = Float.NaN;

        curveExceptHeight = (textPaint.getTextSize() + textMargin) * 2;
        curveHeight = getHeight() - curveExceptHeight;

//        for (int valueIndex = 0; valueIndex < lineSize; ++valueIndex) {
        if (Float.isNaN(currentPointX)) {
            currentPointX = this.getWidth() / 2;
            currentPointY = TransformUtil.temperatureTransformRawY(rawYTop, rawYBottom, list.get(index).getHigh(),curveHeight,curveExceptHeight);
        }
        if (Float.isNaN(previousPointX)) {
            if (index > 0) {
                previousPointX = this.getWidth() / 2 - this.getWidth();
                previousPointY = TransformUtil.temperatureTransformRawY(rawYTop, rawYBottom, list.get(index - 1).getHigh(),curveHeight,curveExceptHeight);
            } else {
                previousPointX = currentPointX;
                previousPointY = currentPointY;
            }
        }

        if (Float.isNaN(prePreviousPointX)) {
            if (index > 1) {
                prePreviousPointX = this.getWidth() / 2 - this.getWidth() * 2;
                prePreviousPointY = TransformUtil.temperatureTransformRawY(rawYTop, rawYBottom, list.get(index - 2).getHigh(),curveHeight,curveExceptHeight);
            } else {
                prePreviousPointX = previousPointX;
                prePreviousPointY = previousPointY;
            }
        }

        // nextPoint is always new one or it is equal currentPoint.
        if (index < lineSize - 1) {
            nextPointX = this.getWidth() / 2 + this.getWidth();
            nextPointY = TransformUtil.temperatureTransformRawY(rawYTop, rawYBottom, list.get(index + 1).getHigh(),curveHeight,curveExceptHeight);
        } else {
            nextPointX = currentPointX;
            nextPointY = currentPointY;
        }

        if (index < lineSize - 2) {
            nextNPointX = this.getWidth() / 2 + this.getWidth() * 2;
            nextNPointY = TransformUtil.temperatureTransformRawY(rawYTop, rawYBottom, list.get(index + 2).getHigh(),curveHeight,curveExceptHeight);
        } else {
            nextNPointX = currentPointX;
            nextNPointY = currentPointY;
        }

        //中点左边的曲线
        if (index > 0) {
            path.moveTo(-this.getWidth() / 2, TransformUtil.temperatureTransformRawY(rawYTop, rawYBottom, list.get(index - 1).getHigh(),curveHeight,curveExceptHeight));
            final float firstDiffX = (currentPointX - prePreviousPointX);
            final float firstDiffY = (currentPointY - prePreviousPointY);
            final float secondDiffX = (nextPointX - previousPointX);
            final float secondDiffY = (nextPointY - previousPointY);
            final float firstControlPointX = previousPointX + (LINE_SMOOTHNESS * firstDiffX);
            final float firstControlPointY = previousPointY + (LINE_SMOOTHNESS * firstDiffY);
            final float secondControlPointX = currentPointX - (LINE_SMOOTHNESS * secondDiffX);
            final float secondControlPointY = currentPointY - (LINE_SMOOTHNESS * secondDiffY);
            path.cubicTo(firstControlPointX, firstControlPointY, secondControlPointX, secondControlPointY,
                    currentPointX, currentPointY);
        }

        //中点右边的曲线
        if (index < lineSize - 1) {
            path2.moveTo(this.getWidth() / 2, TransformUtil.temperatureTransformRawY(rawYTop, rawYBottom, list.get(index).getHigh(),curveHeight,curveExceptHeight));
            final float firstDiffX2 = (nextPointX - previousPointX);
            final float firstDiffY2 = (nextPointY - previousPointY);
            final float secondDiffX2 = (nextNPointX - currentPointX);
            final float secondDiffY2 = (nextNPointY - currentPointY);
            final float firstControlPointX2 = currentPointX + (LINE_SMOOTHNESS * firstDiffX2);
            final float firstControlPointY2 = currentPointY + (LINE_SMOOTHNESS * firstDiffY2);
            final float secondControlPointX2 = nextPointX - (LINE_SMOOTHNESS * secondDiffX2);
            final float secondControlPointY2 = nextPointY - (LINE_SMOOTHNESS * secondDiffY2);
            path2.cubicTo(firstControlPointX2, firstControlPointY2, secondControlPointX2, secondControlPointY2,
                    nextPointX, nextPointY);
        }

        canvas.drawPath(path, linePaint);
        canvas.drawPath(path2, linePaint);
        path.reset();
        path2.reset();
    }

    private void drawLowPath(Canvas canvas, List<FutureWeatherBean.WeatherBean.FutureBean> list) {

        final int lineSize = list.size();
        float prePreviousPointX = Float.NaN;
        float prePreviousPointY = Float.NaN;
        float previousPointX = Float.NaN;
        float previousPointY = Float.NaN;
        float currentPointX = Float.NaN;
        float currentPointY = Float.NaN;
        float nextPointX = Float.NaN;
        float nextPointY = Float.NaN;
        float nextNPointX = Float.NaN;
        float nextNPointY = Float.NaN;

        curveExceptHeight = (textPaint.getTextSize() + textMargin) * 2;
        curveHeight = getHeight() - curveExceptHeight;

        if (Float.isNaN(currentPointX)) {
            currentPointX = this.getWidth() / 2;
            currentPointY = TransformUtil.temperatureTransformRawY(rawYTop, rawYBottom, list.get(index).getLow(),curveHeight,curveExceptHeight);
        }
        if (Float.isNaN(previousPointX)) {
            if (index > 0) {
                previousPointX = this.getWidth() / 2 - this.getWidth();
                previousPointY = TransformUtil.temperatureTransformRawY(rawYTop, rawYBottom, list.get(index - 1).getLow(),curveHeight,curveExceptHeight);
            } else {
                previousPointX = currentPointX;
                previousPointY = currentPointY;
            }
        }

        if (Float.isNaN(prePreviousPointX)) {
            if (index > 1) {
                prePreviousPointX = this.getWidth() / 2 - this.getWidth() * 2;
                prePreviousPointY = TransformUtil.temperatureTransformRawY(rawYTop, rawYBottom, list.get(index - 2).getLow(),curveHeight,curveExceptHeight);
            } else {
                prePreviousPointX = previousPointX;
                prePreviousPointY = previousPointY;
            }
        }

        // nextPoint is always new one or it is equal currentPoint.
        if (index < lineSize - 1) {
            nextPointX = this.getWidth() / 2 + this.getWidth();
            nextPointY = TransformUtil.temperatureTransformRawY(rawYTop, rawYBottom, list.get(index + 1).getLow(),curveHeight,curveExceptHeight);
        } else {
            nextPointX = currentPointX;
            nextPointY = currentPointY;
        }

        if (index < lineSize - 2) {
            nextNPointX = this.getWidth() / 2 + this.getWidth() * 2;
            nextNPointY = TransformUtil.temperatureTransformRawY(rawYTop, rawYBottom, list.get(index + 2).getLow(),curveHeight,curveExceptHeight);
        } else {
            nextNPointX = currentPointX;
            nextNPointY = currentPointY;
        }

        //中点左边的曲线
        if (index > 0) {
            path.moveTo(-this.getWidth() / 2, TransformUtil.temperatureTransformRawY(rawYTop, rawYBottom, list.get(index - 1).getLow(),curveHeight,curveExceptHeight));
            final float firstDiffX = (currentPointX - prePreviousPointX);
            final float firstDiffY = (currentPointY - prePreviousPointY);
            final float secondDiffX = (nextPointX - previousPointX);
            final float secondDiffY = (nextPointY - previousPointY);
            final float firstControlPointX = previousPointX + (LINE_SMOOTHNESS * firstDiffX);
            final float firstControlPointY = previousPointY + (LINE_SMOOTHNESS * firstDiffY);
            final float secondControlPointX = currentPointX - (LINE_SMOOTHNESS * secondDiffX);
            final float secondControlPointY = currentPointY - (LINE_SMOOTHNESS * secondDiffY);
            path.cubicTo(firstControlPointX, firstControlPointY, secondControlPointX, secondControlPointY,
                    currentPointX, currentPointY);
        }

        //中点右边的曲线
        if (index < lineSize - 1) {
            path2.moveTo(this.getWidth() / 2, TransformUtil.temperatureTransformRawY(rawYTop, rawYBottom, list.get(index).getLow(),curveHeight,curveExceptHeight));
            final float firstDiffX2 = (nextPointX - previousPointX);
            final float firstDiffY2 = (nextPointY - previousPointY);
            final float secondDiffX2 = (nextNPointX - currentPointX);
            final float secondDiffY2 = (nextNPointY - currentPointY);
            final float firstControlPointX2 = currentPointX + (LINE_SMOOTHNESS * firstDiffX2);
            final float firstControlPointY2 = currentPointY + (LINE_SMOOTHNESS * firstDiffY2);
            final float secondControlPointX2 = nextPointX - (LINE_SMOOTHNESS * secondDiffX2);
            final float secondControlPointY2 = nextPointY - (LINE_SMOOTHNESS * secondDiffY2);
            path2.cubicTo(firstControlPointX2, firstControlPointY2, secondControlPointX2, secondControlPointY2,
                    nextPointX, nextPointY);
        }

        canvas.drawPath(path, linePaint);
        canvas.drawPath(path2, linePaint);
        path.reset();
        path2.reset();
    }

    private void prepareLinePaint() {
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(3);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        linePaint.setColor(getResources().getColor(R.color.color_white));

        pointPaint.setAntiAlias(true);
        pointPaint.setStyle(Paint.Style.FILL);
        pointPaint.setColor(getResources().getColor(R.color.color_white));

        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(40);
        textPaint.setColor(getResources().getColor(R.color.color_white));
    }
}
