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
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

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

    private ImageButton mSearchButton;
    private Button mMenuButton1, mMenuButton2, mMenuButton3;
    private EditText mSearchView;
    private ListView mListView;

    private BottomBar mBottomBar;


    private User currUser;
    private GoogleApiClient mGoogleApiClient;




    private ArrayList<String> nameOfLocationsList = new ArrayList<>();

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        currUser = makeUser( );

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.fillListFromUser();
        /*
            Setting up the map fragment
         */
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        /*
            Setting up the menu tab buttons
         */
        mMenuButton1 = (Button) findViewById(R.id.button1);
        mMenuButton2 = (Button) findViewById(R.id.button2);
        mMenuButton3 = (Button) findViewById(R.id.button3);

        /*
            Signing out listener
         */
        mMenuButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            Setting up the bottom tab bar
         */
        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.setActiveTabColor("#009688");
        mBottomBar.setItemsFromMenu(R.menu.bottombar_menu, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.bottomBarItemOne) {
                    // The user selected item number one.
                    mMenuButton1.setVisibility(View.VISIBLE);
                    mMenuButton2.setVisibility(View.VISIBLE);
                    mMenuButton3.setVisibility(View.VISIBLE);
                    mSearchView.setVisibility(View.GONE);
                    mSearchButton.setVisibility(View.GONE);
                    mListView.setVisibility(View.GONE);
                    findViewById(R.id.map).setVisibility(View.GONE);

                }
                else if (menuItemId == R.id.bottomBarItemTwo) {
                    // The user selected item number two.
                    mMenuButton1.setVisibility(View.GONE);
                    mMenuButton2.setVisibility(View.GONE);
                    mMenuButton3.setVisibility(View.GONE);
                    mSearchView.setVisibility(View.VISIBLE);
                    mSearchButton.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.GONE);
                    findViewById(R.id.map).setVisibility(View.VISIBLE);
                }
                else if (menuItemId == R.id.bottomBarItemThree) {
                    // The user selected item number three.
                    mMenuButton1.setVisibility(View.GONE);
                    mMenuButton2.setVisibility(View.GONE);
                    mMenuButton3.setVisibility(View.GONE);
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
            String locName = null;
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
                    if (distanceBetween(new LatLng(location.getLatitude(),
                            location.getLongitude()), currUser.getLocations().locations.get(i).getLocation()) <= 161) {
                        if(locName.compareTo(currUser.getLocations().locations.get(i).getName()) != 0) {
                            //TODO: Send the notification
                            locName = currUser.getLocations().locations.get(i).getName();
                        }
                    }
                    //Detect if the user has left
                    if(distanceBetween(new LatLng(location.getLatitude(),
                            location.getLongitude()), currUser.getLocations().searchLoc(locName).getLocation()) > 161)
                    {
                        locName = null;
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
                    if (addresses != null && !addresses.equals(""))
                        searchAddresses(addresses);

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


        onSearchLocationMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(String.format("%s, %s",
                address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                address.getCountryName())));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));

    }

    /*
        Helper function to compute the distance between two LatLngs, in Meters.
     */
    protected static double distanceBetween(LatLng from, LatLng to) {
        double earthRadius = 6371009;
        double dLat = toRadians(from.latitude) - toRadians(to.latitude);
        double dLng = toRadians(from.longitude) - toRadians(to.longitude);
        return ((sin((dLat) * 0.5)) * (sin((dLat) * 0.5))
                + (sin((dLng) * 0.5)) * (sin((dLng) * 0.5))
                * cos(from.latitude) * cos(to.latitude)) * earthRadius;
    }
    /*
        Popping up a dialog for user to enter the name of a Fav. Loc.
        And add that location to the list, then update everything
     */

    protected void showAddLocationDialog(final LatLng latLng) {

        AlertDialog.Builder alert = new AlertDialog.Builder(MapsActivity.this);

        alert.setTitle("Adding Favorite Location");
        alert.setMessage("Please enter a name of the location:");

        // Set an EditText view to get user input
        final EditText input = new EditText(MapsActivity.this);
        alert.setView(input);

        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                boolean duplicateNameOrLocation = false;
                String locName = input.getText().toString();

                for(int i = 0; i < currUser.getLocations().locations.size(); i++) {
                    if((locName.compareTo(currUser.getLocations().get(i).getName()) == 0)
                            || (distanceBetween(latLng, currUser.getLocations().get(i).location)) < 322)
                        duplicateNameOrLocation = true;
                }
                if(!duplicateNameOrLocation) {
                    //Adding the locations here

                    currUser.getLocations().add(new FavLocation(locName, latLng));
                    //TODO: Need to save the info on server

                    //Updating local list
                    nameOfLocationsList.add(locName);
                    mAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText( getApplicationContext(), (CharSequence) "You already have the same name or same location", Toast.LENGTH_LONG ).show( );
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

    public void fillListFromUser() {
        for (int i = 0; i < currUser.getLocations().locations.size(); i++) {
            nameOfLocationsList.add(currUser.getLocations().locations.get(i).getName());
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        mBottomBar.onSaveInstanceState(outState);
    }

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
                    //TODO: Need to update the server

                    //Removing from the local
                    nameOfLocationsList.remove(position);
                    mAdapter.notifyDataSetChanged();

                    Toast.makeText(getContext(), "Button was clicked for list item " + (position + 1), Toast.LENGTH_SHORT).show();
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
