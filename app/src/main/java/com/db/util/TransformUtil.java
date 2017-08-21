package com.db.util;

/**
 * 作者：lyy on 2017/6/28 14:11
 */

public class TransformUtil {

    public static float temperatureTransformRawY(float top, float bottom, float currentVaule, float viewHeight, float viewMargin) {
        float rawY = (currentVaule - bottom) / (top - bottom) * viewHeight;
        return viewHeight - rawY + viewMargin / 4;
    }
}
