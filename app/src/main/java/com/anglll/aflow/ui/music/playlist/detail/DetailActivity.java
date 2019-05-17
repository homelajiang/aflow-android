package com.anglll.aflow.ui.music.playlist.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.anglll.aflow.R;
import com.anglll.aflow.base.BaseMusicActivity;
import com.anglll.aflow.ui.imp.PlayListDetailCallback;
import com.anglll.aflow.utils.Router;
import com.facebook.drawee.view.SimpleDraweeView;

import org.lineageos.eleven.Config;
import org.lineageos.eleven.loaders.LastAddedLoader;
import org.lineageos.eleven.loaders.LocalSongLoader;
import org.lineageos.eleven.loaders.PlaylistSongLoader;
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

public class DetailActivity extends BaseMusicActivity implements
        PlayListDetailCallback {
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.cover)
    SimpleDraweeView mCover;
    @BindView(R.id.toolBar)
    Toolbar mToolBar;

    private DetailController controller = new DetailController(this, null);
    private Playlist playlist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_playlist_detail);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        if (getIntent() == null) {
            finish();
            return;
        }
        playlist = new Playlist(
                getIntent().getLongExtra(Router.PLAYLIST_ID, 0),
                getIntent().getStringExtra(Router.PLAYLIST_NAME),
                getIntent().getIntExtra(Router.PLAYLIST_COUNT, 0)
        );
        if (playlist.mPlaylistId == 0) {
            finish();
            return;
        }
        controller.setPlaylist(playlist);
        Config.SmartPlaylistType type = Config.SmartPlaylistType.getTypeById(playlist.mPlaylistId);
        if (type != null) {
            initLoader(0, null, new DefaultPlayListCallback(type));
        } else {
            initLoader(0, null, new UserPlayListCallback());
        }
    }

    private void initView() {
        setSupportActionBar(mToolBar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(playlist.mPlaylistName);
        }
        controller.setSpanCount(1);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 1);
        manager.setSpanSizeLookup(controller.getSpanSizeLookup());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(controller.getAdapter());
    }

    @OnClick(R.id.floatingActionButton)
    void playAll() {
        playPlaylist(playlist, 0);
    }

    private void updateController(List<Song> songs) {
        controller.setSongList(songs);
        if (songs.size() > 0)
            mCover.setImageURI(MusicUtils.getAlbumUri(songs.get(0).mAlbumId));

    }

    @Override
    public void onMetaChanged() {
        super.onMetaChanged();
        controller.updateCurrentSongPosition();
    }

    @Override
    public void onPlayPlayList(int index) {
        playPlaylist(playlist, index);
    }

    @Override
    public void onShowMenu(int index) {

    }

    class UserPlayListCallback implements LoaderManager.LoaderCallbacks<List<Song>> {

        @NonNull
        @Override
        public Loader<List<Song>> onCreateLoader(int id, @Nullable Bundle args) {
            return new PlaylistSongLoader(getContext(), playlist.mPlaylistId);
        }

        @Override
        public void onLoadFinished(@NonNull Loader<List<Song>> loader, List<Song> data) {
            updateController(data);
        }

        @Override
        public void onLoaderReset(@NonNull Loader<List<Song>> loader) {

        }
    }

    class DefaultPlayListCallback implements LoaderManager.LoaderCallbacks<SectionListContainer<Song>> {

        private final Config.SmartPlaylistType type;

        public DefaultPlayListCallback(Config.SmartPlaylistType type) {
            this.type = type;
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
            updateController(data.mListResults);
        }

        @Override
        public void onLoaderReset(@NonNull Loader<SectionListContainer<Song>> loader) {

        }
    }
}
