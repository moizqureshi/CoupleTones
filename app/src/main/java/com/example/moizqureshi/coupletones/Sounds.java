package com.example.moizqureshi.coupletones;

import android.app.Application;
import android.media.MediaPlayer;

/**
 * Created by moizqureshi on 5/25/16.
 */
public class Sounds extends Application {

    public MediaPlayer mp1, mp2;

    private int[] defaultSound = { R.raw.space1, R.raw.space2};
    private int[] soundList = { R.raw.birdy, R.raw.bottle, R.raw.elastic, R.raw.enigmatic, R.raw.ice,
            R.raw.mallet, R.raw.softbell, R.raw.spaceball, R.raw.toy, R.raw.wood };

    public Sounds() {
        mp1 = new MediaPlayer();
        mp2 = new MediaPlayer();
    }

    public void setPlayer(int def, int idx) {
        mp1 = MediaPlayer.create(this, soundList[def]);
    }




}
