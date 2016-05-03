package com.example.moizqureshi.coupletones;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by moizqureshi on 5/2/16.
 */

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;




public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView mTapTextView;
    private Button mSearchButton;
    private EditText mSearchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mTapTextView = (TextView) findViewById(R.id.latlongLocation);
        mSearchButton = (Button) findViewById(R.id.searchButton);
        mSearchView = (EditText) findViewById(R.id.searchView);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Initialize the map and the camera

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(32.8800604,-117.2362022), 13));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        /*
            Setup OnMapClickListener
         */
        GoogleMap.OnMapClickListener clickListener = new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                /*
                    Tested and Worked.
                    TODO:Need function call to create and store the favorite location.
                 */
                mTapTextView.setText("This location is at: " + latLng);

            }
        };
        mMap.setOnMapClickListener(clickListener);

        /*
            Track current location and update the marker.
            TODO:Need to notify user when user is closed to favorite location(s).
         */
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //mMap.clear();
                //mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("updated path"));
                /*
                    Implementing favorite location(s) detection
                 */
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));
                /*
                    A loop go through the list of Fav. Loc. to see if closed to and if visiting
                    "usersFavLocsList" is the name of the list which storing the user's Fav. Locs. - can be modify later
                 */
//                for(int i = 0; i < usersFavLocsList.size(); i++) {
//                    //Checking if user is visiting.
//                    // "< 5" means if the distance between user and the Fav. Loc. is less than 5 meters
//                    if (distanceBetween(new LatLng(location.getLatitude(),
//                            location.getLongitude()), new Latlng(usersFavLocsList.get(i).getLatitude(),
//                            usersFavLocsList.get(i).location.getLongitude())) < 5) {
//                        //TODO: Need function call to push notification to partner that user is visiting Fav. Loc
//                    }
//                    //Checking if user is closed to
//                    //"< 805" means if the distance between user and the Fav.Loc is less than 805 meters which is 0.5 mile
//                    if (distanceBetween(new LatLng(location.getLatitude(),
//                            location.getLongitude()), new Latlng(usersFavLocsList.get(i).getLatitude(),
//                            usersFavLocsList.get(i).location.getLongitude())) < 805) {
//                        //TODO: Need function call to prompt user that a Fav. Loc. is closed
//                    }
//                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
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
            TODO: Need to implement adding by address - (Can be done by adding another "add" button)
         */
        mSearchButton.setOnClickListener(new android.view.View.OnClickListener() {

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

        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng).title(String.format("%s, %s",
                address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                address.getCountryName())));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

    }
    /*
        Helper function to compute the distance between two LatLngs, in Meters.
     */
    protected static double distanceBetween(LatLng from, LatLng to) {
        double earthRadius = 6371009;
        double dLat = toRadians(from.latitude) - toRadians(to.latitude);
        double dLng = toRadians(from.longitude) - toRadians(to.longitude);
        return ((sin((dLat)*0.5))*(sin((dLat)*0.5))
                + (sin((dLng)*0.5))*(sin((dLng)*0.5))
                * cos(from.latitude) * cos(to.latitude)) * earthRadius;
    }
}
