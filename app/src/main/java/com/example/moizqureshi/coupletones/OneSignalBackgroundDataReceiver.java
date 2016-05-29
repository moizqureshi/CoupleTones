package com.example.moizqureshi.coupletones;

import android.support.v4.content.WakefulBroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONObject;

/**
 * Created by moizqureshi on 5/25/16.
 */

public class OneSignalBackgroundDataReceiver extends WakefulBroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Bundle dataBundle = intent.getBundleExtra("data");
        Sounds sound = new Sounds(context);
        int soundIdx;
        int vibeIdx;

        try {
            JSONObject customJSON = new JSONObject(dataBundle.getString("custom"));
            if (customJSON.has("a")) {
                soundIdx = Integer.parseInt(customJSON.getJSONObject("a").getString("sound"));
                Log.i("SoundIdx", customJSON.getJSONObject("a").getString("sound"));
                sound.setPlayer(soundIdx);
                sound.playSound();

                vibeIdx = Integer.parseInt(customJSON.getJSONObject("a").getString("vibe"));
                Log.i("VIbeIdx", customJSON.getJSONObject("a").getString("vibe"));
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}