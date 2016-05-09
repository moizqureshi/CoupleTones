package Tests;

import android.test.ActivityInstrumentationTestCase2;

import com.google.android.gms.maps.model.LatLng;
import com.example.moizqureshi.coupletones.Locations;
import com.example.moizqureshi.coupletones.FavLocation;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import static org.junit.Assert.*;

/**
 * Created by Alina on 5/5/2016.
 */
public class JUnit_Locations_Test extends ActivityInstrumentationTestCase2<Locations> {

    Locations emptyLocations;
    Locations testThreeLocations;
    LatLng firstLocation;
    LatLng secondLocation;
    LatLng thirdLocation;
    FavLocation first;
    FavLocation second;
    FavLocation third;
    ArrayList<FavLocation> testThreeParam;

    public JUnit_Locations_Test(){
        super(Locations.class);
    }

    @Before
    public void setUp(){

        //Initializes Locations list with no FavLocations
        emptyLocations = new Locations();

        firstLocation = new LatLng(32.637497, -116.985094);
        secondLocation = new LatLng(32.738728, -117.121994);
        thirdLocation = new LatLng(34.031667, -118.330635);


        first = new FavLocation("Alina's House", firstLocation);
        second = new FavLocation("Alina's Old House", secondLocation);
        third = new FavLocation("Raymond's House", thirdLocation);

        testThreeParam = new ArrayList<>();
        testThreeParam.add(first);
        testThreeParam.add(second);
        testThreeParam.add(third);

        //Initializes Locations list with three FavLocations
        testThreeLocations = new Locations( testThreeParam );
    }

    /*Verifies the function successfully adds new FavLocation objects to Locations list*/
    @Test
    public void testAdd(){
        emptyLocations.add(first);
        assertEquals( emptyLocations.size(), 1);
        testThreeLocations.add(first);
        assertEquals(testThreeLocations.size(), 4);
    }

    /* Tests to verify the proper name of the FavLocation at the indicated index. */
    @Test
    public void testGet(){
        FavLocation indexZero = testThreeLocations.get(0);
        assertEquals( indexZero.getName(), testThreeLocations.locations.get(0).getName());
        FavLocation indexOne = testThreeLocations.get(1);
        assertEquals( indexOne.getName(), testThreeLocations.locations.get(1).getName());
        FavLocation indexTwo = testThreeLocations.get(2);
        assertEquals( indexTwo.getName(), testThreeLocations.locations.get(2).getName());
    }

    /* Returns false when remove is called on an empty Locations list and removes proper FavLocation
    at the specified index. */
    @Test
    public void testRemoveByIndex(){
        assertFalse( emptyLocations.remove(1));
        assertTrue( testThreeLocations.remove(1) );
        assertEquals(testThreeLocations.size(), 2);
    }

    /*Removes all of the testThreeLocations FavLocations by name then verifies the Locations list
    is now empty. */
    @Test
    public void testRemoveByName(){
        testThreeLocations.remove( "Alina's House");
        testThreeLocations.remove( "Alina's Old House");
        testThreeLocations.remove("Raymond's House");
        assertEquals( testThreeLocations.size(), 0);
    }


    /* Returns -1 when an items tht is not on the Locations is searched for and the proper index
    number is the item is on the Locations list. */
    @Test
    public void testSearchIdxByName(){
        assertEquals( emptyLocations.searchIdx("Not here") , -1);
        assertEquals( testThreeLocations.searchIdx("Alina's House"), 0);
    }

    /* Searches for FavLocations by name and verifies the proper FavLocation is returned*/
    @Test
    public void testSearchForLocationByName(){
        assertEquals( testThreeLocations.searchLoc("Alina's House"), first);
        assertEquals( testThreeLocations.searchLoc("Raymond's House"), third);
    }


    /*Verifies the proper size is returned */
    @Test
    public void size(){
        assertEquals( testThreeLocations.size(), 3);
        assertEquals( emptyLocations, 0 );
    }

}