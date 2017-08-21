package com.db.weather.mvp.presenter;


public interface IWeatherListPresenter {

    void getDailyWeather(String key, String language, String city);//请求天气预报细节

    void getFutureWeather( String city);//请求天气预报
}
