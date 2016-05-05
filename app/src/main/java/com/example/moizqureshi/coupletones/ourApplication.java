package com.example.moizqureshi.coupletones;

import android.app.Application;
import com.parse.Parse;


/**
 * Created by moizqureshi on 5/5/16.
 */
public class ourApplication extends Application {
    public void onCreate(){
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("moizqureshipushtest")
                .server("http://pushtest126.herokuapp.com/parse/")
                .clientKey("qureshi1990")
                .build()
        );
    }

}
