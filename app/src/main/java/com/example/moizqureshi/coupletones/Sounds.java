package com.example.moizqureshi.coupletones;

import android.app.Application;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

/**
 * Created by moizqureshi on 5/25/16.
 */
public class Sounds {

    private Context context;
    public MediaPlayer mp1, mp2;

    private int[] defaultSound = { R.raw.space1, R.raw.space2};
    private int[] soundList = { R.raw.birdy, R.raw.bottle, R.raw.elastic, R.raw.enigmatic, R.raw.ice,
            R.raw.mallet, R.raw.softbell, R.raw.spaceball, R.raw.toy, R.raw.wood };

    public Sounds(Context context) {
        this.context = context;
        mp1 = new MediaPlayer();
        mp2 = new MediaPlayer();
    }

    public void setPlayer(int def, int idx) {
        mp1 = MediaPlayer.create(context, defaultSound[def]);
        mp2 = MediaPlayer.create(context, soundList[idx]);
    }

    public void playSounds() {
        Log.d("Test", "in playSounds");
        mp1.start();
        mp1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.pause();
                mp.stop();
                mp2.start();
                mp2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.pause();
                        mp.stop();
                    }
                });
            }
        });
    }


}
