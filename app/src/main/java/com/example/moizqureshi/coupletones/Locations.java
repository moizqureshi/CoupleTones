package com.example.moizqureshi.coupletones;

import java.util.ArrayList;

import android.app.Activity;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Alina on 5/3/2016.
 */
public class Locations extends Activity {
    public ArrayList<FavLocation> locations;

    public Locations(int size ) {
        locations = new ArrayList<>(size);
    }

    public Locations( ) {
        locations = new ArrayList<>(0);
    }

    public Locations(ArrayList<FavLocation> newLocations ) {
        locations = newLocations;
    }

    public void add( FavLocation newLocation ) {
        locations.add( newLocation );
    }

    public FavLocation get( int index ) {
        return locations.get(index);
    }

    public boolean remove( int index ) {
        if( index >= locations.size() )
            return false;
        locations.remove( index );
        return true;
    }

    public boolean remove( String name ) {
        int position = searchIdx( name );
        if( position == -1 )
            return false;

        locations.remove( position );
        return true;
    }


    public int searchIdx( String name ) {
        for(int i = 0; i < locations.size( ); i++ ) {
            if( locations.get( i ).getName( ).toLowerCase().equals( name.toLowerCase() ) )
                return i;
        }

        return -1;
    }

    public FavLocation searchLoc( String name ) {
        int position = searchIdx( name );
        if( position == -1 )
            return null;

        return locations.get( position );
    }

    public JSONArray getList( ) throws JSONException {
        JSONArray locations = new JSONArray();

        for( int i = 0; i < this.locations.size(); i++ ) {
            locations.put( this.locations.get(i).toString() );
        }

        return locations;
    }

    public void update( JSONArray dbLocations ) throws JSONException {
        if( dbLocations == null) {
            locations = new ArrayList<>(0);
            return;
        }

        ArrayList<FavLocation> newLocations = new ArrayList<>();

        for(int i = 0; i < dbLocations.length(); i++) {
            newLocations.add( new FavLocation( dbLocations.getString(i) ) );
        }

        locations = newLocations;
    }

    public int size( ) {
        return locations.size();
    }

}