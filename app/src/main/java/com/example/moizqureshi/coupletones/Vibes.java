package com.example.moizqureshi.coupletones;

import android.app.Activity;
import android.content.Context;
import android.os.Vibrator;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by raymondarevalo on 5/21/16.
 */
public class Vibes extends Activity {
    /* Super Mario */
    public final static long first [] = {125,75,125,275,200,275,125,75,125,275,200,600,200,600};

    /* Teenage Mutant Ninja Turtles */
    public final static long second [] = {75,75,75,75,75,75,75,75,150,150,150,450,75,75,75,75,75,525};

    /* Star Wars Imperial March */
    public final static long third [] = {500,110,500,110,450,110,200,110,170,40,450,110,200,110,170,40,500};

    /* GO GO Power */
    public final static long fourth [] = {150,150,150,150,75,75,150,150,150,150,450};

    /* SOS */
    public final static long fifth [] = {100,30,100,30,100,200,200,30,200,30,200,200,100,30,100,30,100};

    /* Shave and Haircut */
    public final static long sixth [] = {100,200,100,100,75,25,100,200,100,500,100,200,100,500};

    /* James */
    public final static long seventh [] = {200,100,200,275,425,100,200,100,200,275,425,100,75,25,75,125,75,25,75,125,100,100};

    /* Dream Theater - Fatal Tragedy "What a phenomenon" */
    public final static long eight [] = {75,38,75,488,75,38,75,200,75,38,75,400};

    /* Michael Jackson - Smooth Criminal */
    public final static long nine [] = {0,300,100,50,100,50,100,50,100,50,100,50,100,50,150,150,150,450,100,50,100,50,150,150,150,450,100,50,100,50,150,150,150,450,150,150};

    /* Muse - Madness */
    public final static long ten [] = {80,80,80,80,80,80,80,80,80,80,80,80,80,80,80,80,320,160,320,160,320};

    public ArrayList<long[]> list = new ArrayList<>(10); /* TODO changed */

    public Vibes() {    // TODO changed. Vibes() -> public Vibes()
        list.add(0, first);
        list.add(1, second);
        list.add(2, third);
        list.add(3, fourth);
        list.add(4, fifth);
        list.add(5, sixth);
        list.add(6, seventh);
        list.add(7, eight);
        list.add(8, nine);
        list.add(9, ten);
    }

    public long[] get( int index ) {
        return list.get(index);
    }

    public long[] play( int index ) { /* TODO changed */
        Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE); /* TODO Changed */
        if( vibrator != null)
            vibrator.vibrate( list.get(index), -1 ); /* Changed */
        return list.get(index);
    }
}
