package com.anglll.aflow.utils;

import android.app.Activity;
import android.content.Intent;

import com.anglll.aflow.ui.music.playlist.detail.DetailActivity;

import org.lineageos.eleven.model.Playlist;

public final class Router {
    public static final String PLAYLIST_ID = "playlist_id";
    public static final String PLAYLIST_NAME = "playlist_name";
    public static final String PLAYLIST_COUNT = "playlist_count";

    public static void openPlayList(final Activity context, Playlist playlist) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(PLAYLIST_ID, playlist.mPlaylistId);
        intent.putExtra(PLAYLIST_NAME, playlist.mPlaylistName);
        intent.putExtra(PLAYLIST_COUNT, playlist.mSongCount);
        context.startActivity(intent);
    }

}
