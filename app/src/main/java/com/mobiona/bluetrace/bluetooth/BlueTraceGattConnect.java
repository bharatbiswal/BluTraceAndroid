package com.mobiona.bluetrace.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.content.Context;
import android.os.Build;
import android.util.Log;

public class BlueTraceGattConnect {

    private static final String TAG = "BlueTraceGatt";
    private BluetoothDevice bluetoothDevice;
    private Context context;
    private BluetoothGatt bluetoothGatt;
    public BlueTraceGattConnect(BluetoothDevice bluetoothDevice, Context context){
        this.bluetoothDevice=bluetoothDevice;
        this.context=context;
    }

    public void connect(BluetoothGattCallback callback){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            bluetoothGatt = bluetoothDevice.connectGatt(context, false, callback, BluetoothDevice.TRANSPORT_LE);
        } else {
            bluetoothGatt = bluetoothDevice.connectGatt(context, false, callback);
        }
        if(bluetoothGatt==null){
            Log.d(TAG,"Fail to connect to device :"+bluetoothDevice.getName());
        }
    }

    public void disconnect(){
        if(bluetoothGatt!=null){
            bluetoothGatt.disconnect();
        }
    }
}
