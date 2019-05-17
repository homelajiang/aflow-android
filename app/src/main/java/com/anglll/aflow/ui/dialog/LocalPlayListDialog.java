package com.anglll.aflow.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyController;
import com.anglll.aflow.R;
import com.anglll.aflow.base.MusicDialogFragment;
import com.anglll.aflow.ui.epoxy.models.MusicPlayListItemModel_;
import com.anglll.aflow.ui.imp.PlayListDetailCallback;
import com.anglll.aflow.widget.menu.ActionMenu;

import org.lineageos.eleven.Config;
import org.lineageos.eleven.loaders.LocalSongLoader;
import org.lineageos.eleven.loaders.SmartPlaylistInfoLoader;
import org.lineageos.eleven.model.Playlist;
import org.lineageos.eleven.model.Song;
import org.lineageos.eleven.sectionadapter.SectionCreator;
import org.lineageos.eleven.sectionadapter.SectionListContainer;
import org.lineageos.eleven.utils.MusicUtils;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class LocalPlayListDialog extends MusicDialogFragment implements PlayListDetailCallback {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.title_left)
    AppCompatImageButton mTitleLeft;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.sub_title)
    TextView mSubTitle;
    @BindView(R.id.title_right)
    AppCompatImageButton mTitleRight;
    private LocalPlaylistController controller;
    private Playlist playlist;
    private List<Song> songList;

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_local_musiclist;
    }

    public void onViewCreated() {
        super.onViewCreated();
        init();
    }

    void init() {
        mTitleLeft.setImageResource(R.drawable.ic_close_black_24dp);
        mTitle.setText(R.string.playlist_local_music);

        controller = new LocalPlaylistController(this);
        controller.setSpanCount(1);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 1);
        manager.setSpanSizeLookup(controller.getSpanSizeLookup());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(controller.getAdapter());

        getActivity().getSupportLoaderManager()
                .initLoader(0, null, new LocalPlaylistInfoCallback());
        getActivity().getSupportLoaderManager()
                .initLoader(1, null, new LocalPlayListCallback());
    }

    @OnClick(R.id.title_left)
    void back() {
        dismiss();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), R.style.DialogFullscreen);
    }

    @Override
    public void onPlayPlayList(int index) {
        playPlaylist(playlist, index);
    }

    public void playPlaylist(Playlist playlist, int index) {
        MusicUtils.playAll(getContext(), getIdList(playlist), index,
                playlist.mPlaylistId, Config.IdType.Playlist, false);
    }

    @Override
    public void onShowMenu(final int index) {
        ActionMenu actionMenu = new ActionMenu(getContext());
        getActivity().getMenuInflater().inflate(R.menu.song_action, actionMenu);
        actionMenu.removeItem(R.id.action_add_to_playlist);
        actionMenu.removeItem(R.id.action_remove_from_playlist);
        new MenuDialog()
                .setMenu(actionMenu)
                .setTitle(String.format(getString(R.string.music_name_label), songList.get(index).mSongName))
                .setOnMenuSelectedCallback(new MenuDialog.MenuClickCallback() {
                    @Override
                    public void onMenuItemClick(MenuItem menuItem) {
                        menuSelected(index, menuItem);
                    }
                })
                .show(getActivity().getSupportFragmentManager(), "song_dialog");
    }

    private void menuSelected(final int index, MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_play:
                onPlayPlayList(index);
                break;
            case R.id.action_play_next:
                MusicUtils.playNext(getSongIdList(index),
                        getSourceId(), getSourceType());
                break;
            case R.id.action_add_to_track_list:
                MusicUtils.addToQueue(getContext(), getSongIdList(index), getSourceId(), getSourceType());
                break;
            case R.id.action_delete:
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.warning)
                        .setMessage(R.string.ensure_delete_song_from_disk)
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton(R.string.ensure, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MusicUtils.deleteTracks(getContext(), getSongIdList(index));
                            }
                        })
                        .show();
                break;
        }
    }

    public long[] getIdList(Playlist playlist) {
        if (playlist.isSmartPlaylist()) {
            return MusicUtils.getSongListForSmartPlaylist(getContext(),
                    Config.SmartPlaylistType.getTypeById(playlist.mPlaylistId));
        } else {
            return MusicUtils.getSongListForPlaylist(getContext(),
                    playlist.mPlaylistId);
        }
    }

    private long[] getSongIdList(int index) {
        return new long[]{songList.get(index).mSongId};
    }

    protected long getSourceId() {
        return playlist.mPlaylistId;
    }

    protected Config.IdType getSourceType() {
        return Config.IdType.Playlist;
    }

    public void updatePlaylistMeta(Playlist playlist) {
        this.playlist = playlist;
//        mCover.setImageURI(MusicUtils.getAlbumUri(playlist.coverSong.mAlbumId));
    }

    @Override
    public void restartLoader() {

    }

    @Override
    public void onPlaylistChanged() {

    }

    @Override
    public void onMetaChanged() {
        controller.updateCurrentSongPosition();
    }

    @Override
    public void onUpdateController() {

    }


    class LocalPlaylistInfoCallback implements LoaderManager.LoaderCallbacks<Playlist> {

        @NonNull
        @Override
        public Loader<Playlist> onCreateLoader(int id, @Nullable Bundle args) {
            return new SmartPlaylistInfoLoader(getContext(), Config.SmartPlaylistType.LocalSong);
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Playlist> loader, Playlist data) {
            updatePlaylistMeta(data);
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Playlist> loader) {

        }
    }

    class LocalPlayListCallback implements LoaderManager.LoaderCallbacks<SectionListContainer<Song>> {

        @NonNull
        @Override
        public Loader<SectionListContainer<Song>> onCreateLoader(int id, @Nullable Bundle args) {
            LocalSongLoader loader = new LocalSongLoader(getContext());
            return new SectionCreator<Song>(getContext(), loader, null);
        }

        @Override
        public void onLoadFinished(@NonNull Loader<SectionListContainer<Song>> loader, SectionListContainer<Song> data) {
            songList = data.mListResults;
            controller.setSongList(data.mListResults);
        }

        @Override
        public void onLoaderReset(@NonNull Loader<SectionListContainer<Song>> loader) {

        }
    }

    public class LocalPlaylistController extends EpoxyController {
        private PlayListDetailCallback callback;

        public Playlist playlist;
        public List<Song> songList = Collections.emptyList();
        public Song firstSong = null;

        LocalPlaylistController(PlayListDetailCallback callback) {
            this.callback = callback;
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
}
