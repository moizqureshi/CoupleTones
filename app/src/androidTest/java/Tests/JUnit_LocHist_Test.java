package Tests;


import com.example.moizqureshi.coupletones.FavLocation;
import com.example.moizqureshi.coupletones.LocHist;
import com.example.moizqureshi.coupletones.Locations;
import com.google.android.gms.maps.model.LatLng;
import android.test.InstrumentationTestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;


/**
 * Created by gcarb on 5/5/2016.
 */
public class JUnit_LocHist_Test extends InstrumentationTestCase

{

    private String logHist;
    private LocHist hist;


    @Before
    public void setUp()
    {

        logHist = "Location1 - in: 11/23 8:23:17 - out: 11/23 9:32:12";
        hist = new LocHist( logHist );

    }

    @After
    public void tearDown()
    {
        logHist = null;
        hist = null;
    }

    @Test
    public void testGetName()
    {
        String name = "Location1";

        assertEquals( name, hist.getName());
    }

    @Test
    public void testGetInTime()
    {
        Calendar inTime = Calendar.getInstance();
        inTime.set(116, 11, 23, 8, 23, 17 );

        assertEquals( inTime.get(Calendar.MONTH), hist.getInTime().get(Calendar.MONTH) + 1);
        assertEquals( inTime.get(Calendar.DAY_OF_MONTH), hist.getInTime().get(Calendar.DAY_OF_MONTH ));
        assertEquals( inTime.get(Calendar.HOUR), hist.getInTime().get(Calendar.HOUR) );
        assertEquals( inTime.get(Calendar.MINUTE), hist.getInTime().get(Calendar.MINUTE) );
        assertEquals( inTime.get(Calendar.SECOND), hist.getInTime().get(Calendar.SECOND));
    }

    @Test
    public void testGetOutTime()
    {
        Calendar outTime = Calendar.getInstance();
        outTime.set(116, 11, 23, 9, 32, 12);

        assertEquals( outTime.get(Calendar.MONTH), hist.getOutTime().get(Calendar.MONTH) + 1);
        assertEquals( outTime.get(Calendar.DAY_OF_MONTH), hist.getOutTime().get(Calendar.DAY_OF_MONTH ));
        assertEquals( outTime.get(Calendar.HOUR), hist.getOutTime().get(Calendar.HOUR) );
        assertEquals( outTime.get(Calendar.MINUTE), hist.getOutTime().get(Calendar.MINUTE) );
        assertEquals( outTime.get(Calendar.SECOND), hist.getOutTime().get(Calendar.SECOND));
    }

    @Test
    public  void testSetOutTime()
    {
        Calendar outTime = Calendar.getInstance();
        outTime.set(116, 22, 18, 11, 35, 56 );

        hist.setOutTime( outTime );

        assertEquals( outTime.get(Calendar.MONTH), hist.getOutTime().get(Calendar.MONTH) );
        assertEquals( outTime.get(Calendar.DAY_OF_MONTH), hist.getOutTime().get(Calendar.DAY_OF_MONTH ));
        assertEquals( outTime.get(Calendar.HOUR), hist.getOutTime().get(Calendar.HOUR) );
        assertEquals( outTime.get(Calendar.MINUTE), hist.getOutTime().get(Calendar.MINUTE) );
        assertEquals( outTime.get(Calendar.SECOND), hist.getOutTime().get(Calendar.SECOND));
    }

    @Test
    public void testToString()
    {
       assertEquals( logHist, hist.toString() );
    }
}
