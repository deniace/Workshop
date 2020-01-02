package com.deni.workshop.model;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Deni Supriyatna on 20 - Nov - 2019.
 */
public class SharedPref {
    private static final String TOKEN = "token";

    private String token;

    private Context context;

    private SharedPreferences preferences;

    public SharedPref(Context context){
        this.context = context;
        preferences = context.getSharedPreferences("Banisaleh", Context.MODE_PRIVATE);
    }

    public String getToken(){
        token = preferences.getString(TOKEN,"");
        return token;
    }

    public void setToken(String token) {
        this.token = token;
        preferences.edit().putString(TOKEN,token).apply();
    }
    public void clear(){
        preferences.edit().clear().apply();
    }
}
