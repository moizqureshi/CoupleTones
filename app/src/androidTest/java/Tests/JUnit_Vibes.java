package Tests;

import android.test.ActivityInstrumentationTestCase2;

import com.example.moizqureshi.coupletones.Vibes;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by raymondarevalo on 5/25/16.
 */
public class JUnit_Vibes extends ActivityInstrumentationTestCase2<Vibes> {
    Vibes vibes;

    public JUnit_Vibes() {
        super(Vibes.class);
    }

    @Before
    public void setUp() {
        /* Constructs a new list of preset vibrations */
        vibes = new Vibes();
    }

    @Test
    public void testPlay() {
        assertEquals(vibes.list.get(0).length, vibes.list.get(0).length);
       // assertEquals("hey", "no");
        int i = 0;
        System.out.print( "working?" + i );
    }
}
