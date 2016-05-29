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
    public MediaPlayer mp1;

    private int[] sounds = { R.raw.birdy, R.raw.bottle, R.raw.elastic, R.raw.enigmatic, R.raw.ice,
            R.raw.mallet, R.raw.softbell, R.raw.spaceball, R.raw.toy, R.raw.wood, R.raw.space1, R.raw.space2 };

    public Sounds(Context context) {
        this.context = context;
        mp1 = new MediaPlayer();
    }

    public void setPlayer(int index) {
        mp1 = MediaPlayer.create(context, sounds[index]);
    }

    public void playSound() {
        mp1.start();
        mp1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.pause();
                mp.stop();
            }
        });
    }


}
