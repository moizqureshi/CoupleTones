package com.example.moizqureshi.coupletones;
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

    private final static DateFormat FORMATOR = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public LocHist(String name, Calendar inTime ) {
        this.name = name;
        this.inTime = inTime;
        this.outTime = null;
    }

    public LocHist( String fullLog ) {
        int year, month, day, hours, mins, secs;

        name = fullLog.substring(0, fullLog.indexOf("-") - 1);

        //Finish this later
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

    @Override
    public String toString( ) {
        String fullLog =  name+" - in: "+FORMATOR.format(inTime.getTime())+" - out: ";

        if( outTime == null)
            fullLog += "NONE";
        else
            fullLog += FORMATOR.format(outTime.getTime());

        return fullLog;
    }

}
