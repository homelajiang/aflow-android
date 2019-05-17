package com.anglll.aflow.ui.music.playlist.detail;

import android.support.v7.widget.RecyclerView;

import com.airbnb.epoxy.EpoxyController;
import com.anglll.aflow.ui.epoxy.models.MusicPlayListItemModel_;
import com.anglll.aflow.ui.imp.PlayListDetailCallback;

import org.lineageos.eleven.model.Playlist;
import org.lineageos.eleven.model.Song;

import java.util.Collections;
import java.util.List;

public class DetailController extends EpoxyController {
    private PlayListDetailCallback callback;
    private RecyclerView.RecycledViewPool recycledViewPool;

    public Playlist playlist;
    public List<Song> songList = Collections.emptyList();
    public Song firstSong = null;

    DetailController(PlayListDetailCallback callback, RecyclerView.RecycledViewPool recycledViewPool) {
        this.callback = callback;
        this.recycledViewPool = recycledViewPool;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
        requestModelBuild();
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
        if (!songList.isEmpty())
            firstSong = songList.get(0);
        requestModelBuild();
    }

    public void updateCurrentSongPosition() {
        requestModelBuild();
    }

    @Override
    protected void buildModels() {

        // TODO: 2018/7/17 0017 播放位置计算不正确，不能使用当前列表的position
//        int playingIndex = MusicUtils.getQueuePosition();
        for (int i = 0; i < songList.size(); i++) {
            add(new MusicPlayListItemModel_()
                    .callback(callback)
                    .id(i)
                    .index(i)
//                    .playing(playingIndex == i)
                    .song(songList.get(i)));
        }
    }

}
