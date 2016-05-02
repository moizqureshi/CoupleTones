import android.app.Activity;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import java.lang.String;

/**
 * Created by Alina on 5/1/2016.
 */
public class Locations extends Activity {
    public static final String FAVS_NAME = "MyFavFile";
    SharedPreferences settings = getSharedPreferences(FAVS_NAME, MODE_PRIVATE);
    SharedPreferences.Editor editor = settings.edit();
    int numOfFavs = 0;

    //@Override
    //protected void onCreate(Bundle state){
      //  super.onCreate(state);

        // Restore preferences

        //boolean silent = settings.getBoolean("silentMode", false);

        //setSilent(silent);
    //}

    protected void addFav( String name, Location location ){
        String latitude = String.valueOf(location.getLatitude());
        String longitude = String.valueOf(location.getLongitude());
        String stringToAdd = name + "," + latitude + "," + longitude;
        editor.putString(String.valueOf(numOfFavs), stringToAdd);
        numOfFavs++;
        editor.apply();
    }

    protected void remove( String name ){
        for( int key = 0; key < numOfFavs; key++){
            String favLocation = settings.getString( String.valueOf(key), "fail");
            String[] splicedFavLocation = favLocation.split(",");
            String nameFav = splicedFavLocation[0];
            if( nameFav.equals(name) ){
                editor.remove( String.valueOf(key) );
            }
        }


    }

    //@Override
    //protected void onStop(){
      //  super.onStop();

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
    //    SharedPreferences settings = getSharedPreferences(FAVS_NAME, 0);
    //    SharedPreferences.Editor editor = settings.edit();

        //editor.putBoolean("silentMode", mSilentMode);

        // Commit the edits!
    //    editor.commit();
    //}
}
