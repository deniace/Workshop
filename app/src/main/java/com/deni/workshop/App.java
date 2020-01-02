package com.deni.workshop;

import android.app.Application;

/**
 * Created by Deni Supriyatna on 08 - Nov - 2019.
 */
public class App extends Application {
    public static final String API_URL =
//            "http://172.10.10.13:8080/banisaleh/";
            "http://192.168.43.190:8080/banisaleh/";

    @Override
    public void onCreate(){
        super.onCreate();
        VolleySingleton.initialize(this);
    }
}
