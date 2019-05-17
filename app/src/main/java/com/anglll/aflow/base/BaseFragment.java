package com.anglll.aflow.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by yuan on 2017/7/21 0021.
 */

public class BaseFragment extends Fragment {
    private CompositeDisposable mCompositeDisposable;
    private boolean isInitData = false;/*标志位 判断数据是否初始化*/
    private boolean isVisibleToUser = false;/*标志位 判断fragment是否可见*/
    private boolean isPrepareView = false;/*标志位 判断view已经加载完成 避免空指针操作*/

    private boolean isInitHide = false;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addSubscription(subscribeEvents());
        isPrepareView = true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //https://juejin.im/post/5a9398b56fb9a0634e6cb19a
        lazyInitData();

        //https://github.com/gpfduoduo/android-article/blob/master/Activity%20%2B%20%E5%A4%9AFrament%20%E4%BD%BF%E7%94%A8%E6%97%B6%E7%9A%84%E4%B8%80%E4%BA%9B%E5%9D%91.md
        //另外一种懒加载
        if (!isHidden()) {
            isInitData = true;
            lazyInit();
        }
    }

    public Context getContext() {
        return getActivity();
    }

    protected void addSubscription(Disposable subscription) {
        if (subscription == null)
            return;
        if (mCompositeDisposable == null)
            mCompositeDisposable = new CompositeDisposable();
        mCompositeDisposable.add(subscription);
    }

    protected Disposable subscribeEvents() {
        return null;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        this.isVisibleToUser = isVisibleToUser;
        lazyInitData();
    }

    private void lazyInitData() {
        if (!isInitData && isVisibleToUser && isPrepareView) {
            isInitData = true;
            initData();
        }
    }

    protected void initData() {
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && !isInitData) {
            isInitData = true;
            lazyInit();
        }
    }

    protected void lazyInit() {
    }

    protected void TT(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    protected void TT(@StringRes int id) {
        TT(getString(id));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mCompositeDisposable != null)
            mCompositeDisposable.clear();
    }

    public int dp2px(float dpVal) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, getResources().getDisplayMetrics()));
    }
}
