package Tests;


import com.example.moizqureshi.coupletones.FavLocation;
import com.example.moizqureshi.coupletones.User;
import com.google.android.gms.maps.model.LatLng;
import android.test.InstrumentationTestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * Created by gcarb on 5/5/2016.
 */
public class JUnit_User_Test extends InstrumentationTestCase

{

    private User user;
    private String email = "myemail@gmail.com";
    private String partnerEmail = "partneremail@gmail.com";
    private String partnerId = "12345";
    private LatLng location= new LatLng( 134, 38 );
    private FavLocation favLocation = new FavLocation("Location 1", location );
    private boolean partner = true;


    @Before
    public void setUp()
    {
        user = new User();
    }

    @After
    public void tearDown()
    {
        user = null;
    }

    @Test
    public void testGetEmail()
    {
        assertEquals( null, user.getEmail());
    }

    @Test
    public void testGetUPartnerEmail()
    {
        assertEquals( "--", user.getPartnerEmail() );
    }

    @Test
    public void testGetPartnerId()
    {
        assertEquals( null, user.getPartnerId());
    }

    @Test
    public void testGetLocations()
    {
        assertEquals( 0, user.getLocations().size());
    }

    @Test
    public void testHasPartener()
    {
        assertEquals(false, user.hasPartner());
    }

    @Test
    public void testSetPartnerEmail()
    {
        user.setPartnerEmail( partnerEmail );
        assertEquals( partnerEmail, user.getPartnerEmail());
    }

    @Test
    public void testSetPartnerId()
    {
        user.setPartnerId(partnerId);
        assertEquals( partnerId, user.getPartnerId());
    }



    @Test
    public void testSetPartnerEmailEmpty()
    {
        user.setPartnerEmail( "--" );
        assertTrue( !user.hasPartner() );
    }

    @Test
    public void testGetLocation()
    {
        user.getLocations().add( favLocation );
        assertEquals( 1, user.getLocations().size());
        assertEquals( "Location 1", user.getLocations().searchLoc("Location 1").getName());
    }

    @Test
    public void testRemovePartner()
    {
        //Inserting new Partner
        user.setPartnerEmail( partnerEmail );
        user.setPartnerId( partnerId );
        assertTrue( user.hasPartner() );

        //Deleting new partner
        user.removePartner();
        assertTrue( !user.hasPartner() );

    }
}
