package com.db.weather.mvp.presenter.impl;


import com.db.API;
import com.db.util.GsonHelper;
import com.db.util.retrofit.HttpSubscriber;
import com.db.util.retrofit.RetrofitHelper;
import com.db.weather.mvp.model.bean.DailyWeatherListBean;
import com.db.weather.mvp.model.bean.FutureWeatherBean;
import com.db.weather.mvp.presenter.IWeatherListPresenter;
import com.db.weather.mvp.view.IWeatherListView;
import com.google.gson.JsonObject;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class WeatherListPresenterImpl implements IWeatherListPresenter {

    private final IWeatherListView view;

    public WeatherListPresenterImpl(IWeatherListView view) {
        this.view = view;
    }

    @Override
    public void getDailyWeather(String key, String language, String city) {
        RetrofitHelper.getWeatherRetrofitHelper()
                .create(API.class)
                .getDailyWeather(city, language, key)
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<JsonObject, Publisher<DailyWeatherListBean>>() {
                    @Override
                    public Publisher<DailyWeatherListBean> apply(@NonNull JsonObject jsonObject) throws Exception {
                        DailyWeatherListBean bean = (DailyWeatherListBean) GsonHelper.parseJson(jsonObject, DailyWeatherListBean.class);
                        return Flowable.just(bean);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpSubscriber<DailyWeatherListBean>() {
                    @Override
                    public void _onNext(DailyWeatherListBean futureWeatherListBean) {
                        view.updateDailyWeather(futureWeatherListBean);
                    }

                    @Override
                    public void _onError(String message) {
                        view.showError(message);
                    }
                });
    }

    @Override
    public void getFutureWeather(String city) {
        RetrofitHelper.getWeatherRetrofitHelper().create(API.class)
                .getFutureWeather(city)
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<JsonObject, Publisher<FutureWeatherBean>>() {
                    @Override
                    public Publisher<FutureWeatherBean> apply(@NonNull JsonObject jsonObject) throws Exception {
                        FutureWeatherBean bean = (FutureWeatherBean) GsonHelper.parseJson(jsonObject, FutureWeatherBean.class);
                        return Flowable.just(bean);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpSubscriber<FutureWeatherBean>() {
                    @Override
                    public void _onNext(FutureWeatherBean futureWeatherBean) {
                        view.updateFutureWeather(futureWeatherBean);
                    }

                    @Override
                    public void _onError(String message) {
                        view.showError(message);
                    }
                });
    }
}
