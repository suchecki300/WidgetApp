package com.studia.projekt5;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Implementation of App Widget functionality.
 */
public class MyWidget extends AppWidgetProvider {

    private static MediaPlayer player;
    private static int numberOfImg;
    private static int numberOfMusic;
    private static final int[] imgArray = new int[]{R.drawable.djphoto1, R.drawable.djphoto2, R.drawable.djphoto3, R.drawable.djphoto4};
    private static final int[] musicArray = new int[]{R.raw.song1, R.raw.song2, R.raw.song3, R.raw.song4};

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.my_widget);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://www.pja.edu.pl"));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.button, pendingIntent);

/*
        Intent intent1 = new Intent(context, MyWidget.class);
        intent1.setAction("myaction1");
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, 0, intent1, 0);
        views.setOnClickPendingIntent(R.id.btn1, pendingIntent1);
*/


        Intent intentNextImg = new Intent(context, MyWidget.class);
        intentNextImg.setAction("next_image");
        PendingIntent pendingNext = PendingIntent.getBroadcast(context, 0, intentNextImg, 0);
        views.setOnClickPendingIntent(R.id.btnNext, pendingNext);

        Intent intentPreviousImage = new Intent(context, MyWidget.class);
        intentPreviousImage.setAction("previous_image");
        PendingIntent pendingPrevious = PendingIntent.getBroadcast(context, 0, intentPreviousImage, 0);
        views.setOnClickPendingIntent(R.id.btnPrev, pendingPrevious);

        musicIntent(context, views);

        numberOfImg = 0;
        numberOfMusic = 0;
        player = MediaPlayer.create(context, R.raw.song1);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        Log.i("widget-app","First widget enabled.");
    }

    @Override
    public void onDisabled(Context context) {
        Log.i("widget-app","Last widget disabled.");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if(intent.getAction().equals("myaction1")){
            Toast.makeText(context,"Action1",Toast.LENGTH_SHORT).show();
        } else if(intent.getAction().equals("next_image")){
            nextImg(context);
        } else if(intent.getAction().equals("previous_image")){
            prevImg(context);
        } else if(intent.getAction().equals("play")){
            play();
        } else if(intent.getAction().equals("pause")){
            pause();
        } else if(intent.getAction().equals("stop")){
            stop();
        } else if(intent.getAction().equals("prevMusic")){
            prevMusic(context);
        } else if(intent.getAction().equals("nextMusic")){
            nextMusic(context);
        }
    }



    private static void musicIntent(Context context, RemoteViews views ) {
        Intent intentPlayMusic = new Intent(context, MyWidget.class);
        intentPlayMusic.setAction("play");
        PendingIntent playPending = PendingIntent.getBroadcast(context, 0, intentPlayMusic, 0);
        views.setOnClickPendingIntent(R.id.btnPlay, playPending);

        Intent intentPauseMusic = new Intent(context, MyWidget.class);
        intentPauseMusic.setAction("pause");
        PendingIntent pausePending = PendingIntent.getBroadcast(context, 0, intentPauseMusic, 0);
        views.setOnClickPendingIntent(R.id.btnPause, pausePending);

        Intent intentStopMusic = new Intent(context, MyWidget.class);
        intentStopMusic.setAction("stop");
        PendingIntent stopPending = PendingIntent.getBroadcast(context, 0, intentPauseMusic, 0);
        views.setOnClickPendingIntent(R.id.btnStop, stopPending);

        Intent nextMusic = new Intent(context, MyWidget.class);
        nextMusic.setAction("nextMusic");
        PendingIntent nextMusicPending = PendingIntent.getBroadcast(context, 0, nextMusic, 0);
        views.setOnClickPendingIntent(R.id.btnNextMusic, nextMusicPending);

        Intent prevMusic = new Intent(context, MyWidget.class);
        prevMusic.setAction("prevMusic");
        PendingIntent prevMusicPending = PendingIntent.getBroadcast(context, 0, prevMusic, 0);
        views.setOnClickPendingIntent(R.id.btnPrevMusic, prevMusicPending);
    }



    public void play() {
        if (player == null) {

            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer player) {
                    stopPlayer();
                }
            });
        }

        player.start();
    }

    public void pause() {
        if (player != null) {
            player.pause();
        }
    }

    public void stop() {
        stopPlayer();
    }

    private void stopPlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }


    private void prevMusic(Context context) {
        if (numberOfMusic == 0) {
            numberOfMusic = musicArray.length - 1;
            stopPlayer();
            Toast.makeText(context,Integer.toString(numberOfMusic),Toast.LENGTH_SHORT).show();
            player = MediaPlayer.create(context, musicArray[numberOfMusic]);
            play();
        } else {
            numberOfMusic = numberOfMusic - 1;
            stop();
            stopPlayer();
            Toast.makeText(context,Integer.toString(numberOfMusic),Toast.LENGTH_SHORT).show();
            player = MediaPlayer.create(context, musicArray[numberOfMusic]);
            play();
        }
    }

    private void nextMusic(Context context) {
        if (numberOfMusic == musicArray.length - 1) {
            numberOfMusic = 0;
            stopPlayer();
            Toast.makeText(context,Integer.toString(numberOfMusic),Toast.LENGTH_SHORT).show();
            player = MediaPlayer.create(context, musicArray[numberOfMusic]);
            play();
        } else {
            numberOfMusic = numberOfMusic + 1;
            stop();
            stopPlayer();
            Toast.makeText(context,Integer.toString(numberOfMusic),Toast.LENGTH_SHORT).show();
            player = MediaPlayer.create(context, musicArray[numberOfMusic]);
            play();
        }
    }


    private void prevImg(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.my_widget);
        if (numberOfImg == 0) {
            numberOfImg = imgArray.length - 1;
            views.setImageViewResource(R.id.imageView, imgArray[numberOfImg]);
            AppWidgetManager.getInstance(context).updateAppWidget(new ComponentName(context, MyWidget.class), views);
        } else {
            numberOfImg = numberOfImg - 1;
            views.setImageViewResource(R.id.imageView, imgArray[numberOfImg]);
            AppWidgetManager.getInstance(context).updateAppWidget(new ComponentName(context, MyWidget.class), views);
        }
    }

    private void nextImg(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.my_widget);
        if (numberOfImg == imgArray.length - 1) {
            numberOfImg = 0;
            views.setImageViewResource(R.id.imageView, imgArray[numberOfImg]);
            AppWidgetManager.getInstance(context).updateAppWidget(new ComponentName(context, MyWidget.class), views);
        } else {
            numberOfImg = numberOfImg + 1;
            views.setImageViewResource(R.id.imageView, imgArray[numberOfImg]);
            AppWidgetManager.getInstance(context).updateAppWidget(new ComponentName(context, MyWidget.class), views);
        }
    }

}

