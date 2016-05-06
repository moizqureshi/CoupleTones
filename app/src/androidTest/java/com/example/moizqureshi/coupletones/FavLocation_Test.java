package com.example.moizqureshi.coupletones;
import android.location.Location;
/**
 * Created by Alina on 5/3/2016.
 */
public class FavLocation_Test {
    String name;
    Location location;

    FavLocation_Test( String newName, Location newLocation ) {
        name = newName;
        location = newLocation;
    }

    public Location getLocation( ) {
        return location;
    }

    public String getName( ) {
        return name;
    }

    public void setLocation( Location newLocation ) {
        location = newLocation;
    }

    public void setName( String newName ) {
        name = newName;
    }

    public String toString( ) {
        return name.length() + " - " + name + longtitude() + latitude();
    }

    private String longtitude( ) {
        return " - " + location.getLongitude();
    }

    private String latitude( ){
        return " - " + getLocation().getLatitude();
    }
}
