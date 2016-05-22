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
    String signalId;
    String partnerId;
    String partnerEmail;

    public DataManager(User user) {
        this.user = user;
    }

    public void setUp(  ) {
        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                signalId = userId;
                user.setSingalId(userId);
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
                                        partnerId = object.getString("partnerId");
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

    public void fetchPartnerId(String email ) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("CoupleTones");
        query.whereEqualTo("email", email);
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
            JSONObject pushJson = new JSONObject("{'contents': {'en':'" + msg + "'}, 'include_player_ids': ['" + partnerId + "'], 'android_sound':'space_push', 'large_icon':'ic_launcher'}");
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

    public void sendPairRequest(String id) {
        JSONObject pushJSON = new JSONObject();
        String msg;
        msg = "Accept pair request from " + user.getEmail();


        try{
            JSONObject pushJson = new JSONObject("{'contents': {'en':'" + msg + "'}, 'include_player_ids': ['" + id + "'], 'large_icon':'ic_launcher', " +
                    "'buttons': [{id: 'acceptBtn', text: 'Accept', icon:'ic_done_white_48dp'}, {id: 'declineBtn', text: 'Decline', icon: 'ic_close_white_48dp'}], 'data': {'email': '" + user.getEmail() + "', 'fromID':'" + user.getSingalId() +"' }}");
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

    public void sendPairSuccess(String id, String email) {
        JSONObject pushJSON = new JSONObject();
        String msg;
        msg = "Pairing with " + email + " was successful!";

        try{
            JSONObject pushJson = new JSONObject("{'contents': {'en':'" + msg + "'}, 'include_player_ids': ['" + id + "'], 'large_icon':'ic_launcher', 'data': {'email': '" + user.getEmail() + "', 'fromID':'" + user.getSingalId() +"', 'pairSuccess':'true' }}");
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

    public void sendPairFail(String id, String email) {
        JSONObject pushJSON = new JSONObject();
        String msg;
        msg = "Pairing with " + email + " was denied!";

        try{
            JSONObject pushJson = new JSONObject("{'contents': {'en':'" + msg + "'}, 'include_player_ids': ['" + id + "'], 'large_icon':'ic_launcher', 'data': {'email': '" + user.getEmail() + "', 'fromID':'" + user.getSingalId() +"', 'pairSuccess':'false' }}");
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



