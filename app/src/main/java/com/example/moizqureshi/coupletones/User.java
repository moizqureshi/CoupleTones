package com.example.moizqureshi.coupletones;

/**
 * Created by moizqureshi on 5/3/16.
 */
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class User {
<<<<<<< HEAD
    private String email;
    private String partnerEmail;
    private Locations locations;

    public User( ) {
        email = null;
        partnerEmail = new String("--");
        locations = new Locations( );
    }
=======
    GoogleSignInAccount gAccount;
    String username;
    Locations locations;
    //Temporary used, needed to delete later
    String partnerAccount;
>>>>>>> addlocation

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

<<<<<<< HEAD
    public void setPartnerEmail( String newPartnerEmail ) {
        partnerEmail = newPartnerEmail;
=======
    //Temporary used, needed to delete later
    public String getPartnerAccount() { return partnerAccount;}
    //Temporary used, needed to delete later
    public void setPartnerAccount(String account) { partnerAccount = account;}

    public void setUsername( String newUsername ) {
        username = newUsername;
>>>>>>> addlocation
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