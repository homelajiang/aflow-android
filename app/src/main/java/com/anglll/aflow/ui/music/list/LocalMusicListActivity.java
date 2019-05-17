package com.anglll.aflow.ui.music.list;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.anglll.aflow.R;
import com.anglll.aflow.base.BaseMusicActivity;
import com.anglll.aflow.ui.dialog.MenuDialog;
import com.anglll.aflow.ui.imp.PlayListDetailCallback;
import com.anglll.aflow.widget.menu.ActionMenu;
import com.facebook.drawee.view.SimpleDraweeView;

import org.lineageos.eleven.Config;
import org.lineageos.eleven.loaders.LastAddedLoader;
import org.lineageos.eleven.loaders.LocalSongLoader;
import org.lineageos.eleven.loaders.SmartPlaylistInfoLoader;
import org.lineageos.eleven.loaders.TopTracksLoader;
import org.lineageos.eleven.model.Playlist;
import org.lineageos.eleven.model.Song;
import org.lineageos.eleven.sectionadapter.SectionCreator;
import org.lineageos.eleven.sectionadapter.SectionListContainer;
import org.lineageos.eleven.utils.MusicUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LocalMusicListActivity extends BaseMusicActivity implements PlayListDetailCallback {

    @BindView(R.id.cover)
    SimpleDraweeView mCover;
    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBar;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.floatingActionButton)
    FloatingActionButton mFloatingActionButton;
    private LocalMusicListController controller;
    private Playlist playlist;
    private List<Song> songList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_playlist_detail);
        ButterKnife.bind(this);
        init();

    }

    private void init() {
        setSupportActionBar(mToolBar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setTitle(playlist.mPlaylistName);
        }
        controller = new LocalMusicListController(this);
        controller.setSpanCount(1);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 1);
        manager.setSpanSizeLookup(controller.getSpanSizeLookup());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(controller.getAdapter());

        getContainingLoaderManager().initLoader(0, null, new SmartPlaylistInfoCallback());
        getContainingLoaderManager().initLoader(1, null, new DefaultPlayListCallback());

    }

    @OnClick(R.id.floatingActionButton)
    void playAll() {
        playPlaylist(playlist, 0);
    }

    @Override
    public void onPlayPlayList(int index) {
        playPlaylist(playlist, index);
    }

    @Override
    public void onShowMenu(final int index) {
        ActionMenu actionMenu = new ActionMenu(getContext());
        getMenuInflater().inflate(R.menu.song_action, actionMenu);
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
                .show(getSupportFragmentManager(), "song_dialog");

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
                MusicUtils.addToQueue(this, getSongIdList(index), getSourceId(), getSourceType());
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

    private long[] getSongIdList(int index) {
        return new long[]{songList.get(index).mSongId};
    }

    protected long getSourceId() {
        return playlist.mPlaylistId;
    }

    protected Config.IdType getSourceType() {
        return Config.IdType.Playlist;
    }


    @OnClick(R.id.floatingActionButton)
    public void onViewClicked() {
    }

    @Override
    public void onMetaChanged() {
        super.onMetaChanged();
        controller.updateCurrentSongPosition();
    }

    public void updatePlaylistMeta(Playlist playlist) {
        this.playlist = playlist;
        mCover.setImageURI(MusicUtils.getAlbumUri(playlist.coverSong.mAlbumId));
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(this.playlist.mPlaylistName);
    }


    class SmartPlaylistInfoCallback implements LoaderManager.LoaderCallbacks<Playlist> {

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

    class DefaultPlayListCallback implements LoaderManager.LoaderCallbacks<SectionListContainer<Song>> {

        private final Config.SmartPlaylistType type;

        public DefaultPlayListCallback() {
            this.type = Config.SmartPlaylistType.LocalSong;
        }

        @NonNull
        @Override
        public Loader<SectionListContainer<Song>> onCreateLoader(int id, @Nullable Bundle args) {
            switch (type) {
                case LocalSong: {
                    LocalSongLoader loader = new LocalSongLoader(getContext());
                    return new SectionCreator<Song>(getContext(), loader, null);
                }
                case LastAdded: {
                    LastAddedLoader loader = new LastAddedLoader(getContext());
                    return new SectionCreator<Song>(getContext(), loader, null);
                }
                case RecentlyPlayed: {
                    TopTracksLoader loader = new TopTracksLoader(getContext(),
                            TopTracksLoader.QueryType.RecentSongs);
                    return new SectionCreator<Song>(getContext(), loader, null);
                }
                case TopTracks: {
                    TopTracksLoader loader = new TopTracksLoader(getContext(),
                            TopTracksLoader.QueryType.TopTracks);
                    return new SectionCreator<Song>(getContext(), loader, null);
                }
                default:
                    return null;
            }
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

}
