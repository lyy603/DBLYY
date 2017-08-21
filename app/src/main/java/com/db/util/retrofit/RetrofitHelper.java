package com.db.util.retrofit;

import com.blankj.utilcode.util.NetworkUtils;
import com.db.App;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 作者：lyy on 2017/5/11 14:50
 */

public class RetrofitHelper {

    //公共域名
    public static final String BASE_URL = "http://api.douban.com/v2/movie/";
    public static final String BASE_URL_WEATHER = "http://tj.nineton.cn/Heart/index/";

    private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTO = chain -> {

        Request request = chain.request();
        if (!NetworkUtils.getWifiEnabled() && !NetworkUtils.is4G()) {
            CacheControl cacheControl = new CacheControl.Builder()
                    .maxStale(30, TimeUnit.SECONDS)
                    .build();
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }

        Response response = chain.proceed(request);

        if (NetworkUtils.getWifiEnabled() || NetworkUtils.is4G()) {
            /**
             * If you have problems in testing on which side is problem (server or app).
             * You can use such feauture to set headers received from server.
             */
            int maxAge = 60 * 60; // 有网络时,设置缓存超时时间1个小时
            response = response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, max-age=" + maxAge)//设置缓存超时时间
                    .build();
        } else {
            int maxStale = 24 * 60 * 60;//无网络时，设置超时为4周
            response = response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }
        return response;
    };

    private static Retrofit retrofit = null;

    private static Retrofit weather = null;

    public static Retrofit getRetrofitHelper() {
        if (retrofit == null) {
            try {
                synchronized (RetrofitHelper.class) {
                    if (retrofit == null) {
                        File httpCacheDirectory = new File(App.getContext().getCacheDir(), "mainCache");
                        Cache cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);//缓存10MB
                        OkHttpClient.Builder httpBuidler = new OkHttpClient().newBuilder();
                        httpBuidler.cache(cache)
                                .connectTimeout(10, TimeUnit.SECONDS)//连接超时限制5秒
                                .writeTimeout(10, TimeUnit.SECONDS)
                                .readTimeout(10, TimeUnit.SECONDS)
                                //添加拦截器
//                                .addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)//离线缓存
                                .addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTO);

                        retrofit = new Retrofit.Builder()
                                .client(httpBuidler.build())
                                .baseUrl(BASE_URL)
                                .addConverterFactory(GsonConverterFactory.create())
                                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                                .build();

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return retrofit;
    }

    public static Retrofit getWeatherRetrofitHelper() {
        if (weather == null) {
            try {
                synchronized (RetrofitHelper.class) {
                    if (weather == null) {
                        File httpCacheDirectory = new File(App.getContext().getCacheDir(), "mainCache");
                        Cache cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);//缓存10MB
                        OkHttpClient.Builder httpBuidler = new OkHttpClient().newBuilder();
                        httpBuidler.cache(cache)
                                .connectTimeout(10, TimeUnit.SECONDS)//连接超时限制5秒
                                .writeTimeout(10, TimeUnit.SECONDS)
                                .readTimeout(10, TimeUnit.SECONDS)
                                //添加拦截器
//                                .addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)//离线缓存
                                .addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTO);

                        weather = new Retrofit.Builder()
                                .client(httpBuidler.build())
                                .baseUrl(BASE_URL_WEATHER)
                                .addConverterFactory(GsonConverterFactory.create())
                                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                                .build();

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return weather;
    }
}
