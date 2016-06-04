package Tests;


import com.example.moizqureshi.coupletones.FavLocation;
import com.example.moizqureshi.coupletones.LocHist;
import com.example.moizqureshi.coupletones.Logs;
import com.google.android.gms.maps.model.LatLng;
import android.test.InstrumentationTestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.NoSuchElementException;


/**
 * Created by gcarb on 5/5/2016.
 */
public class JUnit_Logs_Test extends InstrumentationTestCase

{

    private String str1 = "Location1 - in: 01/23 8:17:15 - out: 02/24 9:32:12";
    private String str2 = "Location2 - in: 03/17 7:55:153 - out: 03/17 9:32:12";
    private String str3 = "Location3 - in: 07/02 10:32:32 - out: 07/03 12:23:13";
    private String str4 = "Location4 - in: 11/30 6:23:59 - out: 12/30 7:22:32";
    private String str5 = "Location5 - in: 12/31 1:11:45 - out: 12/31 11:32:12";
    private String str6 = "Location6 - in: 10/20 11:12:13 - out: 10/20 12:00:00";

    private LocHist loc1;
    private LocHist loc2;
    private LocHist loc3;
    private LocHist loc4;
    private LocHist loc5;
    private LocHist loc6;

    private Logs myLog;
    private LocHist locs[] = new LocHist[6];

    @Before
    public void setUp()
    {
        loc1 = new LocHist(str1);
        loc2 = new LocHist(str2);
        loc3 = new LocHist(str3);
        loc4 = new LocHist(str4);
        loc5 = new LocHist(str5);
        loc6 = new LocHist(str6);

        myLog = new Logs();
        locs[0] = loc1;
        locs[1] = loc2;
        locs[2] = loc3;
        locs[3] = loc4;
        locs[4] = loc5;
        locs[5] = loc6;
    }

    @After
    public void tearDown()
    {
        loc1 = null;
        loc2 = null;
        loc3 = null;
        loc4 = null;
        loc5 = null;
        loc6 = null;
        myLog = null;
        locs = null;
    }

    @Test
    public void testAdd()
    {
        for( int i = 0; i < 6; i++ )
        {
            myLog.add(locs[i]);
        }

        assertTrue( myLog.size() == 6);
        assertEquals( locs[0], myLog.get(0));
        assertEquals( locs[5], myLog.get(5));
    }

    @Test
    public void testRemove()
    {
        //adding elements
        for( int i = 0; i < 6; i++ )
        {
            myLog.add(locs[i]);
        }
        assertTrue( myLog.size() == 6);

        //removing elements
        for( int i = 0; i < 6; i++ )
        {
            myLog.remove();
        }

        assertTrue( myLog.size() == 0);

        try{
            myLog.remove();
            System.out.print("Should've caught exception");
        }
        catch (NoSuchElementException e)
        {
            //Exception
        }
    }

    @Test
    public void testSearch()
    {
        //adding elements
        for( int i = 0; i < 6; i++ )
        {
            myLog.add(locs[i]);
        }

        //searching
        assertTrue( myLog.search("Location1").equals( locs[0]));
        assertTrue( myLog.search("Location6").equals( locs[5]));
        assertTrue( myLog.search("Location3").equals( locs[2]));
        assertTrue( myLog.search("Location2").equals( locs[1]));
        assertTrue( myLog.search("Location4").equals( locs[3]));
        assertTrue( myLog.search("Location5").equals( locs[4]));
    }
}
