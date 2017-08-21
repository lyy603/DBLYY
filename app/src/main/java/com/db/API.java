package com.db;

import com.google.gson.JsonObject;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 作者：lyy on 2017/5/11 14:53
 */

public interface API {
    int LIMIT = 20;

    //热映列表
    @GET("in_theaters")
    Flowable<JsonObject> getShowingList(
            @Query("apikey") String apikey,
            @Query("city") String city,
            @Query("start") String start,
            @Query("count") String count
    );

    @GET("subject/{movie_id}/comments")
    Flowable<JsonObject> getShortCommentList(
            @Path("movie_id") String movie_id,
            @Query("apikey") String apikey,
            @Query("start") String start,
            @Query("count") String count
    );

    @GET("subject/{movie_id}/reviews")
    Flowable<JsonObject> getReviewList(
            @Path("movie_id") String movie_id,
            @Query("apikey") String apikey,
            @Query("start") String start,
            @Query("count") String count
    );

    @GET("subject/{movie_id}")
    Flowable<JsonObject> getMovieDetail(
            @Path("movie_id") String movie_id,
            @Query("apikey") String apikey,
            @Query("city") String city
    );

    //天气接口

    @GET("future24h/")
    Flowable<JsonObject> getDailyWeather(
            @Query("city") String city,
            @Query("language") String language,
            @Query("key") String key
    );

    @GET("all/")
    Flowable<JsonObject> getFutureWeather(
            @Query("city") String city
    );
}
