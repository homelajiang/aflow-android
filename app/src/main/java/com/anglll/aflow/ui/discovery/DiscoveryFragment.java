package com.anglll.aflow.ui.discovery;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anglll.aflow.R;
import com.anglll.aflow.base.BaseFragment;
import com.anglll.aflow.data.model.Feed;
import com.anglll.aflow.data.model.PageModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yuan on 2017/11/30.
 */

public class DiscoveryFragment extends BaseFragment implements DiscoveryContract.View {
    private static final String TAG = DiscoveryFragment.class.getSimpleName();
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private DiscoveryController controller = new DiscoveryController(null, null);
    private DiscoveryContract.Presenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fregment_discovery, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    protected void lazyInit() {
//        presenter.getActivity();
        presenter.getFeedList();
    }

    private void initView() {
        controller.setSpanCount(2);
        final GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        manager.setSpanSizeLookup(controller.getSpanSizeLookup());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(new DiscoveryDecoration(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(controller.getAdapter());
        new DiscoveryPresenter(this);
        controller.getAdapter().registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                if(positionStart == 0) {
                    manager.scrollToPosition(0);
                }
            }
        });
    }

    @Override
    public void setPresenter(DiscoveryContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getDiscovery(PageModel<Feed> feedPage) {
        // TODO: 2018/4/26 0026
        controller.addFeed(feedPage.getList());
    }

    @Override
    public void getDiscoveryFail(Throwable throwable) {
        TT(R.string.get_discovery_fail);
    }

    @Override
    public void getActivity(PageModel<Feed> activityPage) {
        controller.addActivity(activityPage.getList());
    }

    @Override
    public void getActivityFail(Throwable throwable) {
    }

    public static DiscoveryFragment newInstance() {
        return new DiscoveryFragment();
    }
}
