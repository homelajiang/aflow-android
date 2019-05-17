package com.anglll.aflow.data.source;

import com.anglll.aflow.data.model.Feed;
import com.anglll.aflow.data.model.PageModel;

import io.reactivex.Flowable;

/**
 * Created by yuan on 2017/11/25 0025.
 */

public interface AppContract {
    //获取feed列表
    Flowable<PageModel<Feed>> getFeedList(int page, int size);

    //获取推荐的多媒体信息
    Flowable<PageModel<Feed>> getActivityList(int page, int size);

    //获取feed详情
    Flowable<Feed> getFeedInfo(String feedId);
}

