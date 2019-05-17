package com.anglll.aflow.ui.epoxy.models;

import android.net.Uri;
import android.support.v7.widget.CardView;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyModelClass;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.anglll.aflow.R;
import com.anglll.aflow.base.BaseEpoxyHolder;
import com.anglll.aflow.data.model.Feed;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;

/**
 * Created by yuan on 2017/12/6 0006.
 */
@EpoxyModelClass(layout = R.layout.model_media)
public abstract class MediaModel extends EpoxyModelWithHolder<MediaModel.ViewModel> {
    @EpoxyAttribute
    Feed feed;

    @Override
    public void bind(ViewModel holder) {
        holder.bindData(feed);
    }

    @Override
    public int getSpanSize(int totalSpanCount, int position, int itemCount) {
        return totalSpanCount;
    }

    static class ViewModel extends BaseEpoxyHolder<Feed> {
        @BindView(R.id.card_bg)
        SimpleDraweeView mCardBg;
        @BindView(R.id.title)
        TextView mTitle;
        @BindView(R.id.des)
        TextView mDes;
        @BindView(R.id.my_image_view)
        CardView mMyImageView;

        @Override
        protected void bindData(Feed feed) {
            mCardBg.setImageURI(Uri.parse(feed.getCover()));
            mTitle.setText(feed.getTitle());
            mDes.setText(feed.getDescription());
        }
    }
}
