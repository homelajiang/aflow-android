package com.anglll.aflow.ui.epoxy.models;

import android.net.Uri;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyModelClass;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.anglll.aflow.R;
import com.anglll.aflow.base.BaseEpoxyHolder;
import com.anglll.aflow.data.model.Feed;
import com.anglll.aflow.utils.FuzzyDateFormatter;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;

/**
 * Created by yuan on 2017/12/6 0006.
 */
@EpoxyModelClass(layout = R.layout.model_video)
public abstract class VideoModel extends EpoxyModelWithHolder<VideoModel.ViewModel> {
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

    public static class ViewModel extends BaseEpoxyHolder<Feed> {
        @BindView(R.id.cover)
        SimpleDraweeView mCover;
        @BindView(R.id.layout_cover)
        FrameLayout mLayoutCover;
        @BindView(R.id.avatar)
        SimpleDraweeView mAvatar;
        @BindView(R.id.title)
        TextView mTitle;
        @BindView(R.id.owner_name)
        TextView mOwnerName;
        @BindView(R.id.publish_time)
        TextView mPublishTime;
        @BindView(R.id.view_count)
        TextView mViewCount;

        @Override
        protected void bindData(Feed data) {
            mAvatar.setImageURI(Uri.parse(data.getOwner().getAvatar()));
            mAvatar.setImageURI(Uri.parse(data.getCover()));
            mOwnerName.setText(data.getOwner().getName());
            mTitle.setText(data.getTitle());
            mPublishTime.setText(FuzzyDateFormatter.getTimeAgo(context, data.getReleaseDate()));
            mViewCount.setText(String.valueOf(data.getVisit().getViews() + context.getString(R.string.views)));
        }
    }
}
