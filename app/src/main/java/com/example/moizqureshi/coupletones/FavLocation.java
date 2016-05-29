
package com.example.moizqureshi.coupletones;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Alina on 5/3/2016.
 */
public class FavLocation {
    static final int ARRIVE_INDEX = 0;
    static final int LEAVE_INDEX = 1;

    private String name;
    private LatLng location;
    private int vibeIdx[] = new int[2];
    private int soundIdx[] = new int[2];

    public FavLocation( String fullString ) {
        name = fullString.substring( 0, fullString.indexOf('*') - 1 );
        fullString = fullString.substring( fullString.indexOf('*') + 2 );

        double lat;
        double lng;
        int firstInt;
        int secondInt;

        lat = Double.parseDouble( fullString.substring(0, fullString.indexOf('*') - 1) );

        lng = Double.parseDouble( fullString.substring( fullString.indexOf('*') + 2, fullString.indexOf('&') - 1 ) );

        firstInt = Integer.parseInt( fullString.substring( fullString.indexOf('&') + 2, fullString.indexOf('[') - 1 ) );

        secondInt = Integer.parseInt( fullString.substring( fullString.indexOf('[') + 2, fullString.indexOf('%') - 1) );

        location = new LatLng( lat, lng );

        vibeIdx[0] = firstInt;
        vibeIdx[1] = secondInt;

        firstInt = Integer.parseInt( fullString.substring( fullString.indexOf('%') + 2, fullString.indexOf(']') -1 ) );
        secondInt = Integer.parseInt( fullString.substring( fullString.indexOf(']') + 2) );

        soundIdx[0] = firstInt;
        soundIdx[1] = secondInt;
    }

    public FavLocation( String newName, LatLng newLocation ) {
        name = newName;
        location = newLocation;
        vibeIdx[0] = 0;
        vibeIdx[1] = 0;
        soundIdx[0] = 0;
        soundIdx[1] = 0;
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
        return name + latitude() + longtitude() + vibe() + sound();
    }

    private String longtitude( ) {
        return " * " + location.longitude;
    }

    private String latitude( ) {
        return " * " + location.latitude;
    }

    private String vibe() {
        return " & " + vibeIdx[0] + " [ " + vibeIdx[1];
    }

    private String sound() {
        return " % " + soundIdx[0] + " ] " + soundIdx[1];
    }

    public int getVibeIdx(int index) {
        return vibeIdx[index];
    }

    public int getSoundIdx(int index) {
        return soundIdx[index];
    }

    public void setVibeIdx(int index, int idx ) {
        vibeIdx[index] = idx;
    }

    public void setSoundIdx(int index, int idx ) {
        soundIdx[index] = idx;
    }
}