package com.example.moizqureshi.coupletones;

import android.app.Application;
import android.location.Location;
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

    /**
     * This is a method to call in order to see the partner's history. It gives you a Logs object
     * which you can manipulate to show the history into the UI.
     * Need to have the right partner's ID!!!!!
     * EXAMPLE:
     * in mainactivity:
     * Logs partnerLog = app.manager.getPartnerHistory();
     *
     * then to show stuff you just call:
     * i = partnerLog.size()-1;
     * partnerLog.get( i );
     * i--
     * @return partner's history
     */
    public Logs fetchPartnerHistory( ) {
        final Logs partnerHistory = new Logs();
        ParseQuery< ParseObject > query = ParseQuery.getQuery("CoupleTones");
        query.whereEqualTo("email", user.getPartnerEmail());
       // Log.d("The email: ", user.getPartnerEmail());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                   // Log.d("getPartnerHistory", "Could not find such user: " + user.getPartnerEmail());
                } else {
                    try {
                        partnerHistory.update(object.getJSONArray("HistoryList"));
                        //Log.d("size: ", ""+partnerHistory.get().getInTime().get(Calendar.MONTH));
                    } catch( JSONException e2 ) {
                        e2.printStackTrace();
                    }
                }
            }
        });

        return partnerHistory;
    }

    public Locations fetchPartnerLocations( ) {
        final Locations partnerLocations = new Locations();
        ParseQuery< ParseObject > query = ParseQuery.getQuery("CoupleTones");
        query.whereEqualTo("email", user.getPartnerEmail());
        // Log.d("The email: ", user.getPartnerEmail());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    // Log.d("getPartnerHistory", "Could not find such user: " + user.getPartnerEmail());
                } else {
                    try {
                        partnerLocations.update(object.getJSONArray("locationsList"));
                        //Log.d("size: ", ""+partnerHistory.get().getInTime().get(Calendar.MONTH));
                    } catch( JSONException e2 ) {
                        e2.printStackTrace();
                    }
                }
            }
        });

        return partnerLocations;
    }

    /**
     * This method refreshs the history. It iterate through Logs and see if 24hour has passed already.
     * If it has then we remove it and we call updateHistory which will rewrite the history in the
     * server.
     *
     * YOU MUST call UpdateUser( ) after this call
     * @param newUser
     */
    public void refreshHistory( User newUser ) {
        user = newUser;
        //boolean parse = false;
        long fullDay = 24*3600*1000;
        long allSec = 0;

        while( true ) {
            if( user.getHistory().size() == 0)
                break;
            allSec = user.getHistory().get().getInTime().getTimeInMillis();

            if( (allSec + fullDay) <= allSec ) {
                user.getHistory().remove();
                //parse = true;
            } else {
                break;
            }
        }

        updateHistory( null );


    }

    /**
     * Updates the history in the server. Not much.
     * Example:
     * in mainactivity:
     * aftrer sending notification do the following:
     * app.currUser.getHistory().add( new LocHist( THE_LOCATION_OBJECT.getName(), Calender.getInstance() );
     * app.manager.updateHistory( app.currUser );
     * then call refresh
     * app.manager.refreshHistory( app.currUser );
     * @param newUser
     */
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
    public void updateLocations(String user, Locations locations ) {
        try {
            final JSONArray newLocations = locations.getList();
            ParseQuery<ParseObject> query = ParseQuery.getQuery("CoupleTones");
            query.whereEqualTo("email", user );
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                public void done(ParseObject object, ParseException e) {
                    if (object == null) { //There isn't a user

                    } else {
                        object.put("locationsList", newLocations  );

                        object.saveInBackground();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

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

    public void sendArriveMessage(String locationName) {

        //sendBackgroundMessage(1,2);

        JSONObject pushJSON = new JSONObject();
        String msg;
        msg = "Your partner is at " + locationName;

        try{
            JSONObject pushJson = new JSONObject("{'contents': {'en':'" + msg + "'}, 'include_player_ids': ['" + partnerId + "'], 'large_icon':'ic_launcher'}");
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

    public void sendDepartMessage(String locationName) {

        //sendBackgroundMessage(1,2);

        JSONObject pushJSON = new JSONObject();
        String msg;
        msg = "Your partner has left " + locationName;

        try{
            JSONObject pushJson = new JSONObject("{'contents': {'en':'" + msg + "'}, 'include_player_ids': ['" + partnerId + "'], 'large_icon':'ic_launcher'}");
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
    public void sendBackgroundMessage(int soundIdx, int vibeIdx) {
        JSONObject pushJSON = new JSONObject();
        try{
            JSONArray partnerIdArr = new JSONArray();
            partnerIdArr.put(partnerId);
            pushJSON.put("include_player_ids", partnerIdArr);
            pushJSON.put("android_background_data", true);

            JSONObject data = new JSONObject();
            data.put("sound", soundIdx);
            data.put("vibe", vibeIdx);
            pushJSON.putOpt("data", data);

            Log.i("SoundIdx", Integer.toString(soundIdx));
            Log.i("VibeIdx", Integer.toString(soundIdx));

            OneSignal.postNotification(pushJSON, new OneSignal.PostNotificationResponseHandler() {
                @Override
                public void onSuccess(JSONObject response) {
                    Log.i("OneSignal", "postNotificationBG Success: " + response.toString());
                }

                @Override
                public void onFailure(JSONObject response) {
                    Log.e("OneSignal", "postNotificationBG Failure: " + response.toString());
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



