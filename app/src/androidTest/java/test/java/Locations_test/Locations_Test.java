package Locations_test;

import java.util.ArrayList;

import android.app.Activity;
import android.location.Location;
/**
 * Created by Alina on 5/3/2016.
 */
public class Locations_Test extends Activity {
    public ArrayList<FavLocation_Test> locations;

    public Locations_Test(int size ) {
        locations = new ArrayList<>(size);
    }

    public Locations_Test( ) {
        locations = new ArrayList<>(0);
    }

    public Locations_Test(ArrayList<FavLocation_Test> newLocations ) {
        locations = newLocations;
    }

    public void add( FavLocation_Test newLocation ) {
        locations.add( newLocation );
    }

    public FavLocation_Test get( int index ) {
        return locations.get(index);
    }

    public boolean remove( int index ) {
        if( index >= locations.size() )
            return false;
        locations.remove( index );
        return true;
    }

    public boolean remove( Location location ) {
        int position = searchIdx( location );
        if( position == -1)
            return false;

        locations.remove( position );
        return true;
    }

    public boolean remove( String name ) {
        int position = searchIdx( name );
        if( position == -1 )
            return false;

        locations.remove( position );
        return true;
    }

    public int searchIdx( Location location ) {
        for(int i = 0; i < locations.size(); i++) {
            if( locations.get( i ).getLocation().equals( location ) ) {
                return i;
            }
        }

        return -1;
    }

    public int searchIdx( String name ) {
        for(int i = 0; i < locations.size( ); i++ ) {
            if( locations.get( i ).getName( ).equals( name ) )
                return i;
        }

        return -1;
    }

    public FavLocation_Test searchLoc( Location location ) {
        int position = searchIdx( location );
        if( position == -1 )
            return null;

        return locations.get( position );
    }

    public FavLocation_Test searchLoc( String name ) {
        int position = searchIdx( name );
        if( position == -1 )
            return null;

        return locations.get( position );
    }

}
