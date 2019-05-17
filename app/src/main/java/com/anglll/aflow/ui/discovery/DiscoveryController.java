package com.anglll.aflow.ui.discovery;

import android.support.v7.widget.RecyclerView;

import com.airbnb.epoxy.EpoxyController;
import com.anglll.aflow.data.model.Feed;
import com.anglll.aflow.ui.epoxy.LinearLayoutTwoVGroup;
import com.anglll.aflow.ui.epoxy.models.VideoLargeModel_;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuan on 2017/12/2 0002.
 */

public class DiscoveryController extends EpoxyController {

    private DiscoverCallback callback;
    private RecyclerView.RecycledViewPool recyclerViewPool;
    private List<Feed> feeds = new ArrayList<>();
    private List<Feed> activities = new ArrayList<>();

    DiscoveryController(DiscoverCallback callback, RecyclerView.RecycledViewPool recycledViewPool) {
        this.callback = callback;
        this.recyclerViewPool = recycledViewPool;
    }

    @Override
    protected void buildModels() {
        if (!activities.isEmpty())
            add(new LinearLayoutTwoVGroup(activities));

        for (Feed feed : feeds) {
            add(new VideoLargeModel_()
                    .id(feed.getId())
                    .feed(feed));
        }
    }

    public void addFeed(List<Feed> feedList) {
        this.feeds.addAll(feedList);
        requestModelBuild();
    }

    public void addActivity(List<Feed> activityList) {
        this.activities.addAll(activityList);
        requestModelBuild();
    }

    public interface DiscoverCallback {

    }
}
