package com.mobiona.bluetrace.bluetooth;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import java.util.UUID;

public class ProfileService {


    public static final UUID PROFILE_DEVICE_UUID=UUID.fromString("c6d656d5-2e4c-499b-84b7-f02261c3c3e1");



    public static BluetoothGattService createProfileService(String serviceUUIDString,UUID characteristicUUID){
        UUID serviceUUID=UUID.fromString(serviceUUIDString);
        BluetoothGattService gattService = new BluetoothGattService(serviceUUID, BluetoothGattService.SERVICE_TYPE_PRIMARY);
        BluetoothGattCharacteristic gattCharacteristic=new BluetoothGattCharacteristic(characteristicUUID,BluetoothGattCharacteristic.PROPERTY_READ|BluetoothGattCharacteristic.PROPERTY_WRITE,
                BluetoothGattCharacteristic.PERMISSION_READ|BluetoothGattCharacteristic.PERMISSION_WRITE);
        gattService.addCharacteristic(gattCharacteristic);
        return gattService;
    }
}
