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

/**
 * Created by Benjamin on 5/5/2016.
 */
public class DataManager {
    User user;
    String partnerId;
    String partnerEmail;

    DataManager(User user) {
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
                                        partnerEmail = object.getString("partnerEmail");
                                        user.setPartnerEmail( partnerEmail );
//                                        Log.d("test", "user has partner "+getPartnerEmail());

                                        try {
                                            user.getLocations().update( object.getJSONArray("locationsList") );
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
        Log.d("MSG", "PartnerId is " + getPartnerId());

        JSONObject pushJSON = new JSONObject();
        String msg;
        msg = "Your partner is at " + locationName;

        try {
            pushJSON.put("contents", "{'en':'" + msg + "'}");
            pushJSON.put("include_player_ids", "['" + partnerId + "']");
            pushJSON.put("android_sound", "space_push");
            pushJSON.put("large_icon", "ic_launcher.png");

            OneSignal.postNotification(pushJSON, null);

        } catch (JSONException e) {
            e.printStackTrace();
        }

//        try {
//            OneSignal.postNotification(new JSONObject("{'contents': {'en':'" + msg + "'}, 'include_player_ids': ['" + partnerId + "']}"), null);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }


}
