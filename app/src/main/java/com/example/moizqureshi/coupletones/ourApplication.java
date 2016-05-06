package com.example.moizqureshi.coupletones;

import android.app.Application;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.onesignal.OneSignal;
import com.parse.Parse;

import org.json.JSONObject;


/**
 * Created by Team 3 on 5/5/16.
 */
public class ourApplication extends Application {
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
                    if (additionalData.has("actionSelected"))
                        additionalMessage += "Pressed ButtonID: " + additionalData.getString("actionSelected");

                    additionalMessage = message + "\nFull additionalData:\n" + additionalData.toString();
                }

                Log.d("OneSignalExample", "message:\n" + message + "\nadditionalMessage:\n" + additionalMessage);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }





}
