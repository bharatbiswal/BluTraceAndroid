package com.mobiona.bluetrace.bluetooth;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.util.Log;

import java.util.UUID;

public class BluetraceGattCallback extends BluetoothGattCallback {
    private static final String TAG = "BluetraceGatt";
    private UUID serviceUUID;

    public BluetraceGattCallback(UUID serviceUUID){
        this.serviceUUID=serviceUUID;
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        switch (newState){
            case BluetoothProfile.STATE_CONNECTED:
                Log.i(TAG, "Connected to other GATT server"+ gatt.getDevice().getName());
                boolean discoveryOn=gatt.discoverServices();
                Log.i(TAG,"Attempting to start service discovery on "+gatt.getDevice().getName() +" status :"+discoveryOn);
                break;
            case BluetoothProfile.STATE_DISCONNECTED:
                Log.i(TAG, "Disconnected from other GATT server"+ gatt.getDevice().getName());

                break;
        }
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        switch (status){
            case BluetoothGatt.GATT_SUCCESS:
                 Log.i(TAG,"Service discovered"+gatt.getServices().size());
                 BluetoothGattService gattService=gatt.getService(serviceUUID);
                 if(gattService!=null){
                    BluetoothGattCharacteristic characteristic= gattService.getCharacteristic(ProfileService.PROFILE_DEVICE_UUID);
                    if(characteristic!=null){
                       boolean readSuccess= gatt.readCharacteristic(characteristic);
                       if(readSuccess){
                           Log.i(TAG,"Attempt to read characteristic of our service");
                       }
                    }
                 }
                break;
        }

    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        Log.i(TAG, "Read Status:"+status);
        switch (status){
            case BluetoothGatt.GATT_SUCCESS:
                Log.i(TAG,String.format("Characteristic read from %s : %s ",gatt.getDevice().getAddress(),characteristic.getStringValue(0)));
                String data=new String(characteristic.getValue());
                Log.i(TAG,"Data is "+data);
                break;
        }
    }
}
