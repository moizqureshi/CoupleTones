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
        sound.setPlayer(1,4);
        sound.playSounds();
        try {
            //Log.i("OneSignalExample", "NotificationTable content: " + dataBundle.getString("alert"));
            Log.i("OneSignalExample", "NotificationTable title: " + dataBundle.getString("title"));
            Log.i("OneSignalExample", "Is Your App Active: " + dataBundle.getBoolean("isActive"));
            Log.i("OneSignalExample", "data addt: " + dataBundle.getString("custom"));
            JSONObject customJSON = new JSONObject(dataBundle.getString("custom"));
            if (customJSON.has("a"))
                Log.i("OneSignalExample", "additionalData:key_a: " + customJSON.getJSONObject("a").getString("a"));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}