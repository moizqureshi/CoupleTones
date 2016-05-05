package com.example.moizqureshi.coupletones;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;


/**
 *  Main page of the map
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Marker onSearchLocationMarker;

    //private TextView mTapTextView;
    private Button mSearchButton;
    private EditText mSearchView;
    private ListView mListView;

    //Create the dummy list for testing, need to change later
    private ArrayList<String> dummylist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //mTapTextView = (TextView) findViewById(R.id.latlongLocation);
        mSearchButton = (Button) findViewById(R.id.searchButton);
        mSearchView = (EditText) findViewById(R.id.searchView);
        mListView = (ListView) findViewById(R.id.listView);
        this.fillListFromUser();
        mListView.setAdapter(new MyListAdapter(this, R.layout.listview_item, dummylist));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MapsActivity.this, "List item was clicked at " + (position+1), Toast.LENGTH_SHORT).show();
            }
        });
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }


    /**
     *  Create the map and relevant iterms
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
        GoogleMap.OnMapClickListener mapClickListener = new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(final LatLng latLng) {
                /*
                    Tested and Worked.
                    TODO:Need function call to create and store the favorite location.
                 */
                //mTapTextView.setText("This location is at: " + latLng);

                showPopupDialog(latLng);

            }
        };
        mMap.setOnMapClickListener(mapClickListener);

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
        return ((sin((dLat)*0.5))*(sin((dLat)*0.5))
                + (sin((dLng)*0.5))*(sin((dLng)*0.5))
                * cos(from.latitude) * cos(to.latitude)) * earthRadius;
    }
    /*
        Popping up a dialog for user to enter the name of a Fav. Loc.
     */

    protected void showPopupDialog(final LatLng latLng) {

        AlertDialog.Builder alert = new AlertDialog.Builder(MapsActivity.this);

        alert.setTitle("Adding Favorite Location");
        alert.setMessage("Please enter a name of the location:");

        // Set an EditText view to get user input
        final EditText input = new EditText(MapsActivity.this);
        alert.setView(input);

        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String locName = input.getText().toString();
                //mTapTextView.setText(locName + latLng);
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
        if(marker.equals(onSearchLocationMarker)) {
            showPopupDialog(new LatLng(marker.getPosition().latitude, marker.getPosition().longitude));
        }
        return true;
    }
    /*
        Adapter for dynamically constructing the listview
     */
    protected class MyListAdapter extends ArrayAdapter<String> {

        private int layout;
        private List<String> myObjects;
        private MyListAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            myObjects = objects;
            layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewHolder = null;
            if(convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.title = (TextView) convertView.findViewById(R.id.list_item_text);
                viewHolder.button = (Button) convertView.findViewById(R.id.list_item_botton);
                convertView.setTag(viewHolder);
            }
            mainViewHolder = (ViewHolder) convertView.getTag();
            mainViewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Button was clicked for list item " + (position+1), Toast.LENGTH_SHORT).show();
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
    public void fillListFromUser() {
        for (int i = 0; i < 25; i++) {
            dummylist.add("This is the" + (i+1) + " item");
        }
    }

}
