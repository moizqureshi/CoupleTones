
package com.example.moizqureshi.coupletones;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Alina on 5/3/2016.
 */
public class FavLocation {
    private String name;
    private LatLng location;
    private int vibeIdx;
    private int soundIdx;

    public FavLocation( String fullString ) {
        name = fullString.substring( 0, fullString.indexOf('*') - 1 );
        fullString = fullString.substring( fullString.indexOf('*') + 2 );

        double lat;
        double lng;
        int vib;
        int snd;

        lat = Double.parseDouble( fullString.substring(0, fullString.indexOf('*') - 1) );

        lng = Double.parseDouble( fullString.substring( fullString.indexOf('*') + 2, fullString.indexOf('&') - 1 ) );

        vib = Integer.parseInt( fullString.substring( fullString.indexOf('&') + 2, fullString.indexOf('%') -1 ) );

        snd = Integer.parseInt( fullString.substring( fullString.indexOf('%') + 2) );

        location = new LatLng( lat, lng );

        vibeIdx = vib;
        soundIdx = snd;
    }

    public FavLocation( String newName, LatLng newLocation ) {
        name = newName;
        location = newLocation;
        vibeIdx = 0;
        soundIdx = 0;
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
    }

    private String longtitude( ) {
        return " * " + location.longitude;
    }

    private String latitude( ) {
        return " * " + location.latitude;
    }

    private String vibe() {
        return " & " + vibeIdx;
    }

    private String sound() {
        return " % " + soundIdx;
    }

    public int getVibeIdx( ) {
        return vibeIdx;
    }

    public int getSoundIdx( ) {
        return soundIdx;
    }

    public void setVibeIdx( int idx ) {
        vibeIdx = idx;
    }

    public void setSoundIdx( int idx ) {
        soundIdx = idx;
    }
}