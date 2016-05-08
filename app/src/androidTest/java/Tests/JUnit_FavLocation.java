package Tests;

import android.test.ActivityInstrumentationTestCase2;

import com.example.moizqureshi.coupletones.FavLocation;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.testng.annotations.Test;

/**
 * Created by raymondarevalo on 5/7/16.
 */
public class JUnit_FavLocation extends ActivityInstrumentationTestCase2<FavLocation> {
    /* Name and location for the first constructor */
    private FavLocation favLocation_1;
    private LatLng location;
    private String fullString_1;

    /* Name and location for second constructor */
    private FavLocation favLocation_2;
    private String newName;
    private LatLng newLocation;



    public JUnit_FavLocation() {
        super(FavLocation.class);

    }

    @Before
    public void setUp() {
        /* For the first constructor */
        location = new LatLng(34.062917, -118.448065);
        fullString_1 = "In-N-Out * 34.062917 * -118.448065";       /* TODO */
        favLocation_1 = new FavLocation(fullString_1);

        /* For the second constructor */
        newName = "volcano tea house";
        newLocation = new LatLng(34.039188, -118.442449); /* Lat/lng for volcano tea house */
        favLocation_2 = new FavLocation(newName, newLocation);

    }

    /* checks if getLocation gets the correct location */
    @Test
    public void test_getLocation_1() {
        /* tests if getLocation gets the location that was set in the first constructor */
        assertEquals(location, favLocation_1.getLocation());

        /* tests if getLocation gets the location waht was set in the second constructor */
        assertEquals(newLocation, favLocation_2.getLocation());
    }


    /* checks if getName gets the correct name */
    @Test
    public void test_getName_1() {
        /* tests if getName gets the name thatt was set in the first constructor */
        assertEquals("In-N-Out", favLocation_1.getName());

        /* tests if getName gets the name that was set in the second constructor */
        assertEquals("volcano tea house", favLocation_2.getName());
    }


    /* checks if setLocation actually sets the correct location */
    @Test
    public void test_setLocation_1() {
        /* sets location with setLocation. uses getLocation to check if the location was set
         * correct.
         */
        LatLng testLocation1 = new LatLng(32.881934, -117.280891);
        favLocation_1.setLocation(testLocation1);
        assertEquals(testLocation1, favLocation_1.getLocation());

        LatLng testLocation2 = new LatLng(32.881934, -117.280891);
        favLocation_2.setLocation(testLocation2);
        assertEquals(testLocation2, favLocation_2.getLocation());
    }


    /* check if setName actually sets the correct name */
    @Test
    public void test_setName_1() {
        favLocation_1.setName("KXLU");
        assertEquals("KXLU", favLocation_1.getName());

        favLocation_2.setName("NPR");
        assertEquals("NPR", favLocation_2.getName());
    }


    /* checks if toString returns the correct full string for first/second constructor */
    @Test
    public void test_toString_1() {
        assertEquals("In-N-Out * 34.062917 * -118.448065", favLocation_1.toString());

        assertEquals("volcano tea house * 34.039188 * -118.442449", favLocation_2.toString());
    }


    /* checks if latitude returns the correct latitude for first/second constructor */
    /*longitude */
    @Test
    public void test_latitude_1() {

        assertEquals(" * 34.062917", favLocation_1.latitude());

        assertEquals(" * 34.039188", favLocation_2.latitude());

    }


    /* checks if longitude returns the correct longitude for first/second constructor */
    @Test
    public void test_longitude_1() {

        assertEquals(" * -118.448065", favLocation_1.longtitude());

        assertEquals(" * -118.442449", favLocation_2.longtitude());
    }


}
