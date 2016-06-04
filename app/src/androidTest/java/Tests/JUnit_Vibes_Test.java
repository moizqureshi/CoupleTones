package Tests;


import com.example.moizqureshi.coupletones.FavLocation;
import com.example.moizqureshi.coupletones.LocHist;
import com.example.moizqureshi.coupletones.Locations;
import com.example.moizqureshi.coupletones.R;
import com.example.moizqureshi.coupletones.Sounds;
import com.example.moizqureshi.coupletones.Vibes;

import android.test.InstrumentationTestCase;
import android.view.Display;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;




/**
 * Created by gcarb on 5/5/2016.
 */
public class JUnit_Vibes_Test extends InstrumentationTestCase

{

    private FavLocation loc1;
    private FavLocation loc2;

   private Vibes vb;


    @Before
    public void setUp()
    {

        loc1 = new FavLocation("Location 1 * 50 * -50 & 6 [ 0 % 4 ] 8");
        loc2 = new FavLocation("Location 2 * 100 * -30 & 2 [ 1 % 3 ] 7");
        vb = new Vibes();

    }

    @After
    public void tearDown()
    {
        loc1 = null;
        loc2 = null;
        vb = null;
    }

    @Test
    public void testGetVibeIndex()
    {
        assertEquals( loc1.getVibeIdx(0), 6 );
        assertEquals( loc1.getVibeIdx(1), 0 );
        assertEquals( loc2.getVibeIdx(0), 2 );
        assertEquals( loc2.getVibeIdx(1), 1 );
    }

    @Test
    public void testSetVibeIndex()
    {
        loc1.setVibeIdx(0,2);
        loc1.setVibeIdx(1,5);
        loc2.setVibeIdx(0,7);
        loc2.setVibeIdx(1,1);

        assertEquals( loc1.getVibeIdx(0), 2 );
        assertEquals( loc1.getVibeIdx(1), 5 );
        assertEquals( loc2.getVibeIdx(0), 7 );
        assertEquals( loc2.getVibeIdx(1), 1 );
    }

    @Test
    public void testVibesLength()
    {
        assertTrue( vb.get(0).length == Vibes.first.length);
        assertTrue( vb.get(1).length == Vibes.second.length);
        assertTrue( vb.get(2).length == Vibes.third.length);
        assertTrue( vb.get(3).length == Vibes.fourth.length);
        assertTrue( vb.get(4).length == Vibes.fifth.length);
        assertTrue( vb.get(5).length == Vibes.sixth.length);
        assertTrue( vb.get(6).length == Vibes.seventh.length);
        assertTrue( vb.get(7).length == Vibes.eight.length);
        assertTrue( vb.get(8).length == Vibes.nine.length);
        assertTrue( vb.get(9).length == Vibes.ten.length);
    }

    @Test
    public void testVibes()
    {
        assertTrue( vb.get(0).equals( Vibes.first ));
        assertTrue( vb.get(1).equals( Vibes.second ));
        assertTrue( vb.get(2).equals( Vibes.third ));
        assertTrue( vb.get(3).equals( Vibes.fourth ));
        assertTrue( vb.get(4).equals( Vibes.fifth ));
        assertTrue( vb.get(5).equals( Vibes.sixth ));
        assertTrue( vb.get(6).equals( Vibes.seventh ));
        assertTrue( vb.get(7).equals( Vibes.eight ));
        assertTrue( vb.get(8).equals( Vibes.nine ));
        assertTrue( vb.get(9).equals( Vibes.ten ));
    }
}