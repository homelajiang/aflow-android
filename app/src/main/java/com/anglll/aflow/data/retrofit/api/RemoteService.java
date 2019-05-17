package com.anglll.aflow.data.retrofit.api;

import com.anglll.aflow.data.model.Feed;
import com.anglll.aflow.data.model.PageModel;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by yuan on 2017/11/25 0025.
 */

public interface RemoteService {
    //获取feed列表
    @GET("api/v1/feed")
    Flowable<PageModel<Feed>> getFeedList(
            @Query("page") int page, @Query("size") int size);

    //获取activity列表
    @GET("api/v1/activity")
    Flowable<PageModel<Feed>> getActivityList(
            @Query("page") int page, @Query("size") int size);

    //获取feed详情
    @GET("api/v1/feed/{feedId}")
    Flowable<Feed> getFeedInfo(@Path("feedId") String feedId);

    //登录
    //注册


}
