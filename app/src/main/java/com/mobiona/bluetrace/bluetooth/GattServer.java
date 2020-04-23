package com.mobiona.bluetrace.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;

import com.mobiona.bluetrace.PreferenceManager;
import com.mobiona.bluetrace.R;

import java.util.UUID;

public class GattServer {
    private static final String TAG = "GattServer";
    private BluetoothManager bluetoothManager;
    private String serviceUUID;
    private BluetoothGattServer bluetoothGattServer;
    private Context context;
    public GattServer(Context context,String serviceUUID){
        this.context=context;
        bluetoothManager= (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        this.serviceUUID=serviceUUID;

    }

    public boolean startGattserver(){
        bluetoothGattServer=bluetoothManager.openGattServer(context,gattServerCallback);
        if(bluetoothGattServer!=null){
            bluetoothGattServer.clearServices();
            return true;
        }
        return false;
    }

    public void  addService(UUID characteristicUUID){
        bluetoothGattServer.addService(ProfileService.createProfileService(serviceUUID,characteristicUUID));
    }

    public void stopServer(){
        try {
            bluetoothGattServer.clearServices();
            bluetoothGattServer.close();
        }catch (Exception e){
            Log.e(TAG,e.getMessage(),e);
        }

    }


    private BluetoothGattServerCallback gattServerCallback=new BluetoothGattServerCallback() {

        @Override
        public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
             switch (newState){
                 case BluetoothProfile.STATE_CONNECTED:
                      Log.d(TAG,String.format("%s connected to local GATT Server",device.getAddress()));
                     break;
                 case BluetoothProfile.STATE_DISCONNECTED:
                     Log.d(TAG,String.format("%s Disconnected to local GATT Server",device.getAddress()));
                     break;
             }
        }

        @Override
        public void onCharacteristicReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicReadRequest(device, requestId, offset, characteristic);
            if(device==null){
                Log.e(TAG,"No Device Found");
                return;
            }
            Log.d(TAG,"Device Read Request from"+device.getAddress());

            if(characteristic.getUuid().equals(ProfileService.PROFILE_DEVICE_UUID)){
                Log.i(TAG,PreferenceManager.getInstance(context).getDeviceUuid());
                bluetoothGattServer.sendResponse(
                       device,requestId, BluetoothGatt.GATT_SUCCESS,0, PreferenceManager.getInstance(context).getDeviceUuid().getBytes());
            }
        }

        @Override
        public void onCharacteristicWriteRequest(BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
            super.onCharacteristicWriteRequest(device, requestId, characteristic, preparedWrite, responseNeeded, offset, value);
        }

        @Override
        public void onExecuteWrite(BluetoothDevice device, int requestId, boolean execute) {
            super.onExecuteWrite(device, requestId, execute);
        }
    };
}
