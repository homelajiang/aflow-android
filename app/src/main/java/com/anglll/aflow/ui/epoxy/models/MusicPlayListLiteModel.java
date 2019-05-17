package com.anglll.aflow.ui.epoxy.models;

import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyModelClass;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.anglll.aflow.R;
import com.anglll.aflow.base.BaseEpoxyHolder;
import com.anglll.aflow.ui.music.playlist.PlayListController;

import org.lineageos.eleven.model.Playlist;

import butterknife.BindView;
import butterknife.OnClick;

@EpoxyModelClass(layout = R.layout.model_music_playlist_lite)
public abstract class MusicPlayListLiteModel extends EpoxyModelWithHolder<MusicPlayListLiteModel.ViewHolder> {
    @EpoxyAttribute
    Playlist playlist;
    @EpoxyAttribute
    PlayListController.MusicPlayListCallback callback;

    @Override
    public void bind(@NonNull ViewHolder holder) {
        holder.bindData(playlist);
    }

    @Override
    public int getSpanSize(int totalSpanCount, int position, int itemCount) {
        return super.getSpanSize(totalSpanCount, position, itemCount);
    }

    class ViewHolder extends BaseEpoxyHolder<Playlist> {
        @BindView(R.id.icon)
        ImageView mIcon;
        @BindView(R.id.title)
        TextView mTitle;
        @BindView(R.id.des)
        TextView mDes;

        @OnClick(R.id.card)
        void onItemClick() {
            if (playlist != null && callback != null)
                callback.onPlayListClick(playlist);
        }

        @Override
        protected void bindData(Playlist data) {
            mTitle.setText(data.mPlaylistName);
        }
    }
}
