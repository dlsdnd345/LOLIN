package com.iris.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedpreferencesUtil {
 
    private Context context;
 
    public SharedpreferencesUtil(Context context) {
    	this.context = context;
    }
 
    public void put(String key, String value) {
        SharedPreferences pref = context.getSharedPreferences(context.getPackageName(),Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }
 
    public void put(String key, boolean value) {
        SharedPreferences pref = context.getSharedPreferences(context.getPackageName(),Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
 
    public void put(String key, int value) {
        SharedPreferences pref = context.getSharedPreferences(context.getPackageName(),Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
 
        editor.putInt(key, value);
        editor.commit();
    }
 
    public String getValue(String key, String dftValue) {
        SharedPreferences pref = context.getSharedPreferences(context.getPackageName(),Activity.MODE_PRIVATE);
        try {
            return pref.getString(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }
 
    }
 
    public int getValue(String key, int dftValue) {
        SharedPreferences pref = context.getSharedPreferences(context.getPackageName(),Activity.MODE_PRIVATE);
        try {
            return pref.getInt(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }
 
    }
 
    public boolean getValue(String key, boolean dftValue) {
        SharedPreferences pref = context.getSharedPreferences(context.getPackageName(),Activity.MODE_PRIVATE);
        try {
            return pref.getBoolean(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }
    }
	
}
