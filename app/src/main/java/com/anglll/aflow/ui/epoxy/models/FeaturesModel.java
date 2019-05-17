package com.anglll.aflow.ui.epoxy.models;

import android.view.View;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyModelClass;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.anglll.aflow.R;
import com.anglll.aflow.base.BaseEpoxyHolder;
import com.anglll.aflow.data.model.Feed;

import java.util.List;

import butterknife.OnClick;

/**
 * Created by yuan on 2017/12/6 0006.
 */

@EpoxyModelClass(layout = R.layout.model_features)
public abstract class FeaturesModel extends EpoxyModelWithHolder<FeaturesModel.ViewModel> {

    @EpoxyAttribute
    List<Feed> multiMediaList;

    @Override
    public void bind(ViewModel holder) {
        holder.bindData(multiMediaList);
    }

    @Override
    public int getSpanSize(int totalSpanCount, int position, int itemCount) {
        return totalSpanCount;
    }

    static class ViewModel extends BaseEpoxyHolder<List<Feed>> {

        @OnClick({R.id.card1, R.id.card2, R.id.card3, R.id.card4})
        public void onViewClicked(View view) {
            switch (view.getId()) {
                case R.id.card1:
                    break;
                case R.id.card2:
                    break;
                case R.id.card3:
                    break;
                case R.id.card4:
                    break;
            }
        }

        @Override
        protected void bindData(List<Feed> data) {

        }
    }
}
