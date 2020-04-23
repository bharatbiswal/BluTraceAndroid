package com.mobiona.bluetrace.bluetooth;

import android.content.Context;

import com.mobiona.bluetrace.R;

public class BluetraceServer {

    private static final String TAG = "BluetraceServer";
    private String serviceUUID;

    private GattServer gattServer;

    public BluetraceServer(Context context){
        serviceUUID=context.getString(R.string.serviceId);
        setUp(context);
    }

    private void setUp(Context context){
        gattServer=new GattServer(context,serviceUUID);
        boolean isStarted=gattServer.startGattserver();
        if(isStarted){
            gattServer.addService(ProfileService.PROFILE_DEVICE_UUID);
        }
    }
    public void stopServer(){
        gattServer.stopServer();
    }
}
