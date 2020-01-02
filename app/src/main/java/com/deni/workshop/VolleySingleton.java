package com.deni.workshop;

import android.content.Context;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import java.io.File;

/**
 * Created by Deni Supriyatna on 08 - Nov - 2019.
 */
public class VolleySingleton {
    static VolleySingleton volley;
    private RequestQueue requestQueue;

    public static VolleySingleton getVolley(){
        return volley;
    }

    public RequestQueue getRequestQueue(){
        return requestQueue;
    }

    public static void initialize(Context context){
        HurlStack hurlStack = new HurlStack();
        Network network = new BasicNetwork(hurlStack);
        File cacheLocation = context.getCacheDir();
        Cache cache = new DiskBasedCache(cacheLocation);

        volley = new VolleySingleton();
        volley.requestQueue = new RequestQueue(cache, network);
        volley.requestQueue.start();
    }
}