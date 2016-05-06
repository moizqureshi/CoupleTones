package com.example.moizqureshi.coupletones;

/**
 * Created by moizqureshi on 5/3/16.
 */
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class User {
    private String email;
    private String partnerEmail;
    private Locations locations;

    public User( ) {
        email = null;
        partnerEmail = new String("--");
        locations = new Locations( );
    }

    public User( GoogleSignInAccount newAccount ) {
        email = newAccount.getEmail();
        partnerEmail = new String("--");
        locations = new Locations( );
    }

    public String getEmail( ) {
        return email;
    }

    public String getPartnerEmail( ) {
        return partnerEmail;
    }

    public Locations getLocations( ) {
        return locations;
    }

    public void setPartnerEmail( String newPartnerEmail ) {
        partnerEmail = newPartnerEmail;
    }

    public boolean removePartner( ) {
        if( !hasPartner( ) )
            return false;

        partnerEmail = new String("--");
        return true;
    }

    public boolean hasPartner( ) {
        return !(partnerEmail.equals("--"));
    }

}