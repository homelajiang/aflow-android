package com.anglll.aflow.ui.discovery;

import com.anglll.aflow.data.model.Feed;
import com.anglll.aflow.data.model.PageModel;
import com.anglll.aflow.data.source.AppRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.disposables.ListCompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yuan on 2017/12/4 0004.
 */

public class DiscoveryPresenter implements DiscoveryContract.Presenter {

    private DiscoveryContract.View view;
    private final ListCompositeDisposable listCompositeDisposable;
    private final AppRepository appRepository;
    private int page = 1;
    private int FEED_SIZE = 10;
    private int ACTIVITY_SIZE = 8;

    public DiscoveryPresenter(DiscoveryContract.View view) {
        this.view = view;
        listCompositeDisposable = new ListCompositeDisposable();
        appRepository = AppRepository.getInstance();
        this.view.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        if (!listCompositeDisposable.isDisposed()) {//是否处理资源
            listCompositeDisposable.clear();
        }
        this.view = null;
    }

    @Override
    public void getFeedList() {
        Disposable disposable = appRepository.getFeedList(page, FEED_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PageModel<Feed>>() {
                    @Override
                    public void accept(PageModel<Feed> feedPageModel) throws Exception {
                        view.getDiscovery(feedPageModel);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        view.getDiscoveryFail(throwable);
                    }
                });
        listCompositeDisposable.add(disposable);
    }

    @Override
    public void getActivity() {
        Disposable disposable = appRepository.getActivityList(1, ACTIVITY_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PageModel<Feed>>() {
                    @Override
                    public void accept(PageModel<Feed> feedPageModel) throws Exception {
                        view.getActivity(feedPageModel);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        view.getActivityFail(throwable);
                    }
                });
        listCompositeDisposable.add(disposable);
    }
}
