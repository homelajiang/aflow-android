package com.anglll.aflow.ui.epoxy.models;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyModelClass;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.anglll.aflow.R;
import com.anglll.aflow.base.BaseEpoxyHolder;
import com.anglll.aflow.ui.dialog.PlayQueueCallback;
import com.anglll.aflow.utils.TextHelper;

import org.lineageos.eleven.model.Song;

import butterknife.BindView;
import butterknife.OnClick;

@EpoxyModelClass(layout = R.layout.model_music_playlist_track)
public abstract class MusicQueueModel extends EpoxyModelWithHolder<MusicQueueModel.ViewHolder> {

    @EpoxyAttribute
    Song playingSong;
    @EpoxyAttribute
    int index;
    @EpoxyAttribute
    boolean playing;

    @EpoxyAttribute
    PlayQueueCallback callback;

    @Override
    public void bind(@NonNull ViewHolder holder) {
        holder.setData(playingSong, playing, index);
    }

    @Override
    public int getSpanSize(int totalSpanCount, int position, int itemCount) {
        return totalSpanCount;
    }

    class ViewHolder extends BaseEpoxyHolder<Song> {
        @BindView(R.id.title)
        TextView mTitle;
        @BindView(R.id.delete_form_queue)
        AppCompatImageButton mDeleteFormQueue;
        @BindView(R.id.item_layout)
        LinearLayout mItemLayout;
        private int index;

        @OnClick(R.id.delete_form_queue)
        void removeFromQueue() {
/*            if (callback != null && playingSong != null)
                callback.removeFromQueue(playingSong);*/
        }

        @OnClick(R.id.item_layout)
        void itemClick() {
            if (callback != null)
                callback.playIndex(index);
        }

        @Override
        protected void bindData(Song data) {
        }

        public void setData(Song song, boolean playing, int index) {
            this.index = index;
            if (playing) {
                TextHelper.SpannableText[] spannableTexts = {
                        new TextHelper.SpannableText(song.mSongName, ContextCompat.getColor(context, R.color.warm_pink), 15),
                        new TextHelper.SpannableText(" - ", ContextCompat.getColor(context, R.color.warm_pink), 11),
                        new TextHelper.SpannableText(song.mArtistName, ContextCompat.getColor(context, R.color.warm_pink), 11),
                };
                mTitle.setText(TextHelper.getStringBuilder(spannableTexts));
/*                    Drawable drawable= getResources().getDrawable(R.drawable.drawable);
                      // 这一步必须要做,否则不会显示.
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    myTextview.setCompoundDrawables(drawable,null,null,null);*/
            } else {
                TextHelper.SpannableText[] spannableTexts = {
                        new TextHelper.SpannableText(song.mSongName, ContextCompat.getColor(context, R.color.text_black), 15),
                        new TextHelper.SpannableText(" - ", ContextCompat.getColor(context, R.color.text_black), 11),
                        new TextHelper.SpannableText(song.mArtistName, ContextCompat.getColor(context, R.color.text_black), 11),
                };
                mTitle.setText(TextHelper.getStringBuilder(spannableTexts));
/*                    public void setCompoundDrawablesWithIntrinsicBounds (Drawable left,
                            Drawable top, Drawable right, Drawable bottom)*/
            }
        }
    }
}
