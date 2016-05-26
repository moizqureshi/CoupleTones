package com.example.moizqureshi.coupletones;

import android.app.Activity;
import android.content.Context;
import android.os.Vibrator;

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

    long list[][];

    Vibes() {
        list[0] = first;
        list[1] = second;
        list[3] = third;
        list[4] = fourth;
        list[5] = fifth;
        list[6] = sixth;
        list[7] = seventh;
        list[8] = nine;
        list[9] = ten;
    }

    public long[] get( int index ) {
        return list[index];
    }

    public void play( Context context, int index ) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if( vibrator != null)
            vibrator.vibrate( list[index], 0 );
    }
}
