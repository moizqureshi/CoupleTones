package tests;

import android.test.ActivityInstrumentationTestCase2;

import Locations_test.FavLocation_Test;
import Locations_test.Locations_Test;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import static org.junit.Assert.*;

/**
 * Created by Alina on 5/5/2016.
 */
public class JUnit_Locations_Test extends ActivityInstrumentationTestCase2<Locations_Test> {
    Locations_Test emptyLocations;
    Locations_Test testTwo;
    LatLng firstLocation;
    LatLng secondLocation;
    LatLng thirdLocation;
    FavLocation_Test first;
    FavLocation_Test second;
    FavLocation_Test third;
    ArrayList<FavLocation_Test> testThreeParam;

    public JUnit_Locations_Test(){
        super(Locations_Test.class);
    }

    @Before
    public void setUp(){

        emptyLocations = new Locations_Test();

        firstLocation = new LatLng(32.637497, -116.985094);
        secondLocation = new LatLng(32.738728, -117.121994);
        thirdLocation = new LatLng(34.031667, -118.330635);


        first = new FavLocation_Test("Alina's House", firstLocation);
        second = new FavLocation_Test("Alina's Old House", secondLocation);
        third = new FavLocation_Test("Raymond's House", thirdLocation);


        testThreeParam = new ArrayList<>();
        testThreeParam.add(first);
        testThreeParam.add(second);
        testThreeParam.add(third);
        testTwo = new Locations_Test( testThreeParam );
    }



    @Test
    public void testAdd(){
        emptyLocations.add(first);
        assertEquals( emptyLocations.locations.size(), 1);
        testTwo.add(first);
        assertEquals(testTwo.locations.size(), 4);
    }

    @Test
    public void testGet(){
        FavLocation_Test indexZero = testTwo.get(0);
        assertEquals( indexZero.name, testTwo.locations.get(0).name);
        FavLocation_Test indexOne = testTwo.get(1);
        assertEquals( indexOne.name, testTwo.locations.get(1).name);
        FavLocation_Test indexTwo = testTwo.get(2);
        assertEquals( indexTwo.name, testTwo.locations.get(2).name);
    }

    @Test
    public void testRemoveByIndex(){
        assertFalse( emptyLocations.remove(1));
        assertTrue( testTwo.remove(1) );
        assertEquals(testTwo.locations.size(), 2);
    }


    @Test
    public void testRemoveByLocation(){

    }

    /*
    @Test
    public void testRemoveByName(){

    }


    @Test
    public void testSearchIdx(){

    }

    @Test
    public void testSearchForLocationByLocation(){

    }

    @Test
    public void testSearchForLocationByName(){

    }*/

}
