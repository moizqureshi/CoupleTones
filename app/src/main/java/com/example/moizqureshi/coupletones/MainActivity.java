package com.example.moizqureshi.coupletones;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;


import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import org.json.JSONException;

import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.RunnableFuture;


/**
 * Main page of the map
 */
public class MainActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    protected ourApplication app;

    private GoogleMap mMap;
    private Marker onSearchLocationMarker;
    private ArrayAdapter mAdapter, mLogAdapter, mPartnerLocAdapter;
    private boolean update = false;

    private ImageButton mSearchButton;
    private TextView mSettingTitle, mAddPartner, mDeletePartner, mSignOut;
    static TextView mPartner;
    private EditText mSearchView;
    private ListView mListView, mLogListView, mPartnerLocsView;


    private BottomBar mBottomBar;

    //ArrayList for storing the locations during runtime
    private ArrayList<String> nameOfLocationsList = new ArrayList<>();
    //ArrayList for storing the history during runtime
    private ArrayList<String> nameOfLogList = new ArrayList<>();
    //ArrayList for storing partner fav locations
    private ArrayList<String> nameOfPartnerLocs = new ArrayList<>();

    //TODO: Need to creat an ArrayList hold Partner's location's name and the user's tones' choices corresponding to the locations
    //TODO: Might be done by creating a "PartnerLocation" structure/class hold "String loc_name", "int soundSeleted", "int vibrationSelected"
    //TODO: Then make an Arraylist<PartnerLocation> inside the "User" class, then the list can be access through ourApplication
    //

    private String temp;
    private Boolean exists = false;

    MediaPlayer mpSucc;
    MediaPlayer mpFail;
    MediaPlayer mpDel;

        /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        app = (ourApplication)getApplication();
        makeUser( );
        final Handler signinHandler = new Handler();
        app.currUser = new User( );

        //Signing in wait
        signinHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                app.currUser = new User(app.acct);
                app.manager = new DataManager(app.currUser);
                app.manager.setUp();
            }}, 500);

        //Making a window to make use wait until we initialize data
        final ProgressDialog dialog=new ProgressDialog(this);
        dialog.setMessage("Initializing data");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                app.currUser = app.manager.updateUser();
                fillListFromUser();
                mAdapter.notifyDataSetChanged();
                mLogAdapter.notifyDataSetChanged();
                mPartnerLocAdapter.notifyDataSetChanged();
                dialog.hide();

                if(app.currUser.hasPartner()){
                    mAddPartner.setVisibility(View.GONE);
                    mDeletePartner.setVisibility(View.VISIBLE);
                } else {
                    mAddPartner.setVisibility(View.VISIBLE);
                    mDeletePartner.setVisibility(View.GONE);
                }
            }
        }, 3000);


        /*
            Setting up the map fragment
         */
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        /*
            Setting up the menu tab buttons
         */
        mSettingTitle = (TextView) findViewById(R.id.settings);
        mAddPartner = (TextView) findViewById(R.id.addPartner);
        mDeletePartner = (TextView) findViewById(R.id.deletePartner);
        mPartner = (TextView) findViewById(R.id.partner);
        mSignOut = (TextView) findViewById(R.id.signOut);

        /*
           TODO: on Notification Listener for adding the logs
         */
        /*
            Add partner listener
         */
        mAddPartner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddPartnerDialog( );
            }
        });
        /*
            Delete partner listener
         */
        mDeletePartner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeletePartnerDialog();
            }
        });
        /*
            Signing out listener
         */
        mSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Sounds sounds = new Sounds(getApplicationContext());
//                sounds.setPlayer(1,5);
//                sounds.playSounds();
                signOut();
            }
        });

        /*
            Setting up search text field and button
         */
        mSearchButton = (ImageButton) findViewById(R.id.searchButton);
        mSearchView = (EditText) findViewById(R.id.searchView);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        /*
            Setting up the list of locations
         */
        mAdapter = new MyListAdapter(this, R.layout.listview_item, nameOfLocationsList);
        mListView = (ListView) findViewById(R.id.listView);
        mListView.setAdapter(mAdapter);
        /*
            Setting up the list of history
         */
        mLogAdapter = new MyLogListAdapter(this, R.layout.loglist_item, nameOfLogList);
        mLogListView = (ListView) findViewById(R.id.loglistView);
        mLogListView.setAdapter(mLogAdapter);

        /*
            Setting up the partner fav locs
         */
        mPartnerLocAdapter = new MyPartnerLocsListAdapter(this,R.layout.partnerloc_item, nameOfPartnerLocs);
        mPartnerLocsView = (ListView) findViewById(R.id.partnerLocListView);
        mPartnerLocsView.setAdapter(mPartnerLocAdapter);
        /*
            Setting up the bottom tab bar
         */
        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.setActiveTabColor("#009688");
        mBottomBar.setItemsFromMenu(R.menu.bottombar_menu, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {

                if (menuItemId == R.id.bottomBarItemTwo) {
                    // The user selected item number two.
                    mSettingTitle.setVisibility(View.GONE);
                    mAddPartner.setVisibility(View.GONE);
                    mDeletePartner.setVisibility(View.GONE);
                    mPartner.setVisibility(View.GONE);
                    mSignOut.setVisibility(View.GONE);
                    mSearchView.setVisibility(View.VISIBLE);
                    mSearchButton.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.GONE);
                    mLogListView.setVisibility(View.GONE);
                    mPartnerLocsView.setVisibility(View.GONE);
                    findViewById(R.id.map).setVisibility(View.VISIBLE);
                }
                else if (menuItemId == R.id.bottomBarItemOne) {
                    // The user selected item number one.

                    mSettingTitle.setVisibility(View.VISIBLE);
                    mSignOut.setVisibility(View.VISIBLE);
                    mSearchView.setVisibility(View.GONE);
                    mSearchButton.setVisibility(View.GONE);
                    mListView.setVisibility(View.GONE);
                    mLogListView.setVisibility(View.GONE);
                    mPartnerLocsView.setVisibility(View.GONE);
                    findViewById(R.id.map).setVisibility(View.GONE);

                    if(app.currUser.hasPartner()){
                        mAddPartner.setVisibility(View.GONE);
                        mDeletePartner.setVisibility(View.VISIBLE);
                        mPartner.setText("Partner: " + app.currUser.getPartnerEmail());
                        mPartner.setVisibility(View.VISIBLE);

                    } else {
                        mAddPartner.setVisibility(View.VISIBLE);
                        mDeletePartner.setVisibility(View.GONE);
                        mPartner.setVisibility(View.GONE);
                    }

                }
                else if (menuItemId == R.id.bottomBarItemThree) {
                    // The user selected item number three.
                    mSettingTitle.setVisibility(View.GONE);
                    mAddPartner.setVisibility(View.GONE);
                    mDeletePartner.setVisibility(View.GONE);
                    mPartner.setVisibility(View.GONE);
                    mSignOut.setVisibility(View.GONE);
                    mSearchView.setVisibility(View.GONE);
                    mSearchButton.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                    mLogListView.setVisibility(View.GONE);
                    mPartnerLocsView.setVisibility(View.GONE);
                    findViewById(R.id.map).setVisibility(View.GONE);
                }
                else if (menuItemId == R.id.bottomBarItemFour) {
                    // The user selected item number three.
                    if( !app.currUser.getPartnerEmail().equals("--") )
                        fillPartnerLocFromUser();
                    //line below is just for testing the drop down lists
                    //nameOfPartnerLocs.add("sound");
                    //mPartnerLocAdapter.notifyDataSetChanged();
                    mSettingTitle.setVisibility(View.GONE);
                    mAddPartner.setVisibility(View.GONE);
                    mDeletePartner.setVisibility(View.GONE);
                    mPartner.setVisibility(View.GONE);
                    mSignOut.setVisibility(View.GONE);
                    mSearchView.setVisibility(View.GONE);
                    mSearchButton.setVisibility(View.GONE);
                    mListView.setVisibility(View.GONE);
                    mLogListView.setVisibility(View.GONE);
                    mPartnerLocsView.setVisibility(View.VISIBLE);
                    findViewById(R.id.map).setVisibility(View.GONE);
                }
                else if (menuItemId == R.id.bottomBarItemFive) {
                    // The user selected item number three.
                    if( !app.currUser.getPartnerEmail().equals("--") )
                        fillLogFromUser();
                    //mLogAdapter.notifyDataSetChanged();
                    mSettingTitle.setVisibility(View.GONE);
                    mAddPartner.setVisibility(View.GONE);
                    mDeletePartner.setVisibility(View.GONE);
                    mPartner.setVisibility(View.GONE);
                    mSignOut.setVisibility(View.GONE);
                    mSearchView.setVisibility(View.GONE);
                    mSearchButton.setVisibility(View.GONE);
                    mListView.setVisibility(View.GONE);
                    mLogListView.setVisibility(View.VISIBLE);
                    mPartnerLocsView.setVisibility(View.GONE);
                    findViewById(R.id.map).setVisibility(View.GONE);
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.bottomBarItemOne) {
                    // The user reselected item number one, scroll your content to top.
                }
            }
        });

        mBottomBar.setDefaultTabPosition(1);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


        mpSucc = MediaPlayer.create(this, R.raw.pair_succ);
        mpSucc.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                onPause();
                onStop();
            }
        });

        mpFail = MediaPlayer.create(this, R.raw.pair_fail);
        mpFail.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                onPause();
                onStop();            }
        });

        mpDel = MediaPlayer.create(this, R.raw.pair_delete);
        mpDel.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                onPause();
                onStop();
            }
        });

    }

    /**
     * Create the map and relevant iterms
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Initialize the map and the camera

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(32.8800604, -117.2362022), 15));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        /*
            Setup OnMapClickListener
         */
        GoogleMap.OnMapClickListener mapClickListener = new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(final LatLng latLng) {

                showAddLocationDialog(latLng);

            }
        };
        mMap.setOnMapClickListener(mapClickListener);

        /*
            Track current location.
         */
        LocationListener locationListener = new LocationListener() {
//            ArrayList<String> locNames = new ArrayList<>();
            String locName = "--";
            @Override
            public void onLocationChanged(Location location) {
                /*
                    Implementing favorite location(s) detection
                 */
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
                /*
                    A loop go through the list of Fav. Loc. to see if closed to and if visiting
                    "usersFavLocsList" is the name of the list which storing the user's Fav. Locs. - can be modify later
                 */

                for(int i = 0; i < app.currUser.getLocations().size(); i++) {
                    Log.d("Test1", " " +
                            distanceBetween(new LatLng(location.getLatitude(), location.getLongitude()),
                                    app.currUser.getLocations().get(i).getLocation()) + " at " + i);
                    Log.d("Test2", " " + app.currUser.getLocations().get(i).getName() + " " + app.currUser.getLocations().get(i).getLocation());
                    //Checking if user is visiting.
                    // "< 161" means if the distance between user and the Fav. Loc. is less than 161 meters which is 0.1 mile
                    //Detect if the user has left
                    if(app.currUser.getLocations().searchLoc(locName) != null) {
                        if ((distanceBetween(new LatLng(location.getLatitude(), location.getLongitude()),
                                app.currUser.getLocations().searchLoc(locName).getLocation())) > 161) {

                            Log.d("Test4", "has left" + locName);
                            LocHist currPlace = app.currUser.getHistory().search(locName);
                            if( currPlace != null ) {
                                currPlace.setOutTime(Calendar.getInstance());
                                app.manager.updateHistory( app.currUser );
                            }
                            locName = "--";
                        }
                    }
                    if (distanceBetween(new LatLng(location.getLatitude(), location.getLongitude()),
                            app.currUser.getLocations().get(i).getLocation()) < 161) { //deleted .locations.
                        //manager.sendMessage(currUser.getLocations().get(i).getName());
                        if (locName.compareTo(app.currUser.getLocations().get(i).getName()) != 0) {
                            Log.d("Test3", "At a fav location");
                            app.manager.fetchPartnerId(app.manager.getPartnerEmail());
                            final String name = app.currUser.getLocations().get(i).getName();
                            app.currUser.getHistory().add( new LocHist( name, Calendar.getInstance() ) );
                            final Handler handlerP = new Handler();

                            handlerP.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    app.manager.sendMessage(name);
                                    //app.manager.updateHistory( app.currUser );
                                    app.manager.refreshHistory( app.currUser );
                                }
                            }, 2000);
                            locName = app.currUser.getLocations().get(i).getName();
                        }
                    }

                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.GPS_PROVIDER;
        //Checking permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            Log.d("test1", "ins");
            return;
        }
        locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);


        /*
            Implementing Searching by address.
         */
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String g = mSearchView.getText().toString();

                Geocoder geocoder = new Geocoder(getBaseContext());
                List<Address> addresses = null;

                try {
                    // Getting a maximum of 3 Address that matches the input
                    // text
                    addresses = geocoder.getFromLocationName(g, 3);
                    if (addresses != null && !addresses.equals("")) {
                        searchAddresses(addresses);
                    }
                } catch (Exception e) {

                }
            }
        });
    }

    /*
        Helper function for search by address.
     */
    protected void searchAddresses(List<Address> addresses) {

        Address address = (Address) addresses.get(0);
        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

        String.format("%s, %s", address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "", address.getCountryName());

        //mMap.clear();
        if( !update ) {
            onSearchLocationMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(String.format("%s, %s",
                    address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                    address.getCountryName())));
            update = true;
        }
        else {
            onSearchLocationMarker.hideInfoWindow();
            onSearchLocationMarker.setPosition(latLng);
            onSearchLocationMarker.setTitle(String.format("%s, %s",
                    address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                    address.getCountryName()));
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));

    }

    /*
        Helper function to compute the distance between two LatLngs, in Meters.
     */
    protected float distanceBetween(LatLng from, LatLng to) {
//        double earthRadius = 6371009;
//        double dLat = toRadians(from.latitude) - toRadians(to.latitude);
//        double dLng = toRadians(from.longitude) - toRadians(to.longitude);
//        return ((sin((dLat) * 0.5)) * (sin((dLat) * 0.5))
//                + (sin((dLng) * 0.5)) * (sin((dLng) * 0.5))
//                * cos(from.latitude) * cos(to.latitude)) * earthRadius;
        float[] res = new float[2];
        Location.distanceBetween(from.latitude, from.longitude, to.latitude, to.longitude, res);

        return res[0];
    }
    /*
        Dialog for Adding a Favorite Location
     */

    protected void showAddLocationDialog(final LatLng latLng) {

        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

        alert.setTitle("Add a favorite location");
        alert.setMessage("Please assign a location name:");

        // Set an EditText view to get user input
        final EditText input = new EditText(MainActivity.this);
        input.setSingleLine();
        input.setGravity(Gravity.CENTER_HORIZONTAL);
        alert.setView(input);

        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                if (isAlphaNumeric(input.getText().toString())) {
                    boolean duplicateNameOrLocation = false;
                    String locName = input.getText().toString();

                    for (int i = 0; i < app.currUser.getLocations().size(); i++) {
                    /*
                        No duplicate name
                        And not allowing adding location that close to an existing location by 0.2 mile which is 362 meters
                     */

                        if ((locName.compareTo(app.currUser.getLocations().get(i).getName()) == 0)
                                || (distanceBetween(latLng, app.currUser.getLocations().get(i).getLocation()) < 362)) {

                            duplicateNameOrLocation = true;
                        }
                    }
                    //duplicateNAmeOrLocation = currUser.getLocations().isValid( locName, latLng );
                    if (!duplicateNameOrLocation) {
                        //Adding the locations here
                        Toast.makeText(getApplicationContext(), "Location Added", Toast.LENGTH_SHORT).show();

                        app.currUser.getLocations().add(new FavLocation(locName, latLng));
                        try {
                            app.manager.updateLocations( app.currUser, app.currUser.getLocations().getList() );
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //Updating local list
                        nameOfLocationsList.add(locName);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getApplicationContext(), (CharSequence) "You already have the same name or the same location", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    showAddLocationDialog(latLng);
                    Toast.makeText(getApplicationContext(), (CharSequence) "Invalid input", Toast.LENGTH_LONG).show();
                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();

    }

    /*
        Adding Fav. Loc. by click on the result marker from searching
     */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        if (marker.equals(onSearchLocationMarker)) {
            showAddLocationDialog(new LatLng(marker.getPosition().latitude, marker.getPosition().longitude));
        }
        return true;
    }
    /*
        Function for coping the list from server to local
     */
    public void fillListFromUser() {
        if (app.currUser.getLocations().size() != 0){
            for (int i = 0; i < app.currUser.getLocations().size(); i++) {
                nameOfLocationsList.add(app.currUser.getLocations().get(i).getName());
            }
        }

        final Logs partnerLog = app.manager.fetchPartnerHistory();

        Handler hd = new Handler();
        hd.postDelayed(new Runnable() {
            @Override
            public void run(){
                if( partnerLog.size() != 0) {
                    for( int i = partnerLog.size()-1; i >= 0; i-- ) {
                        nameOfLogList.add( partnerLog.get(i).toString() );
                        //Log.d("Adding a partner's log", "The partner is: " + app.currUser.getPartnerEmail() + " and the history is: " + partnerLog.get(i).toString() );
                    }
                }
                //mLogAdapter.notifyDataSetChanged();
            }}, 2000);


        final Locations partnerLocs = app.manager.fetchPartnerLocations();

        Handler hd2 = new Handler();
        hd2.postDelayed( new Runnable() {
            @Override
            public void run() {
                if( partnerLocs.size() != 0) {
                    for( int i = 0; i < partnerLocs.size(); i++ ) {
                        nameOfPartnerLocs.add( partnerLocs.get(i).getName() );
                    }
                }

            }
        }, 2000); //THIST

    }
    /*
        Function for only filling the partner's loc's list from user
     */
    public void fillPartnerLocFromUser() {
        nameOfPartnerLocs.removeAll( nameOfPartnerLocs );
        app.partnerLocs = app.manager.fetchPartnerLocations();
        final Locations partnerLocs = app.partnerLocs;//app.manager.fetchPartnerLocations();

        Handler hd2 = new Handler();
        hd2.postDelayed( new Runnable() {
            @Override
            public void run() {
                if( partnerLocs.size() != 0) {
                    for( int i = 0; i < partnerLocs.size(); i++ ) {
                        nameOfPartnerLocs.add( partnerLocs.get(i).getName() );
                    }
                }
                mPartnerLocAdapter.notifyDataSetChanged();
            }
        }, 1000);


    }
    /*
        Function for only filling the log's list from user
     */
    public void fillLogFromUser() {
        nameOfLogList.removeAll( nameOfLogList );
        final Logs partnerLog = app.manager.fetchPartnerHistory();

        Handler hd = new Handler();
        hd.postDelayed(new Runnable() {
            @Override
            public void run(){
                if( partnerLog.size() != 0) {
                    for( int i = partnerLog.size()-1; i >= 0; i-- ) {
                        nameOfLogList.add( partnerLog.get(i).toString() );
                        Log.d("Adding a partner's log", "The partner is: " + app.currUser.getPartnerEmail() + " and the history is: " + partnerLog.get(i).toString() );
                    }
                }
                mLogAdapter.notifyDataSetChanged();
                //mLogAdapter.notifyDataSetChanged();
            }}, 1000);
    }

    /*
        Function for initializing the user
     */

    private void makeUser( ) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        app.mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, null /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        app.opr = Auth.GoogleSignInApi.silentSignIn(app.mGoogleApiClient);

    }

    /*
        Function for signing out
     */
    private void signOut( ) {

        Auth.GoogleSignInApi.signOut(app.mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        // [END_EXCLUDE]
                    }
                });
        Toast.makeText( getApplicationContext(), (CharSequence) "You are signed out!", Toast.LENGTH_LONG ).show( );
    }

    /*
        Dialog for Adding the partner
     */
    protected void showAddPartnerDialog( ) {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle("Pair with a partner");
        alert.setMessage("Please enter your partner's email:");

        // Set an EditText view to get user input
        final EditText input = new EditText(MainActivity.this);
        input.setSingleLine();
        input.setGravity(Gravity.CENTER_HORIZONTAL);
        alert.setView(input);

        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                final String partnerEmail = input.getText().toString();
                if(!partnerEmail.equals("")) {
                    final ProgressDialog dialogP=new ProgressDialog(MainActivity.this);

                    app.manager.findPartnerEmail(partnerEmail);
                    app.manager.fetchPartnerId(partnerEmail);


                    dialogP.setMessage("Searching...");
                    dialogP.setCancelable(false);
                    dialogP.setInverseBackgroundForced(false);
                    dialogP.show();

                    final Handler handlerP = new Handler();
                    handlerP.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(!app.manager.getPartnerEmail().equals("--")) {
                               app.manager.sendPairRequest(app.manager.getPartnerId());
                               mPartner.setText("Pending invitation");

                            } else {
                                Toast.makeText(getApplicationContext(), (CharSequence) "Partner does not have CoupleTones!", Toast.LENGTH_LONG).show();
                                mpFail.start();
                            }
                            dialogP.hide();
                        }
                    }, 1000);

                } else {
                    Toast.makeText( getApplicationContext(), (CharSequence) "Partner email was blank!", Toast.LENGTH_LONG ).show( );
                }
              }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

   /*
        Dialog for deleting the partner
     */
    protected void showDeletePartnerDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

        alert.setTitle("Confirm partner unpairing");
        alert.setMessage("Are you sure want to unpair with your partner?");


        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {

                if (!app.currUser.hasPartner()) {
                    Toast.makeText( getApplicationContext(), (CharSequence) "Not currently paired with anyone!", Toast.LENGTH_LONG ).show( );
                }
                Toast.makeText( getApplicationContext(), (CharSequence) "Removed partner", Toast.LENGTH_LONG ).show( );
                app.currUser.removePartner();
                app.manager.updatePartnerEmail(app.currUser);

                mpDel.start();

                mAddPartner.setVisibility(View.VISIBLE);
                mPartner.setText("");
                mPartner.setVisibility(View.GONE);
                mDeletePartner.setVisibility(View.GONE);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

    public static void setPartnerTxt(String partnerEmail){
        mPartner.setText("Partner: " + partnerEmail);
    }



    /*
        Checking if the input is alphanumeric
     */
    protected boolean isAlphaNumeric(String s){
        String pattern= "^[a-zA-Z0-9 ]+$";
        return s.matches(pattern);
    }

    /*
        Necessary to restore the BottomBar's state
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        mBottomBar.onSaveInstanceState(outState);
    }

    /*
        Adapter for dynamically constructing the favorite location's listview
     */
    public class MyListAdapter extends ArrayAdapter<String> {

        private int layout;
        private List<String> myObjects;

        public MyListAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            myObjects = objects;
            layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewHolder = null;
            ViewHolder viewHolder = new ViewHolder();
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                viewHolder.title = (TextView) convertView.findViewById(R.id.list_item_text);
                viewHolder.button = (Button) convertView.findViewById(R.id.list_item_botton);
                convertView.setTag(viewHolder);
            }

            mainViewHolder = (ViewHolder) convertView.getTag();
            //Removing the location
            mainViewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String locName = nameOfLocationsList.get(position);
                    app.currUser.getLocations().remove(locName);
                    try {
                        app.manager.updateLocations( app.currUser, app.currUser.getLocations().getList() );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //Removing from the local
                    nameOfLocationsList.remove(position);
                    mAdapter.notifyDataSetChanged();

                    Toast.makeText(getContext(), "Location: " + locName + " deleted", Toast.LENGTH_SHORT).show();
                }
            });
            mainViewHolder.title.setText(getItem(position));

            return convertView;
        }

        protected class ViewHolder {

            TextView title;
            Button button;
        }
    }

    /*
        Adapter for dynamically constructing the log's listview
     */
    public class MyLogListAdapter extends ArrayAdapter<String> {

        private int layout;
        private List<String> myLogObjects;

        public MyLogListAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            myLogObjects = objects;
            layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewHolder = null;
            ViewHolder viewHolder = new ViewHolder();
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                viewHolder.title = (TextView) convertView.findViewById(R.id.log_item_text);
                convertView.setTag(viewHolder);
            }

            mainViewHolder = (ViewHolder) convertView.getTag();
            mainViewHolder.title.setText(getItem(position));

            return convertView;
        }

        protected class ViewHolder {
            TextView title;
        }
    }

    /*
        Adapter for dynamically constructing the Partner fav location's listview
    */
    public class MyPartnerLocsListAdapter extends ArrayAdapter<String> {

        private int layout;
        private List<String> myLogObjects;

        public MyPartnerLocsListAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            myLogObjects = objects;
            layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewHolder = null;
            ViewHolder viewHolder = new ViewHolder();
            //if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                viewHolder.title = (TextView) convertView.findViewById(R.id.partnerLoc_item_text);

                viewHolder.soundList = (Spinner) convertView.findViewById(R.id.sound_list);
                int soundIdx = app.partnerLocs.get(position).getSoundIdx(0);
                viewHolder.soundList.setSelection(soundIdx);

                viewHolder.soundList2 = (Spinner) convertView.findViewById(R.id.sound_list2);
                int soundIdx2 = app.partnerLocs.get(position).getSoundIdx(1);
                viewHolder.soundList2.setSelection(soundIdx2);

                viewHolder.vibrationList = (Spinner) convertView.findViewById(R.id.vibration_list);
                int vibeIdx = app.partnerLocs.get(position).getVibeIdx(0);
                viewHolder.vibrationList.setSelection(vibeIdx);

                viewHolder.vibrationList2 = (Spinner) convertView.findViewById(R.id.vibration_list2);
                int vibeIdx2 = app.partnerLocs.get(position).getVibeIdx(1);
                viewHolder.vibrationList2.setSelection(vibeIdx2);

                convertView.setTag(viewHolder);
            //}

            mainViewHolder = (ViewHolder) convertView.getTag();

            //Select arrive sound tone
            mainViewHolder.soundList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int pos, long id) {
                    // An item was selected. Can retrieve the selected item using parent.getItemAtPosition(pos)
                    app.partnerLocs.get(position).setSoundIdx(0, pos);
                    app.manager.updateLocations(app.currUser.getPartnerEmail(), app.partnerLocs);
                    Log.d("Sound drop down list", " Selected " + parent.getItemAtPosition(pos) + " at position " + position);
                }

                public void onNothingSelected(AdapterView<?> parent) {
                    // Another interface callback, no need to modify
                }

            });

            //Select a leave sound tone
            mainViewHolder.soundList2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int pos, long id) {
                    // An item was selected. Can retrieve the selected item using parent.getItemAtPosition(pos)
                    app.partnerLocs.get(position).setSoundIdx(1, pos);
                    app.manager.updateLocations(app.currUser.getPartnerEmail(), app.partnerLocs);
                    Log.d("Sound drop down list", " Selected " + parent.getItemAtPosition(pos) + " at position " + position);
                }

                public void onNothingSelected(AdapterView<?> parent) {
                    // Another interface callback, no need to modify
                }

            });

            //Select arrive vibration tone
            mainViewHolder.vibrationList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int pos, long id) {
                    // An item was selected. Can retrieve the selected item using parent.getItemAtPosition(pos)
                    app.partnerLocs.get(position).setVibeIdx(0, pos);
                    app.manager.updateLocations(app.currUser.getPartnerEmail(), app.partnerLocs);
                    Log.d("Vib drop down list", " Selected " + parent.getItemAtPosition(pos) + " at position " + position);

                }

                public void onNothingSelected(AdapterView<?> parent) {
                    // Another interface callback, no need to modify
                }

            });

            //Select leave vibration tone
            mainViewHolder.vibrationList2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int pos, long id) {
                    // An item was selected. Can retrieve the selected item using parent.getItemAtPosition(pos)
                    app.partnerLocs.get(position).setVibeIdx(1, pos);
                    app.manager.updateLocations(app.currUser.getPartnerEmail(), app.partnerLocs);
                    Log.d("Vib drop down list", " Selected " + parent.getItemAtPosition(pos) + " at position " + position);

                }

                public void onNothingSelected(AdapterView<?> parent) {
                    // Another interface callback, no need to modify
                }

            });

            mainViewHolder.title.setText(getItem(position));

            return convertView;
        }

        protected class ViewHolder {
            TextView title;
            Spinner soundList;
            Spinner vibrationList;
            Spinner soundList2;
            Spinner vibrationList2;

        }
    }
}

