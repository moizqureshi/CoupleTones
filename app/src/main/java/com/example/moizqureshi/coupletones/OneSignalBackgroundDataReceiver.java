package com.example.moizqureshi.coupletones;

import android.media.AudioManager;
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
        Vibes vibe = new Vibes();

        int soundIdx;
        int vibeIdx;

        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int mode = am.getRingerMode();


        try {
            JSONObject customJSON = new JSONObject(dataBundle.getString("custom"));
            if (customJSON.has("a")) {

                if(customJSON.getJSONObject("a").has("sound")) {
                    Log.i("SoundIdx", customJSON.getJSONObject("a").getString("sound"));
                    soundIdx = Integer.parseInt(customJSON.getJSONObject("a").getString("sound"));
                    if (mode == AudioManager.RINGER_MODE_NORMAL) {
                        Log.d("Ringer Mode", Integer.toString(am.getRingerMode()));
                        sound.setPlayer(soundIdx);
                        sound.playSound();
                    }
                }

                if(customJSON.getJSONObject("a").has("vibe")) {
                    Log.i("VIbeIdx", customJSON.getJSONObject("a").getString("vibe"));
                    vibeIdx = Integer.parseInt(customJSON.getJSONObject("a").getString("vibe"));
                    if (mode == AudioManager.RINGER_MODE_VIBRATE || mode == AudioManager.RINGER_MODE_NORMAL) {
                        vibe.play(vibeIdx, context);
                    }
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}