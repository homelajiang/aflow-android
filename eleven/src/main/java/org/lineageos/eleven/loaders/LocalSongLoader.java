package org.lineageos.eleven.loaders;

import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import org.lineageos.eleven.model.Song;
import org.lineageos.eleven.sectionadapter.SectionCreator;
import org.lineageos.eleven.utils.Lists;

import java.util.ArrayList;
import java.util.List;

public class LocalSongLoader extends SectionCreator.SimpleListLoader<Song> {
    private final ArrayList<Song> mSongList = Lists.newArrayList();

    //    private final
    public LocalSongLoader(Context context) {
        super(context);
    }

    @Nullable
    @Override
    public List<Song> loadInBackground() {
        Cursor cursor = makeLocalSongCursor(getContext());
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Copy the song Id
                final long id = cursor.getLong(0);

                // Copy the song name
                final String songName = cursor.getString(1);

                // Copy the artist name
                final String artist = cursor.getString(2);

                // Copy the album id
                final long albumId = cursor.getLong(3);

                // Copy the album name
                final String album = cursor.getString(4);

                // Copy the duration
                final long duration = cursor.getLong(5);

                // Convert the duration into seconds
                final int durationInSecs = (int) duration / 1000;

                // Grab the Song Year
                final int year = cursor.getInt(6);

                // Create a new song
                final Song song = new Song(id, songName, artist, album, albumId, durationInSecs, year);

                // Add everything up
                mSongList.add(song);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
        return mSongList;
    }

    public static final Cursor makeLocalSongCursor(Context context) {
        String selection = (MediaStore.Audio.AudioColumns.IS_MUSIC + "=1") +
                " AND " + MediaStore.Audio.AudioColumns.TITLE + " !=''";
        return context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{
                        /* 0 */
                        BaseColumns._ID,
                        /* 1 */
                        MediaStore.Audio.AudioColumns.TITLE,
                        /* 2 */
                        MediaStore.Audio.AudioColumns.ARTIST,
                        /* 3 */
                        MediaStore.Audio.AudioColumns.ALBUM_ID,
                        /* 4 */
                        MediaStore.Audio.AudioColumns.ALBUM,
                        /* 5 */
                        MediaStore.Audio.AudioColumns.DURATION,
                        /* 6 */
                        MediaStore.Audio.AudioColumns.YEAR,
                }, selection, null, MediaStore.Audio.Media.DATE_ADDED + " DESC");
    }
}
