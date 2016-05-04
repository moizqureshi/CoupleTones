package com.example.moizqureshi.coupletones;

/**
 * Created by moizqureshi on 5/3/16.
 */
import android.net.Uri;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class User {
    GoogleSignInAccount gAccount;
    String username;
    Partner partner;
    Locations locations;

    User( GoogleSignInAccount argAcc ) {
        gAccount = argAcc;
        username = argAcc.getDisplayName();
        partner = null;
        locations = new Locations( );
    }

    public GoogleSignInAccount getAccount( ) {
        return gAccount;
    }

    public String getUsername( ) {
        return username;
    }

    public Partner getPartner( ) {
        return partner;
    }

    public Locations getLocations( ) {
        return locations;
    }

    public void setUsername( String newUsername ) {
        username = newUsername;
    }

    public void setPartner( Partner newPartner) {
        partner = newPartner;
    }

    public boolean removePartner( ) {
        if( !hasPartner( ) )
            return false;

        partner = null;
        return true;
    }

    public boolean hasPartner( ) {
        return !(partner == null);
    }

    public boolean saveInfo( ) {
        /*
            HANDLES SHAREDPREFERENCE
         */
        return false;
    }
}
