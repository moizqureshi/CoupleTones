package Locations;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import java.lang.String;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.google.gson.Gson;

/**
 * Created by Alina on 5/1/2016.
 */
public class Locations {
    private ArrayList
        public static final String PREFS_NAME = "LOCATION_APP";
        public static final String FAVORITES = "Location_Favorite";

        public Locations() {}

        // This four methods are used for maintaining favorites.
        public void saveFavoriteLocations(Context context, List<String> favorites) {
            SharedPreferences settings;
            SharedPreferences.Editor editor;

            settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            editor = settings.edit();

            Gson gson = new Gson();
            String jsonFavorites = gson.toJson(favorites);

            editor.putString(FAVORITES, jsonFavorites);

            editor.apply();
        }

        public void addFavoriteLocation(Context context, String name, Location location) {
            List<String> favorites = getFavoriteLocations(context);
            if (favorites == null)
                favorites = new ArrayList<String>();
            String nameAndLocation = name + "," + location.getLatitude() + "," +
                    location.getLongitude();
            favorites.add(nameAndLocation);
            saveFavoriteLocations(context, favorites);
        }

        public void removeFavoriteLocation(Context context, String name, Location location) {
            ArrayList<String> favorites = getFavoriteLocations(context);
            if (favorites != null) {
                String locationToRemove = name + "," + location.getLatitude() + "," +
                        location.getLongitude();
                favorites.remove(locationToRemove);
                saveFavoriteLocations(context, favorites);
            }
        }

        public ArrayList<String> getFavoriteLocations(Context context) {
            SharedPreferences settings;
            List<String> favorites;

            settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

            if (settings.contains(FAVORITES)) {
                String jsonFavorites = settings.getString(FAVORITES, null);
                Gson gson = new Gson();
                String[] favoriteItems = gson.fromJson(jsonFavorites, String[].class);

                favorites = Arrays.asList(favoriteItems);
                favorites = new ArrayList<String>(favorites);
            } else
                return null;

            return (ArrayList<String>) favorites;
        }

}
