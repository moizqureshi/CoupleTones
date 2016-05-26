package com.example.moizqureshi.coupletones;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.maps.MapFragment;
import com.onesignal.OneSignal;
import com.parse.Parse;
import android.widget.TextView;
import android.view.View;


import org.json.JSONObject;


/**
 * Created by Team 3 on 5/5/16.
 */
public class ourApplication extends Application {
    OptionalPendingResult<GoogleSignInResult> opr;
    GoogleApiClient mGoogleApiClient;
    GoogleSignInAccount acct;

    DataManager manager;
    User currUser;

    public void onCreate(){
        super.onCreate();


        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("CoupleTones")
                .server("http://coupletones.herokuapp.com/parse/")
                .clientKey("qureshi10726990")
                .build()
        );

        OneSignal.startInit(this)
                .setAutoPromptLocation(true)
                .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler())
                .init();
        OneSignal.enableInAppAlertNotification(true);
        OneSignal.enableNotificationsWhenActive(false);

    }

    private class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
        /**
         * Callback to implement in your app to handle when a notification is opened from the Android status bar or
         * a new one comes in while the app is running.
         * This method is located in this Application class as an example, you may have any class you wish implement NotificationOpenedHandler and define this method.
         *
         * @param message        The message string the user seen/should see in the Android status bar.
         * @param additionalData The additionalData key value pair section you entered in on onesignal.com.
         * @param isActive       Was the app in the foreground when the notification was received.
         */
        @Override
        public void notificationOpened(String message, JSONObject additionalData, boolean isActive) {
            String additionalMessage = "";
            try {
                if (additionalData != null) {
                    Log.d("Notification from: ", additionalData.getString("fromID"));
                    if (additionalData.has("actionSelected")) {
                        if(additionalData.getString("actionSelected").toString().equals("acceptBtn")) {
                            currUser.setPartnerEmail(additionalData.getString("email"));
                            manager.updatePartnerEmail(currUser);
                            MainActivity.setPartnerTxt(currUser.getPartnerEmail());
                            manager.sendPairSuccess(additionalData.getString("fromID"), additionalData.getString("email"));

                            additionalMessage += "Pressed ButtonID: " + additionalData.getString("actionSelected");
                            Log.d("Test", "Accepted pair request: Partner is: " + currUser.getPartnerEmail());
                        } else {
                            manager.sendPairFail(additionalData.getString("fromID"), additionalData.getString("email"));
                        }
                    }

                    if (additionalData.has("pairSuccess")) {
                        if(additionalData.getString("pairSuccess").toString().equals("true")) {
                            currUser.setPartnerEmail(additionalData.getString("email"));
                            manager.updatePartnerEmail(currUser);
                            MainActivity.setPartnerTxt(currUser.getPartnerEmail());
                            Log.d("Test", "Pairing was successful with " + currUser.getPartnerEmail());
                        } else {
                            Log.d("Test", "Pairing declined by " + additionalData.getString("email"));
                        }
                    }

                    additionalMessage = message + "\nFull additionalData:\n" + additionalData.toString();
                }

                Log.d("OneSignalExample", "message:\n" + message + "\nadditionalMessage:\n" + additionalMessage);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
}
