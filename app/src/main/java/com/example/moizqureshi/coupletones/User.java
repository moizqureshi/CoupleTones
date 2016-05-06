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
    private String partnerId;
    private Locations locations;

    public User( ) {
        email = null;
        partnerEmail = new String("--");
        partnerId = null;
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

    public String getPartnerId() {
        return partnerId;
    }

    public Locations getLocations( ) {
        return locations;
    }

<<<<<<< HEAD
    public void setPartnerEmail( String newPartnerEmail ) {
        if (newPartnerEmail == null) {
            partnerEmail ="--";
            return;
        }
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

    public void setPartnerId(String newPartnerId) {
        if (newPartnerId == null){
            partnerId = "--";
            return;
        }
        partnerId = newPartnerId;

    }

    public boolean removePartner( ) {
        if( !hasPartner( ) )
            return false;

        partnerEmail = new String("--");
        partnerId = new String("--");
        return true;
    }

    public boolean hasPartner( ) {
        return !(partnerEmail.equals("--"));
    }

}