package com.anglll.aflow.data.source;

import com.anglll.aflow.data.model.Feed;
import com.anglll.aflow.data.model.PageModel;
import com.anglll.aflow.data.retrofit.RetrofitAPI;
import com.anglll.aflow.data.retrofit.api.RemoteService;

import io.reactivex.Flowable;

/**
 * Created by yuan on 2017/11/25 0025.
 */

public class AppRepository implements AppContract {
    private static volatile AppRepository instance;
    private static volatile RemoteService remoteService;

    public static AppRepository getInstance() {
        synchronized (AppRepository.class) {
            if (instance == null)
                instance = new AppRepository();
            return instance;
        }
    }

    private AppRepository() {
        remoteService = RetrofitAPI.getInstance().getRemoteService();
    }

    @Override
    public Flowable<PageModel<Feed>> getFeedList(int page, int size) {
        return remoteService.getFeedList(page, size);
    }

    @Override
    public Flowable<PageModel<Feed>> getActivityList(int page, int size) {
        return remoteService.getActivityList(page, size);
    }

    @Override
    public Flowable<Feed> getFeedInfo(String feedId) {
        return remoteService.getFeedInfo(feedId);
    }
}
