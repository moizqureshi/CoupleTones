package Tests;

import android.location.Location;
import android.test.ActivityInstrumentationTestCase2;

import com.example.moizqureshi.coupletones.FavLocation;
import com.example.moizqureshi.coupletones.Locations;
import com.example.moizqureshi.coupletones.User;
import com.google.android.gms.maps.model.LatLng;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * Created by gcarb on 5/5/2016.
 */
public class UserTest extends ActivityInstrumentationTestCase2<User>

{

    private User user;
    private String email = "myemail@gmail.com";
    private String partnerEmail = "partneremail@gmail.com";
    private String partnerId = "12345";
    private LatLng location= new LatLng( 134, 38 );
    private FavLocation favLocation = new FavLocation("Location 1", location );
    private boolean partner = true;
    public UserTest test;

    public UserTest()
    {
        super(User.class);
    }


    @Before
    public void setUp()
    {
        user = new User();
        test = new UserTest();
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
    public void testSetUserEmail()
    {
        user.setEmail( email );
        assertEquals( email, user.getEmail() );
    }

    @Test
    public void testSetPartner()
    {
        user.setHasPartner( partner );
        assertTrue( user.hasPartner());
    }

    @Test
    public void testLocation()
    {
        user.getLocations().add( favLocation );
        assertEquals( 1, user.getLocations().size());
        assertEquals( "Location 1", user.getLocations().searchLoc("Location 1").getName());
    }
}