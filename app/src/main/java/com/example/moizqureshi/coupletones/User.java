
package com.example.moizqureshi.coupletones;

/**
 * Created by moizqureshi on 5/3/16.
 */
import android.app.Activity;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class User extends Activity {
    private String email;
    private String partnerEmail;
    private String partnerId;
    private Locations locations;
    private boolean hasPartner;

    public User( ) {
        email = null;
        partnerEmail = new String("--");
        partnerId = null;
        locations = new Locations( );
        hasPartner = false;
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

    public String getPartnerId() {
        return partnerId;
    }

    public Locations getLocations( ) {
        return locations;
    }

    public void setEmail( String newEmail )
    {
        email = newEmail;
    }

    public void setPartnerEmail( String newPartnerEmail ) {
        if (newPartnerEmail == null) {
            partnerEmail ="--";
            return;
        }
        partnerEmail = newPartnerEmail;
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
        return hasPartner;
    }

    public void setHasPartner(boolean partnerExist){
        hasPartner = partnerExist;
    }

}