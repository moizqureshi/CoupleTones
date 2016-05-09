package Tests;

/**
 * Created by raymondarevalo on 5/8/16.
 */


import android.os.Handler;
import android.test.AndroidTestCase;

import com.example.moizqureshi.coupletones.DataManager;
import com.example.moizqureshi.coupletones.FavLocation;
import com.example.moizqureshi.coupletones.User;
import com.example.moizqureshi.coupletones.Locations;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.junit.Before;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class JUnit_DataManager extends AndroidTestCase{
    DataManager managerNew;
    DataManager managerExist;

    User newUser;
    User existUser;

    public JUnit_DataManager () {
        super();
    }

    @Before
    protected void setUp() throws Exception{
        newUser = new User( "testing@email.com", "--", new Locations( ) );
        existUser = new User( "testingWithPartner@email.com", "testing@email.com", new Locations( ) );

        managerNew = new DataManager( newUser );
        managerExist = new DataManager( existUser );

        managerExist.setUp();
    }

    @Test
    public void testSetupNewAccount( ) {
        managerNew.setUp( );        /* to get it */
        newUser.setPartnerEmail("wrong");

        final Handler newHandler = new Handler();

        newHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                newUser = managerNew.updateUser();

                assertEquals("--", newUser.getPartnerEmail()); // assertEquals("--", newuser.getPartnerEmail());
                assertEquals("testing@email.com", newUser.getEmail());
            }}, 1000);
    }

    @Test
    public void testSetupExistingAccount( ) {
        managerExist.setUp();           /* to get it */
        existUser = managerExist.updateUser();

        assertEquals("testing@email.com", existUser.getPartnerEmail() );
        assertEquals("testingWithPartner@email.com", existUser.getEmail() );
    }

    /* updatePartnerEmail */
    @Test
    public void testUpdatePartnerEmail() {
        existUser.setPartnerEmail("new@email.com");
        managerExist.updatePartnerEmail( existUser );
        existUser.setPartnerEmail("error");
        managerExist = new DataManager( existUser );
        final Handler newHandler = new Handler();

        newHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                managerExist.findPartnerEmail( existUser.getEmail() );
                assertEquals("new@email.com", managerExist.getPartnerEmail() );
            }
        }, 1000);
    }

    @Test
    public void testUpdateUser( ) {
        DataManager m = new DataManager( new User("1", "2", new Locations() ) );

        User myUser = m.updateUser();

        assertEquals("1", myUser.getEmail());
        assertEquals("2", myUser.getPartnerEmail());
    }

    @Test
    public void testUpdateLocations( ) throws JSONException {
        existUser.getLocations().add( new FavLocation( "loc", new LatLng(9, 9) ));

        managerExist.updateLocations( existUser, existUser.getLocations().getList() );
        existUser.getLocations().remove(0);

        managerExist = new DataManager( existUser );

        final Handler newHandler = new Handler();

        newHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                managerExist.setUp( );
                existUser = managerExist.updateUser();

                assertEquals("loc", existUser.getLocations().get(0).getName() );
            }
        }, 1000);

    }



}
