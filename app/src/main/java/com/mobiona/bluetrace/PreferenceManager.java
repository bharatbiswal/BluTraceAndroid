package com.mobiona.bluetrace;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    private static PreferenceManager INSTANCE;

    private static final String PREF_NAME="BLUETRACE";
    private static final String DEVICE_UUID="DEVICE_UUID";

    private SharedPreferences sharedPreferences;
    public static PreferenceManager getInstance(Context context){
        if(INSTANCE==null){
            synchronized (PreferenceManager.class){
                if(INSTANCE==null){
                    INSTANCE=new PreferenceManager(context);
                }
            }
        }
        return INSTANCE;
    }


    private PreferenceManager(Context context){
        sharedPreferences=context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
    }

    public void setDeviceUUID(String uuid){
        sharedPreferences.edit().putString(DEVICE_UUID,uuid).commit();
    }
    public String getDeviceUuid(){
       return sharedPreferences.getString(DEVICE_UUID,"");
    }
}
