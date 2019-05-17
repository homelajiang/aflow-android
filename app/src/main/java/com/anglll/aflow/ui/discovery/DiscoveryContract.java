package com.anglll.aflow.ui.discovery;

import com.anglll.aflow.base.BasePresenter;
import com.anglll.aflow.base.BaseView;
import com.anglll.aflow.data.model.Feed;
import com.anglll.aflow.data.model.PageModel;

/**
 * Created by yuan on 2017/12/4 0004.
 */

public class DiscoveryContract {
    interface View extends BaseView<Presenter> {
        void getDiscovery(PageModel<Feed> feedPage);

        void getDiscoveryFail(Throwable throwable);

        void getActivity(PageModel<Feed> activityPage);

        void getActivityFail(Throwable throwable);
    }

    interface Presenter extends BasePresenter {
        void getFeedList();

        void getActivity();
    }
}
