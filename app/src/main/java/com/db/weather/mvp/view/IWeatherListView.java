package com.db.weather.mvp.view;

import com.db.util.retrofit.exception.IErrorView;
import com.db.weather.mvp.model.bean.DailyWeatherListBean;
import com.db.weather.mvp.model.bean.FutureWeatherBean;

public interface IWeatherListView extends IErrorView {

    void updateDailyWeather(DailyWeatherListBean bean);//更新天气详情

    void updateFutureWeather(FutureWeatherBean bean);//更新天气
}
