package com.example.moizqureshi.coupletones;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Alina on 5/3/2016.
 */
public class FavLocation {
    String name;
    LatLng location;

    FavLocation( String newName, LatLng newLocation ) {
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
        return name.length() + name + longtitude() + latitude();
    }

    private String longtitude( ) {
        return " * " + location.longitude;
    }

    private String latitude( ){
        return " * " + location.latitude;
    }
}
