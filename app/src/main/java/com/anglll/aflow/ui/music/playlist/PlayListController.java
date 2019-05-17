package com.anglll.aflow.ui.music.playlist;

import android.support.v7.widget.RecyclerView;

import com.airbnb.epoxy.AutoModel;
import com.airbnb.epoxy.TypedEpoxyController;
import com.anglll.aflow.ui.epoxy.models.FeaturesModel_;
import com.anglll.aflow.ui.epoxy.models.MusicPlayListLiteModel_;
import com.anglll.aflow.ui.epoxy.models.MusicPlayListModel_;

import org.lineageos.eleven.model.Playlist;

import java.util.List;

public class PlayListController extends TypedEpoxyController<List<Playlist>> {
    private MusicPlayListCallback callback;
    private RecyclerView.RecycledViewPool recyclerViewPool;

    PlayListController(MusicPlayListCallback callback, RecyclerView.RecycledViewPool recycledViewPool) {
        this.callback = callback;
        this.recyclerViewPool = recycledViewPool;
    }

    @Override
    protected void buildModels(List<Playlist> data) {
        if (data == null)
            return;

        for (Playlist playlist : data) {
            add(new MusicPlayListModel_()
                    .id(playlist.mPlaylistId)
                    .callback(callback)
                    .playlist(playlist));
        }
    }

    @Override
    public int getSpanCount() {
        return super.getSpanCount();
    }

    public interface MusicPlayListCallback {
        void onPlayListClick(Playlist playlist);

        void showPlayListMenu(Playlist playlist);
    }
}
