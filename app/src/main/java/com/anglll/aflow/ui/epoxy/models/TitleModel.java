package com.anglll.aflow.ui.epoxy.models;

import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyModelClass;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.anglll.aflow.R;
import com.anglll.aflow.base.BaseEpoxyHolder;
import com.anglll.aflow.data.model.Title;

import butterknife.BindView;

/**
 * Created by yuan on 2017/12/6 0006.
 */
@EpoxyModelClass(layout = R.layout.model_title)
public abstract class TitleModel extends EpoxyModelWithHolder<TitleModel.ViewModel> {
    @EpoxyAttribute
    Title title;

    @Override
    public void bind(ViewModel holder) {
        holder.bindData(title);
    }

    @Override
    public int getSpanSize(int totalSpanCount, int position, int itemCount) {
        return totalSpanCount;
    }

    static class ViewModel extends BaseEpoxyHolder<Title> {
        @BindView(R.id.title_icon)
        ImageView mTitleIcon;
        @BindView(R.id.title)
        TextView mTitle;

        @Override
        protected void bindData(Title data) {
            if(data == null)
                return;
            mTitle.setText(data.getContent());
            mTitleIcon.setImageResource(data.getImgRes());
        }
    }
}
