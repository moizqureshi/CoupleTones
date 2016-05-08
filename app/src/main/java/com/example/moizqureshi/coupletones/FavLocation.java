package com.example.moizqureshi.coupletones;
import android.app.Activity;        /* TODO ADDED */
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Alina on 5/3/2016.
 */
public class FavLocation extends Activity {
    private String name;
    private LatLng location;

    public FavLocation( String fullString ) {
        name = fullString.substring( 0, fullString.indexOf('*') - 1 );
        fullString = fullString.substring( fullString.indexOf('*') + 2 );

        double lat;
        double lng;

        lat = Double.parseDouble( fullString.substring(0, fullString.indexOf('*') - 1) );

        lng = Double.parseDouble( fullString.substring( fullString.indexOf('*') + 2 ) );

        location = new LatLng( lat, lng );
    }

    public FavLocation( String newName, LatLng newLocation ) {
        name = newName;
        location = newLocation;
    }

    public LatLng getLocation( ) {
        return location;
    }

    public String getName( ) {
        return name;
    }

    public void setLocation( LatLng newLocation ) {
        location = newLocation;
    }

    public void setName( String newName ) {
        name = newName;
    }

    public String toString( ) {
        return name + latitude() + longtitude();
    } /*  */

    public String longtitude( ) {
        return " * " + location.longitude;
    } /* TODO */

    public String latitude( ) {
        return " * " + location.latitude;
    }   /* TODO */
}
