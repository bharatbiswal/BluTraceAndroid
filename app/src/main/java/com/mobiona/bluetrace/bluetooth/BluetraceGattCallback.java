package com.mobiona.bluetrace.bluetooth;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.util.Log;

import com.mobiona.bluetrace.Event;
import com.mobiona.bluetrace.RxBus;

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
                gatt.requestConnectionPriority(BluetoothGatt.CONNECTION_PRIORITY_BALANCED);
                gatt.requestMtu(512);

                break;
            case BluetoothProfile.STATE_DISCONNECTED:
                Log.i(TAG, "Disconnected from other GATT server"+ gatt.getDevice().getName());
                gatt.close();

                break;
        }
    }

    @Override
    public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
        Log.i(TAG,String.format("%s MTU is %d was changed status : %d",gatt.getDevice().getAddress(),mtu,status));
        if(status==BluetoothGatt.GATT_SUCCESS){
            boolean discoveryOn=gatt.discoverServices();
            Log.i(TAG,"Attempting to start service discovery on "+gatt.getDevice().getName() +" status :"+discoveryOn);
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
                RxBus.getInstance().sendEvent(new Event(String.format("Read Success %s",data),Event.EVENT_CHARACTERSTICS_READ));
                break;
        }
    }
}
