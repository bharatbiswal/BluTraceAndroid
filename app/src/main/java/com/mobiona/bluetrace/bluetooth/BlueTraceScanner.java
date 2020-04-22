package com.mobiona.bluetrace.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.os.Handler;
import android.os.ParcelUuid;

import com.mobiona.bluetrace.R;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class BlueTraceScanner {

    private BluetoothAdapter bluetoothAdapter;
    private Handler scanHandler;
    private static final long SCAN_PERIOD = 10000;
    private boolean isScanning;

    private String serviceUUID;

    private ScanCallback scanCallback;

    private List<ScanFilter>scanFilters;
    private ScanSettings scanSettings;
    private long reportDelay=0;




    public BlueTraceScanner(Context context,ScanCallback scanCallback){
        BluetoothManager bluetoothManager= (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter=bluetoothManager.getAdapter();
        scanHandler=new Handler();
        this.scanCallback=scanCallback;
        serviceUUID=context.getString(R.string.serviceId);
        setUpFilter();
        setupScanSettings();
    }

    private void setUpFilter(){
        ScanFilter scanFilter=new ScanFilter.Builder().
                setServiceUuid(new ParcelUuid(UUID.fromString(serviceUUID)))
                .build();
        scanFilters=new ArrayList<>();
        scanFilters.add(scanFilter);
    }

    private void setupScanSettings(){
        scanSettings=new ScanSettings.Builder()
                .setReportDelay(reportDelay)
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build();
    }

    public void startBLEScan(){

        if(bluetoothAdapter.isEnabled()){
            scanHandler.postDelayed(()->{
                isScanning=false;
                bluetoothAdapter.getBluetoothLeScanner().stopScan(scanCallback);
            },SCAN_PERIOD);
            isScanning=true;
            bluetoothAdapter.getBluetoothLeScanner().startScan(scanFilters,scanSettings,scanCallback);
        }
    }

    public void stopBLEScan(){
        if(bluetoothAdapter.isEnabled() && isScanning){
            bluetoothAdapter.getBluetoothLeScanner().stopScan(scanCallback);
            isScanning=false;
        }
    }

    public boolean isScanning(){
        return false;
    }
}
