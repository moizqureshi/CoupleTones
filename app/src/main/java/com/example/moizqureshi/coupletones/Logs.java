package com.example.moizqureshi.coupletones;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Created by Benjamin on 5/21/2016.
 */
public class Logs {
    ArrayList<LocHist> history;

    public Logs( ) {
        history = new ArrayList<>();
    }

    public LocHist get(int index) {
        return history.get(index);
    }

    public LocHist get() {
        return history.get(0);
    }

    public void add( LocHist newLog ) {
        history.add(newLog);
    }

    public void remove( ) throws NoSuchElementException {
        if( history.isEmpty() )
            throw new NoSuchElementException("No element in history to delete!");

        history.remove(0);
    }

    public LocHist search( String name ) {
        for( int i = 0; i < history.size(); i++ ) {
            if( history.get(i).getName().equals( name ) ) {
                return history.get(i);
            }
        }

        return null;
    }

    public int size( ) {
        return history.size();
    }

    public JSONArray getList( ) throws JSONException {
        JSONArray history = new JSONArray();

        for( int i = 0; i < this.history.size(); i++ ) {
            history.put( this.history.get(i).toString() );
        }

        return history;
    }

    public void update( JSONArray dbHistory ) throws JSONException {
        if( dbHistory == null) {
            history = new ArrayList<>(0);
            return;
        }

        ArrayList<LocHist> newLocations = new ArrayList<>();

        for(int i = 0; i < dbHistory.length(); i++) {
            newLocations.add( new LocHist( dbHistory.getString(i) ) );
            //Log.d("json his:", dbHistory.getString(i));
            //Log.d("newHist:", newLocations.get(i).toString());

        }

        history = newLocations;
    }
}
