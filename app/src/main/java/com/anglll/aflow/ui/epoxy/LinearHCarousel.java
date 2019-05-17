package com.anglll.aflow.ui.epoxy;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.airbnb.epoxy.Carousel;
import com.airbnb.epoxy.ModelView;
import com.anglll.aflow.ui.discovery.DiscoveryDecoration;

/**
 * Created by yuan on 2017/12/14 0014.
 */
@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
public class LinearHCarousel extends Carousel {
    public LinearHCarousel(Context context) {
        super(context);
        addItemDecoration(new LinearHDecoration(getContext()));
    }

    @Override
    protected LayoutManager createLayoutManager() {
        return new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
    }

}
