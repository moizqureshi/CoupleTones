package Tests;


import com.example.moizqureshi.coupletones.FavLocation;
import com.example.moizqureshi.coupletones.LocHist;
import com.example.moizqureshi.coupletones.Locations;
import com.example.moizqureshi.coupletones.R;
import com.example.moizqureshi.coupletones.Sounds;

import android.test.InstrumentationTestCase;
import android.view.Display;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;




/**
 * Created by gcarb on 5/5/2016.
 */
public class JUnit_Sounds_Test extends InstrumentationTestCase

{

    private FavLocation loc1;
    private FavLocation loc2;

    private int[] sd = { R.raw.birdy, R.raw.bottle, R.raw.elastic, R.raw.enigmatic, R.raw.ice,
            R.raw.mallet, R.raw.softbell, R.raw.spaceball, R.raw.toy, R.raw.wood, R.raw.space1, R.raw.space2 };


    @Before
    public void setUp()
    {

        loc1 = new FavLocation("Location 1 * 50 * -50 & 6 [ 0 % 4 ] 8");
        loc2 = new FavLocation("Location 2 * 100 * -30 & 2 [ 1 % 3 ] 7");

    }

    @After
    public void tearDown()
    {
        loc1 = null;
        loc2 = null;
    }

    @Test
    public void testGetSoundIndex()
    {
        assertEquals( loc1.getSoundIdx(0), 4 );
        assertEquals( loc1.getSoundIdx(1), 8 );
        assertEquals( loc2.getSoundIdx(0), 3 );
        assertEquals( loc2.getSoundIdx(1), 7 );
    }

    @Test
    public void testSetSoundIndex()
    {
        loc1.setSoundIdx(0,2);
        loc1.setSoundIdx(1,5);
        loc2.setSoundIdx(0,7);
        loc2.setSoundIdx(1,1);

        assertEquals( loc1.getSoundIdx(0), 2 );
        assertEquals( loc1.getSoundIdx(1), 5 );
        assertEquals( loc2.getSoundIdx(0), 7 );
        assertEquals( loc2.getSoundIdx(1), 1 );
    }

    @Test
    public void testSounds()
    {
        Sounds sound1 = new Sounds(0);
        Sounds sound2 = new Sounds(1);
        Sounds sound3 = new Sounds(2);
        Sounds sound4 = new Sounds(3);
        Sounds sound5 = new Sounds(4);
        Sounds sound6 = new Sounds(5);
        Sounds sound7 = new Sounds(6);
        Sounds sound8 = new Sounds(7);
        Sounds sound9 = new Sounds(8);
        Sounds sound10 = new Sounds(9);
        Sounds sound11 = new Sounds(10);
        Sounds sound12 = new Sounds(11);

        assertTrue(  sound1.getPlayer() == sd[0] );
        assertEquals( sd[1], sound2.getPlayer() );
        assertEquals( sd[2], sound3.getPlayer() );
        assertEquals( sd[3], sound4.getPlayer() );
        assertEquals( sd[4], sound5.getPlayer() );
        assertEquals( sd[5], sound6.getPlayer() );
        assertEquals( sd[6], sound7.getPlayer() );
        assertEquals( sd[7], sound8.getPlayer() );
        assertEquals( sd[8], sound9.getPlayer() );
        assertEquals( sd[9], sound10.getPlayer() );
        assertEquals( sd[10], sound11.getPlayer() );
        assertEquals( sd[11], sound12.getPlayer() );

    }
}
