package com.db.weather;

import android.content.res.Resources;
import android.graphics.Bitmap;

import com.blankj.utilcode.util.ImageUtils;
import com.db.R;

/**
 * 作者：lyy on 2017/6/29 11:32
 */

public class WeatherImageUtil {

    public static Bitmap getImg(Resources resources, int weatherCode) {
        Bitmap bitmap = null;

        if (weatherCode == 11)
            bitmap = ImageUtils.getBitmap(resources, R.drawable.w_icon_4);
        else if (weatherCode == 4)
            bitmap = ImageUtils.getBitmap(resources, R.drawable.w_icon_2);
        else if (weatherCode == 9)
            bitmap = ImageUtils.getBitmap(resources, R.drawable.w_icon_1);
        else
            bitmap = ImageUtils.getBitmap(resources, R.drawable.w_icon_0);

        return bitmap;
    }

    public static Integer getResourceId(int weatherCode) {

        if (weatherCode == 11)
            return R.drawable.w_icon_4;
        else if (weatherCode == 4)
            return R.drawable.w_icon_2;
        else if (weatherCode == 9)
            return R.drawable.w_icon_1;
        else
            return R.drawable.w_icon_0;

    }
}
