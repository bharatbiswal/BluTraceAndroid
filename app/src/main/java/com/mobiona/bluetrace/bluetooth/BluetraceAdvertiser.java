package com.mobiona.bluetrace.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.os.ParcelUuid;
import android.util.Log;

import java.nio.charset.Charset;
import java.util.UUID;

public class BluetraceAdvertiser {
    private static final String TAG = "BluetraceAdvertiser";
    private BluetoothLeAdvertiser bluetoothLeAdvertiser;
    private ParcelUuid pUuid;
    private AdvertiseSettings advertiseSettings;
    private boolean isAdvertising;
    private int charLength=3;
    public BluetraceAdvertiser(String serviceId){
        bluetoothLeAdvertiser=BluetoothAdapter.getDefaultAdapter().getBluetoothLeAdvertiser();
        pUuid=new ParcelUuid(UUID.fromString(serviceId));
        advertiseSettings=createAdvertiseSettings();
    }



    private AdvertiseSettings createAdvertiseSettings(){
            return new  AdvertiseSettings.Builder()
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM)
                .setConnectable(true)
                .setTimeout(0)
                .build();
    }



    public void startAdvertising(){
        if(isAdvertising){
            return;
        }
        String randomUUID = UUID.randomUUID().toString();
        String finalString = randomUUID.substring(randomUUID.length() - charLength);
        Log.d(TAG, "Unique string:"+finalString);
        byte[] serviceDataByteArray = finalString.getBytes();

        AdvertiseData data = new AdvertiseData.Builder()
                .setIncludeDeviceName( true )
                .addServiceUuid( pUuid )
                //.setIncludeTxPowerLevel(true)
                //.addManufacturerData(1024,serviceDataByteArray)
                .build();
        bluetoothLeAdvertiser.startAdvertising( advertiseSettings, data, callback);


    }

    public void stopAdvertising(){
        if(isAdvertising) {
            bluetoothLeAdvertiser.stopAdvertising(callback);
        }
    }

    public boolean iAdvertising(){
        return isAdvertising;
    }

    private AdvertiseCallback callback=new AdvertiseCallback() {
        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            super.onStartSuccess(settingsInEffect);
            Log.i(TAG,"Advertising onStartSuccess");
            isAdvertising=true;
        }

        @Override
        public void onStartFailure(int errorCode) {
            super.onStartFailure(errorCode);
            String reason="";
            switch (errorCode){
                case ADVERTISE_FAILED_ALREADY_STARTED:
                    reason="ADVERTISE_FAILED_ALREADY_STARTED";
                    isAdvertising=true;
                    break;
                case ADVERTISE_FAILED_FEATURE_UNSUPPORTED:
                    reason="ADVERTISE_FAILED_FEATURE_UNSUPPORTED";
                    isAdvertising=false;
                    break;
                case ADVERTISE_FAILED_INTERNAL_ERROR:
                    reason="ADVERTISE_FAILED_INTERNAL_ERROR";
                    isAdvertising=false;
                    break;
                case ADVERTISE_FAILED_TOO_MANY_ADVERTISERS:
                    reason="ADVERTISE_FAILED_TOO_MANY_ADVERTISERS";
                    isAdvertising=false;
                    break;
                case ADVERTISE_FAILED_DATA_TOO_LARGE:
                    reason="ADVERTISE_FAILED_DATA_TOO_LARGE";
                    isAdvertising=false;
                    break;

            }
            Log.e(TAG,reason);
        }
    };
}
