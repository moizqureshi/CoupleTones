package com.example.moizqureshi.coupletones;

import android.app.Application;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.onesignal.OneSignal;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.FindCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Benjamin on 5/5/2016.
 */
public class DataManager {
    User user;
    String partnerId;
    String partnerEmail;

    public DataManager(User user) {
        this.user = user;
    }

    public void setUp(  ) {
        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                final String signal_id = userId;
                ParseQuery<ParseObject> query = ParseQuery.getQuery("CoupleTones");
                query.whereEqualTo("email", user.getEmail() );
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    public void done(ParseObject object, ParseException e) {
                        if (object == null) { //There isn't a user
                            ParseObject obj = new ParseObject("CoupleTones");
                            obj.put("email", user.getEmail( ) );
                            obj.put("signal_id", signal_id);
                            obj.put("partnerEmail", user.getPartnerEmail() );
                            obj.saveInBackground();
                        } else {
                            //Get information
                            object.put("signal_id", signal_id);
                            object.saveInBackground();
                            object.fetchInBackground(new GetCallback<ParseObject>() {
                                public void done(ParseObject object, ParseException e) {
                                    if (e == null) {
                                        //partnerId = object.getString("partnerId");
                                        partnerEmail = object.getString("partnerEmail");
                                        user.setPartnerEmail(partnerEmail);
//                                        Log.d("test", "user has partner "+getPartnerEmail());

                                        try {
                                            user.getLocations().update(object.getJSONArray("locationsList"));
                                        } catch (JSONException e1) {
                                            e1.printStackTrace();
                                        }
                                        try {
                                            user.getHistory().update(object.getJSONArray("HistoryList"));
                                        } catch (JSONException e1) {
                                            e1.printStackTrace();
                                        }
                                    } else {
                                        // Failure!
                                    }
                                }
                            });

                        }
                    }
                });

            }
        });
        user.setHasPartner(user.hasPartner());
    }

    public Logs getPartnerHistory( ) {
        final Logs partnerHistory = new Logs();
        ParseQuery< ParseObject > query = ParseQuery.getQuery("CoupleTone");
        query.whereEqualTo("email", user.getPartnerEmail());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    //Log.d("getPartnerHistory", "Could not find such user");
                } else {
                    try {
                        partnerHistory.update(object.getJSONArray("HistoryList"));
                    } catch( JSONException e2 ) {
                        e2.printStackTrace();
                    }
                }
            }
        });

        return partnerHistory;
    }

    //need to call update user after using this.
    public void refreshHistory( User newUser ) {
        user = newUser;
        boolean parse = false;
        long fullDay = 24*3600*1000;
        long allSec = 0;

        while( true ) {
            if( user.getHistory().size() == 0)
                break;
            allSec = user.getHistory().get().getInTime().getTimeInMillis();

            if( (allSec + fullDay) <= allSec ) {
                user.getHistory().remove();
                parse = true;
            } else {
                break;
            }
        }

        if ( !parse )
            return;

        updateHistory( null );


    }

    public void updateHistory( User newUser ) {
        if( newUser != null )
            user = newUser;

        ParseQuery<ParseObject> query = ParseQuery.getQuery("CoupleTones");
        query.whereEqualTo("email", user.getEmail() );
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) { //There isn't a user

                } else {
                    try {
                        object.put("HistoryList", user.getHistory().getList() );
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }

                    object.saveInBackground();
                }
            }
        });

    }

    public void updatePartnerEmail( User newUser ) {
        user = newUser;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("CoupleTones");
        query.whereEqualTo("email", user.getEmail() );
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) { //There isn't a user
                } else {
                    object.put("partnerEmail", user.getPartnerEmail() );
                    object.saveInBackground();
                }
            }
        });
    }

    public void findPartnerEmail(String email) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("CoupleTones");
        query.whereEqualTo("email", email);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) { //There isn't a user
                    partnerEmail = "--";
                } else {
                    partnerEmail = object.getString("email");
                }
            }
        });
    }

    public void updateLocations(User newUser, final JSONArray newLocations ) {
        this.user = newUser;

        ParseQuery<ParseObject> query = ParseQuery.getQuery("CoupleTones");
        query.whereEqualTo("email", user.getEmail() );
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) { //There isn't a user

                } else {
                    object.put("locationsList", newLocations );

                    object.saveInBackground();
                }
            }
        });
    }

    public void fetchPartnerId( ) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("CoupleTones");
        query.whereEqualTo("email", user.getPartnerEmail() );
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) { //There isn't a user

                } else {
                    partnerId = object.getString("signal_id");
                    Log.d("Test", "Got partnerId it is "+ partnerId);
                }
            }
        });
    }

    public String getPartnerEmail() {
        return partnerEmail;
    }

    public String getPartnerId( ) {
        return partnerId;
    }

    public User updateUser( ) {
        return user;
    }

    public void sendMessage(String locationName) {
        Log.d("MSG", "PartnerId is " + partnerId);

        JSONObject pushJSON = new JSONObject();
        String msg;
        msg = "Your partner is at " + locationName;

        try{
            JSONObject pushJson = new JSONObject("{'contents': {'en':'" + msg + "'}, 'include_player_ids': ['" + partnerId + "'], 'android_sound':'space_push', 'large_icon':'ic_launcher.jpg'}");
            Log.d("Test", "testJson is:" + '\n' + pushJson.toString());
            OneSignal.postNotification(pushJson, new OneSignal.PostNotificationResponseHandler() {
                @Override
                public void onSuccess(JSONObject response) {
                    Log.i("OneSignal", "postNotification Success: " + response.toString());
                }

                @Override
                public void onFailure(JSONObject response) {
                    Log.e("OneSignal", "postNotification Failure: " + response.toString());
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
