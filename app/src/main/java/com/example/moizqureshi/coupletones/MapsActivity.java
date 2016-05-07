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
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.MotionEvent;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.app.ProgressDialog;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import org.json.JSONException;

import java.util.List;
import java.util.ArrayList;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;


/**
 * Main page of the map
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Marker onSearchLocationMarker;
    private ArrayAdapter mAdapter;
    private boolean update = false;

    private ImageButton mSearchButton;
    private TextView mSettingTitle, mAddPartner, mDeletePartner, mSignOut;
    private EditText mSearchView;
    private ListView mListView;

    private BottomBar mBottomBar;

    private User currUser;
    private GoogleApiClient mGoogleApiClient;

    DataManager manager;

    //ArrayList for storing the locations during runtime
    private ArrayList<String> nameOfLocationsList = new ArrayList<>();

    private String temp;
    private Boolean exists = false;


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        this.fillListFromUser();

        currUser = makeUser( );
        manager = new DataManager( currUser );
        manager.setUp();

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
                currUser = manager.updateUser();
                fillListFromUser();
                mAdapter.notifyDataSetChanged();
                dialog.hide();
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
        mSignOut = (TextView) findViewById(R.id.signOut);
        mAddPartner.setVisibility(View.GONE);
        mDeletePartner.setVisibility(View.GONE);

        /*
            Add partner listener
         */
        mAddPartner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddPartnerDialog();
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
                signOut();
            }
        });

        if (currUser.hasPartner()) {
            mAddPartner.setVisibility(View.GONE);
            mDeletePartner.setVisibility(View.VISIBLE);
        } else{
            mAddPartner.setVisibility(View.VISIBLE);
            mDeletePartner.setVisibility(View.GONE);
        }

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
            Setting up the bottom tab bar
         */
        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.setActiveTabColor("#009688");
        mBottomBar.setItemsFromMenu(R.menu.bottombar_menu, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.bottomBarItemOne) {
                    // The user selected item number one.
                    mSettingTitle.setVisibility(View.VISIBLE);
                    mSignOut.setVisibility(View.VISIBLE);
                    mSearchView.setVisibility(View.GONE);
                    mSearchButton.setVisibility(View.GONE);
                    mListView.setVisibility(View.GONE);
                    findViewById(R.id.map).setVisibility(View.GONE);

                }
                else if (menuItemId == R.id.bottomBarItemTwo) {
                    // The user selected item number two.
                    mSettingTitle.setVisibility(View.GONE);
                    mAddPartner.setVisibility(View.GONE);
                    mDeletePartner.setVisibility(View.GONE);
                    mSignOut.setVisibility(View.GONE);
                    mSearchView.setVisibility(View.VISIBLE);
                    mSearchButton.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.GONE);
                    findViewById(R.id.map).setVisibility(View.VISIBLE);
                }
                else if (menuItemId == R.id.bottomBarItemThree) {
                    // The user selected item number three.
                    mSettingTitle.setVisibility(View.GONE);
                    mAddPartner.setVisibility(View.GONE);
                    mDeletePartner.setVisibility(View.GONE);
                    mSignOut.setVisibility(View.GONE);
                    mSearchView.setVisibility(View.GONE);
                    mSearchButton.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
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


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * Create the map and relevant iterms
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Initialize the map and the camera

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(32.8800604, -117.2362022), 13));
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
            ArrayList<String> locNames = new ArrayList<>();
            @Override
            public void onLocationChanged(Location location) {
                /*
                    Implementing favorite location(s) detection
                 */
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));
                /*
                    A loop go through the list of Fav. Loc. to see if closed to and if visiting
                    "usersFavLocsList" is the name of the list which storing the user's Fav. Locs. - can be modify later
                 */

                for(int i = 0; i < currUser.getLocations().locations.size(); i++) {

                    //Checking if user is visiting.
                    // "< 161" means if the distance between user and the Fav. Loc. is less than 161 meters which is 0.1 mile
                    if (distanceBetween(new LatLng(location.getLatitude(), location.getLongitude()),
                            currUser.getLocations().get(i).getLocation()) <= 161) { //deleted .locations.
                        for(int j = 0; j < locNames.size(); j++) {
                            if (locNames.get(j).compareTo(currUser.getLocations().locations.get(i).getName()) != 0) {
                                //TODO: Send the notification
                                locNames.add(currUser.getLocations().locations.get(i).getName());
                            }
                        }
                    }
                    //Detect if the user has left
                    for(int k = 0; k < locNames.size(); k++) {
                        if (distanceBetween(new LatLng(location.getLatitude(), location.getLongitude()),
                                currUser.getLocations().searchLoc(locNames.get(k)).getLocation()) > 161) {
                            locNames.remove(k);
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


//    protected void showAddPartnerialog() {
//
//        AlertDialog.Builder alert = new AlertDialog.Builder(MapsActivity.this);
//
//        alert.setTitle("Pair a partner");
//        alert.setMessage("Please enter your partner's email:");
//
//        // Set an EditText view to get user input
//        final EditText input = new EditText(MapsActivity.this);
//        alert.setView(input);
//
//        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                if(!input.getText().toString().equals("")) {
//                    if (manager.findPartnerEmail(input.getText().toString()))
//                        currUser.setPartnerEmail(input.getText().toString());
//                    else {
//                        Toast.makeText(getApplicationContext(), (CharSequence) "Partner does not have CoupleTones!", Toast.LENGTH_LONG).show();
//                        return;
//                    }
//
//                } else {
//                    currUser.setPartnerEmail("--");
//                }
//
//                manager.updatePartnerEmail(currUser);
//
//                manager.fetchPartnerId();
//                setPartnerProgressDialog();
//                Toast.makeText( getApplicationContext(), (CharSequence) "Pairing with: " + currUser.getPartnerEmail(), Toast.LENGTH_LONG ).show( );
//            }
//        });
//
//        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                // Canceled.
//            }
//        });
//
//        alert.show();
//    }


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

        AlertDialog.Builder alert = new AlertDialog.Builder(MapsActivity.this);

        alert.setTitle("Adding Favorite Location");
        alert.setMessage("Please assign a name of the location:");

        // Set an EditText view to get user input
        final EditText input = new EditText(MapsActivity.this);
        alert.setView(input);

        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                if (isAlphaNumeric(input.getText().toString())) {
                    boolean duplicateNameOrLocation = false;
                    String locName = input.getText().toString();

                    for (int i = 0; i < currUser.getLocations().size(); i++) {
                    /*
                        No duplicate name
                        And not allowing adding location that close to an existing location by 0.2 mile which is 362 meters
                     */

                        if ((locName.compareTo(currUser.getLocations().get(i).getName()) == 0)
                                || (distanceBetween(latLng, currUser.getLocations().get(i).getLocation()) < 362)) {

                            duplicateNameOrLocation = true;
                        }
                    }
                    //duplicateNAmeOrLocation = currUser.getLocations().isValid( locName, latLng );
                    if (!duplicateNameOrLocation) {
                        //Adding the locations here
                        Toast.makeText(getApplicationContext(), "Location Added", Toast.LENGTH_SHORT).show();

                        currUser.getLocations().add(new FavLocation(locName, latLng));
                        try {
                            manager.updateLocations( currUser, currUser.getLocations().getList() );
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
                    Toast.makeText(getApplicationContext(), (CharSequence) "Input invalid", Toast.LENGTH_LONG).show();
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
        if (currUser.getLocations().size() != 0){
            for (int i = 0; i < currUser.getLocations().size(); i++) {
                nameOfLocationsList.add(currUser.getLocations().get(i).getName());
            }
        }

    }
    /*
        Function for initializing the user
     */

    private User makeUser( ) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, null /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        return new User( opr.get().getSignInAccount() );

    }
    /*
        Function for signing out
     */
    private void signOut( ) {
        //Toast.makeText( getApplicationContext(), (CharSequence) "Entering func", Toast.LENGTH_LONG ).show( );

        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        Intent intent = new Intent(MapsActivity.this, LoginActivity.class);
                        startActivity(intent);
                        // [END_EXCLUDE]
                    }
                });


        Toast.makeText( getApplicationContext(), (CharSequence) "You are signed out!", Toast.LENGTH_LONG ).show( );

    }
    /*
        Dialog for Adding the partner
     */
    protected void showAddPartnerDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(MapsActivity.this);

        alert.setTitle("Pair a partner");
        alert.setMessage("Please enter your partner's email:");

        // Set an EditText view to get user input
        final EditText input = new EditText(MapsActivity.this);
        alert.setView(input);
        final String partnerEmail = input.getText().toString();

        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                exists = manager.findPartnerEmail(partnerEmail);
                findPartnerProgressDialog();
                if(exists) {
                    currUser.setHasPartner(true);
                } else {
                    currUser.setHasPartner(false);
                }


                manager.updatePartnerEmail(currUser);

                manager.fetchPartnerId();
                setPartnerProgressDialog();
                Toast.makeText( getApplicationContext(), (CharSequence) "Pairing with: " + currUser.getPartnerEmail(), Toast.LENGTH_LONG ).show( );
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

    void findPartnerProgressDialog() {
        final ProgressDialog dialogP=new ProgressDialog(this);
        dialogP.setMessage("Searching");
        dialogP.setCancelable(false);
        dialogP.setInverseBackgroundForced(false);
        dialogP.show();

        final Handler handlerP = new Handler();
        handlerP.postDelayed(new Runnable() {
            @Override
            public void run() {

                dialogP.hide();
            }
        }, 500);
    }


    void setPartnerProgressDialog() {
        final ProgressDialog dialogP=new ProgressDialog(this);
        dialogP.setMessage("Initializing data");
        dialogP.setCancelable(false);
        dialogP.setInverseBackgroundForced(false);
        dialogP.show();

        final Handler handlerP = new Handler();
        handlerP.postDelayed(new Runnable() {
            @Override
            public void run() {
                currUser.setPartnerId(manager.getPartnerId( ));
                Toast.makeText( getApplicationContext(), (CharSequence) "Pairing with: " + currUser.getPartnerId(), Toast.LENGTH_LONG ).show( );
                dialogP.hide();
            }
        }, 500);
    }



    /*
        Dialog for deleting the partner
     */
    protected void showDeletePartnerDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(MapsActivity.this);

        alert.setTitle("Confirm delete Partner");
        alert.setMessage("Are you sure want to DELETE your partner?");


        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {

                if (!currUser.hasPartner()) {
                    Toast.makeText( getApplicationContext(), (CharSequence) "Not currently paired with anyone!", Toast.LENGTH_LONG ).show( );
                }
                Toast.makeText( getApplicationContext(), (CharSequence) "Removed partner", Toast.LENGTH_LONG ).show( );
                currUser.removePartner();
                manager.updatePartnerEmail(currUser);

                mAddPartner.setVisibility(View.VISIBLE);
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
    /*
        Checking if the input is alphanumeric
     */
    protected boolean isAlphaNumeric(String s){
        String pattern= "^[a-zA-Z0-9]+$";
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
        Adapter for dynamically constructing the listview
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
                    currUser.getLocations().remove(locName);
                    try {
                        manager.updateLocations( currUser, currUser.getLocations().getList() );
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




}
/**
 * if you need partner Id. First call:
 * manager.fetchPartnerId( );
 *
 * then copy past the following:
 *
 *
 final ProgressDialog dialogP=new ProgressDialog(this);
 dialogP.setMessage("Initializing data");
 dialogP.setCancelable(false);
 dialogP.setInverseBackgroundForced(false);
 dialogP.show();

 final Handler handlerP = new Handler();
 handlerP.postDelayed(new Runnable() {
@Override
public void run() {
YOUR_VARIABLE_TO_STORE_ID_HERE = manager.getPartnerId( );
dialogP.hide();
}
}, 2000);
 */