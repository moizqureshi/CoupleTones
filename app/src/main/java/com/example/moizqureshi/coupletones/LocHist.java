package com.example.moizqureshi.coupletones;
import android.provider.CalendarContract;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/**
 * Created by Benjamin on 5/21/2016.
 */
public class LocHist {
    private String name;
    private Calendar inTime;
    private Calendar outTime;

    private final static DateFormat FORMATOR = new SimpleDateFormat("mm/dd HH:mm:ss");

    public LocHist(String name, Calendar inTime ) {
        this.name = name;
        this.inTime = inTime;
        this.outTime = null;
    }

    public LocHist( String fullLog ) {
        int year, month, day, hours, mins, secs;

        name = fullLog.substring(0, fullLog.indexOf('-') - 1);
        fullLog = fullLog.substring( fullLog.indexOf('-') + 6 );
        //Log.d("date string:", fullLog);

       /* year = Integer.parseInt( fullLog.substring(0, fullLog.indexOf('/')) );
        fullLog = fullLog.substring( fullLog.indexOf('/') + 1);
        */
        month = Integer.parseInt( fullLog.substring(0, fullLog.indexOf('/')) );
        fullLog = fullLog.substring( fullLog.indexOf('/') + 1);
        //Log.d("mins:", ""+month);

        day = Integer.parseInt( fullLog.substring(0, fullLog.indexOf(' ')) );
        fullLog = fullLog.substring( fullLog.indexOf(' ') + 1);

        hours = Integer.parseInt( fullLog.substring(0, fullLog.indexOf(':')) );
        fullLog = fullLog.substring( fullLog.indexOf(':') + 1);

        mins = Integer.parseInt( fullLog.substring(0, fullLog.indexOf(':')) );
        fullLog = fullLog.substring( fullLog.indexOf(':') + 1);

        secs = Integer.parseInt( fullLog.substring(0, fullLog.indexOf('-') -1 ) );

        inTime = Calendar.getInstance();
        inTime.set( 116, month, day, hours, mins, secs);
        //Log.d("mins after:", ""+inTime.get(Calendar.MONTH));

        fullLog = fullLog.substring( fullLog.indexOf('-') + 7);
        String checker;

        checker = fullLog.substring(0, 4);

        if( checker.equals("NONE") )
            outTime = null;
        else {
            /*year = Integer.parseInt(fullLog.substring(0, fullLog.indexOf('/')));
            fullLog = fullLog.substring(fullLog.indexOf('/') + 1);
            */
            month = Integer.parseInt(fullLog.substring(0, fullLog.indexOf('/')));
            fullLog = fullLog.substring(fullLog.indexOf('/') + 1);

            day = Integer.parseInt(fullLog.substring(0, fullLog.indexOf(' ')));
            fullLog = fullLog.substring(fullLog.indexOf(' ') + 1);

            hours = Integer.parseInt(fullLog.substring(0, fullLog.indexOf(':')));
            fullLog = fullLog.substring(fullLog.indexOf(':') + 1);

            mins = Integer.parseInt(fullLog.substring(0, fullLog.indexOf(':')));
            fullLog = fullLog.substring(fullLog.indexOf(':') + 1);

            secs = Integer.parseInt(fullLog);
            outTime = Calendar.getInstance();
            outTime.set(116, month, day, hours, mins, secs);
        }
    }

    public String getName( ) {
        return name;
    }

    public Calendar getInTime( ) {
        return inTime;
    }

    public Calendar getOutTime( ) {
        return outTime;
    }

    public void setOutTime( Calendar outTime ) {
        this.outTime = outTime;
    }

    //name - in: yyyy/mm/dd hh:mm:ss - out: yyyy/mm/dd hh:mm:ss
    @Override
    public String toString( ) {
        int month, day, hours, min, sec;
        month = inTime.get(Calendar.MONTH);
        day = inTime.get(Calendar.DAY_OF_MONTH);
        hours = inTime.get(Calendar.HOUR);
        min = inTime.get(Calendar.MINUTE);
        sec = inTime.get( Calendar.SECOND);

        String fullLog =  name+" - in: "+month+"/"+day+" "+hours+":"+min+":"+sec+" - out: ";


        if( outTime == null)
            fullLog += "NONE";
        else {
            month = outTime.get(Calendar.MONTH);
            day = outTime.get(Calendar.DAY_OF_MONTH);
            hours = outTime.get(Calendar.HOUR);
            min = outTime.get(Calendar.MINUTE);
            sec = outTime.get( Calendar.SECOND);
            fullLog += month+"/"+day+" "+hours+":"+min+":"+sec;

        }
        return fullLog;
    }

}
