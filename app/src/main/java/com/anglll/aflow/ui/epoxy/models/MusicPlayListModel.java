package com.anglll.aflow.ui.epoxy.models;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyModelClass;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.anglll.aflow.R;
import com.anglll.aflow.base.BaseEpoxyHolder;
import com.anglll.aflow.ui.music.playlist.PlayListController;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.postprocessors.IterativeBoxBlurPostProcessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import org.lineageos.eleven.model.Playlist;
import org.lineageos.eleven.utils.MusicUtils;

import butterknife.BindView;
import butterknife.OnClick;

@EpoxyModelClass(layout = R.layout.model_music_playlist)
public abstract class MusicPlayListModel extends EpoxyModelWithHolder<MusicPlayListModel.ViewHolder> {
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
        return totalSpanCount;
    }

    class ViewHolder extends BaseEpoxyHolder<Playlist> {
        @BindView(R.id.bg)
        SimpleDraweeView mBg;
        @BindView(R.id.cover)
        SimpleDraweeView mCover;
        @BindView(R.id.title)
        TextView mTitle;
        @BindView(R.id.summary)
        TextView mSummary;

        @OnClick(R.id.card)
        void onItemClick() {
            if (playlist != null && callback != null)
                callback.onPlayListClick(playlist);
        }

        @OnClick(R.id.cover_layout)
        void showPlaylistMenus() {
            if (playlist != null && callback != null)
                callback.showPlayListMenu(playlist);
        }

        @Override
        protected void bindData(Playlist playlist) {
            mTitle.setText(playlist.mPlaylistName);
            String temp = context.getString(R.string.playlist_song_count);
            mSummary.setText(String.format(temp, playlist.mSongCount));
            if (playlist.coverSong != null) {
                mCover.setImageURI(MusicUtils.getAlbumUri(playlist.coverSong.mAlbumId));

                try {
                    Uri uri = MusicUtils.getAlbumUri(playlist.coverSong.mAlbumId);
                    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                            .setPostprocessor(new IterativeBoxBlurPostProcessor(3, 10))
                            .build();
                    AbstractDraweeController controller = Fresco.newDraweeControllerBuilder()
                            .setOldController(mBg.getController())
                            .setImageRequest(request)
                            .build();
                    mBg.setController(controller);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                // TODO: 2018/7/28 0028 null handle
            }

/*            BitmapWithColors bitmapWithColors =
                    new BitmapWithColors(bitmap, key.hashCode());*/
        }
    }
}
