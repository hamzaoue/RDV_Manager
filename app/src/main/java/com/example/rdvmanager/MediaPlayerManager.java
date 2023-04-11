package com.example.rdvmanager;

import android.content.Context;
import android.media.MediaPlayer;
/********************/
public class MediaPlayerManager
{
    private static MediaPlayerManager sInstance;
    private final MediaPlayer aMediaPlayer;
    /********************/
    private MediaPlayerManager(Context context)
    {
        this.aMediaPlayer = MediaPlayer.create(context,R.raw.kikis_delivery_service);
    }
    /********************/
    public static synchronized MediaPlayerManager getInstance(Context context)
    {
        if (sInstance == null)
            sInstance = new MediaPlayerManager(context);
        return sInstance;
    }
    /********************/
    public MediaPlayer getMediaPlayer() { return this.aMediaPlayer;}
}