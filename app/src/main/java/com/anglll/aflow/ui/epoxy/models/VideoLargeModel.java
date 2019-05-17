package com.anglll.aflow.ui.epoxy.models;

import android.net.Uri;
import android.text.Html;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.View;
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
 * Created by yuan on 2017/11/30.
 */
@EpoxyModelClass(layout = R.layout.model_video_large)
public abstract class VideoLargeModel extends EpoxyModelWithHolder<VideoLargeModel.ViewModel> {
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
        @BindView(R.id.avatar)
        SimpleDraweeView mAvatar;
        @BindView(R.id.owner_name)
        TextView mOwnerName;
        @BindView(R.id.release_time)
        TextView mReleaseTime;
        @BindView(R.id.cover)
        SimpleDraweeView mCover;
        @BindView(R.id.title)
        TextView mTitle;
        @BindView(R.id.content)
        TextView mContent;

        @Override
        protected void bindData(Feed data) {
            mAvatar.setImageURI(Uri.parse(data.getOwner().getAvatar()));
            mCover.setImageURI(Uri.parse(data.getCover()));
            mOwnerName.setText(data.getOwner().getName());
            String content;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                content = Html.fromHtml(data.getDescription(), Html.FROM_HTML_MODE_LEGACY).toString();
            } else {
                content = Html.fromHtml(data.getDescription()).toString();
            }

            if (data.getTitle().equals(content)) {
                mTitle.setVisibility(View.GONE);
            } else {
                mTitle.getPaint().setFakeBoldText(true);
                mTitle.setVisibility(View.VISIBLE);
                mTitle.setText(data.getTitle());
            }
            mContent.setText(content);

            mReleaseTime.setText(FuzzyDateFormatter.getTimeAgo(context, data.getReleaseDate()));
        }

        public int dp2px(float dpVal) {
            return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources().getDisplayMetrics()));
        }
    }

}
