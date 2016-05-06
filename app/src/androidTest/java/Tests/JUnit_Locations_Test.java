package Tests;

import com.example.moizqureshi.coupletones.FavLocation_Test;
import com.example.moizqureshi.coupletones.Locations_Test;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by Alina on 5/5/2016.
 */
public class JUnit_Locations_Test  {

    @Before
    public void setUp(){
        Locations_Test testOne = new Locations_Test();

        int size = 5;
        Locations_Test testTwo = new Locations_Test( size );

        ArrayList<FavLocation_Test> testThreeParam = new ArrayList<>(size);
        Locations_Test testThree = new Locations_Test( testThreeParam );

    }

    @Test
    public void getFavLocation(){

    }

    @Test
    public void testAdd(){

    }

    @Test
    public void testRemoveByIndex(){

    }

    @Test
    public void testRemoveByLocation(){

    }

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

    }

}
