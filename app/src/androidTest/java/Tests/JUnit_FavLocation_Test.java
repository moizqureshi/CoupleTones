package Tests;


import android.location.Location;
import android.test.ActivityInstrumentationTestCase2;

import com.example.moizqureshi.coupletones.FavLocation;
import com.example.moizqureshi.coupletones.Locations;
import com.example.moizqureshi.coupletones.LoginActivity;
import com.example.moizqureshi.coupletones.MapsActivity;
import com.example.moizqureshi.coupletones.User;
import com.google.android.gms.maps.model.LatLng;
import android.test.InstrumentationTestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;


/**
 * Created by gcarb on 5/5/2016.
 */
public class JUnit_FavLocation_Test extends InstrumentationTestCase

{

    private FavLocation loc1;
    private FavLocation loc2;

    private LatLng location = new LatLng( 100, -100 );

    @Before
    public void setUp()
    {

        loc1 = new FavLocation("Location 1 * 50 * -50");
        loc2 = new FavLocation("Location 2", location);

    }

    @After
    public void tearDown()
    {
        loc1 = null;
        loc2 = null;
    }

    @Test
    public void testGetLocations()
    {
        LatLng location1 = new LatLng( 50, -50);
        LatLng location2 = new LatLng( 100, -100);

        assertEquals( location1, loc1.getLocation());
        assertEquals( location2, loc2.getLocation());
    }

    @Test
    public void testGetName()
    {
        assertEquals("Location 1", loc1.getName());
        assertEquals("Location 2", loc2.getName());
    }

    @Test
    public void testSetLocation()
    {
        LatLng newLocation = new LatLng(30, 30);
        loc1.setLocation( newLocation );

        assertEquals( newLocation, loc1.getLocation());
    }

    @Test
    public void testSetName()
    {
        loc2.setName("New Name");
        assertEquals("New Name", loc2.getName());
    }

    @Test
    public void testToSTring()
    {
        assertEquals("Location 1 * 50.0 * -50.0", loc1.toString() );
        assertEquals("Location 2 * 90.0 * -100.0", loc2.toString());
    }
}
