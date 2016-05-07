package Tests;

import com.example.moizqureshi.coupletones.FavLocation;
import com.example.moizqureshi.coupletones.Locations;


import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by Alina on 5/5/2016.
 */
public class JUnit_Locations_Test  {

    @Before
    public void setUp(){
        Locations testOne = new Locations();

        int size = 5;
        Locations testTwo = new Locations( size );

        ArrayList<FavLocation> testThreeParam = new ArrayList<>(size);
        Locations testThree = new Locations( testThreeParam );

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
