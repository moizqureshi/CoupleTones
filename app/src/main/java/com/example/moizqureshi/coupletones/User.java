package com.example.moizqureshi.coupletones;

/**
 * Created by moizqureshi on 5/3/16.
 */
import android.net.Uri;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class User {
    GoogleSignInAccount gAccount;
    String username;
    Locations locations;
    //Temporary used, needed to delete later
    String partnerAccount;

    User( GoogleSignInAccount argAcc ) {
        gAccount = argAcc;
        username = argAcc.getDisplayName();
        locations = new Locations( );
    }

    public GoogleSignInAccount getAccount( ) {
        return gAccount;
    }

    public String getUsername( ) {
        return username;
    }

    public Locations getLocations( ) {
        return locations;
    }

    //Temporary used, needed to delete later
    public String getPartnerAccount() { return partnerAccount;}
    //Temporary used, needed to delete later
    public void setPartnerAccount(String account) { partnerAccount = account;}

    public void setUsername( String newUsername ) {
        username = newUsername;
    }

    public boolean saveInfo( ) {
        /*
            HANDLES SHAREDPREFERENCE
         */
        return false;
    }
}
