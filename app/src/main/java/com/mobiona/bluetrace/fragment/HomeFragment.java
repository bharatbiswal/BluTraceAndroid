package com.mobiona.bluetrace.fragment;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.mobiona.bluetrace.OnItemClickListener;
import com.mobiona.bluetrace.R;
import com.mobiona.bluetrace.bluetooth.BlueTraceGattConnect;
import com.mobiona.bluetrace.bluetooth.BlueTraceScanner;
import com.mobiona.bluetrace.bluetooth.BluetraceAdvertiser;
import com.mobiona.bluetrace.bluetooth.BluetraceGattCallback;
import com.mobiona.bluetrace.bluetooth.BluetraceServer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class HomeFragment extends Fragment{

    private static final String TAG = "HomeFragment";
    private static final int REQUEST_LOCATION_PERMISSION=1;
    private static final int REQUEST_ENABLE_BT =2 ;

    public HomeFragment(){
        super(R.layout.fragment_home_layout);
    }

    private BluetoothAdapter bluetoothAdapter;

    private BlueTraceScanner blueTraceScanner;
    private BluetraceAdvertiser bluetraceAdvertiser;

    private MaterialButton scanBleButton,startBLEAdvButton;

    private BluetraceServer bluetraceServer;

    private DeviceAdapter adapter;

    private BlueTraceGattConnect blueTraceGattConnect;





    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        scanBleButton=view.findViewById(R.id.scanBLEDevices);
        startBLEAdvButton=view.findViewById(R.id.advertiseBLE);
        RecyclerView recyclerView=view.findViewById(R.id.deviceListview);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(),LinearLayoutManager.VERTICAL,false));
        adapter=new DeviceAdapter(new DeviceDiffCallback(),this::onItemClick);
        recyclerView.setAdapter(adapter);
        scanBleButton.setOnClickListener(v->{
            startBleScan();
        });
        startBLEAdvButton.setOnClickListener(v->{
            startAdvertise();
        });

        checkPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

    }

    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    private void checkPermission(){
        String[]permissions={Manifest.permission.ACCESS_FINE_LOCATION};
        if(EasyPermissions.hasPermissions(requireActivity(),permissions)){
           startBluetooth();
        }else{
            EasyPermissions.requestPermissions(this,"Ble Permissions",REQUEST_LOCATION_PERMISSION,permissions);
        }
    }

    private void startBluetooth(){
        BluetoothManager bluetoothManager= (BluetoothManager) requireActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter=bluetoothManager.getAdapter();

        if(bluetoothAdapter.isEnabled()){
            onBluetoothEnabled();
        }else{
            enableBluetooth();
        }

    }

    private void onBluetoothEnabled(){
        updateUI();
        setUpScanner();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_ENABLE_BT && resultCode== Activity.RESULT_OK){
            onBluetoothEnabled();
        }
    }

    private void updateUI(){
        scanBleButton.setEnabled(true);
        startBLEAdvButton.setEnabled(true);
    }

    private void setUpScanner(){
        blueTraceScanner=new BlueTraceScanner(requireActivity(),new BluetraceScanCallback() );
        bluetraceAdvertiser=new BluetraceAdvertiser(getString(R.string.serviceId));
        bluetraceServer=new BluetraceServer(requireActivity());

    }

    private void startBleScan(){
        if(blueTraceScanner!=null){
            if(blueTraceScanner.isScanning()){
                return;
            }
            blueTraceScanner.startBLEScan();
        }

    }

    private void stopBleScan(){
      if (blueTraceScanner!=null && blueTraceScanner.isScanning()){
          blueTraceScanner.stopBLEScan();
      }
    }

    private void startAdvertise(){
       if(bluetraceAdvertiser!=null) {
           if (bluetraceAdvertiser.iAdvertising()) {
               return;
           }
           bluetraceAdvertiser.startAdvertising();
       }
    }

    private void stopAdvertise(){
       if(bluetraceAdvertiser!=null && bluetraceAdvertiser.iAdvertising()){
           bluetraceAdvertiser.stopAdvertising();
       }
    }

    private void stopServer(){
        if(bluetraceServer!=null){
            bluetraceServer.stopServer();
        }
    }
    private void enableBluetooth(){
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }

    @Override
    public void onDestroyView() {
        stopBleScan();
        stopAdvertise();
        stopServer();
        if(blueTraceGattConnect!=null){
            blueTraceGattConnect.disconnect();
        }
        super.onDestroyView();
    }

    public void onItemClick(BluetoothDevice device) {
        blueTraceGattConnect=new BlueTraceGattConnect(device,requireActivity());
        UUID serviceUUID=UUID.fromString(getString(R.string.serviceId));
        blueTraceGattConnect.connect(new BluetraceGattCallback(serviceUUID));
    }

    private class DeviceDiffCallback extends DiffUtil.ItemCallback<BluetoothDevice>{

        @Override
        public boolean areItemsTheSame(@NonNull BluetoothDevice oldItem, @NonNull BluetoothDevice newItem) {
            return oldItem.getAddress().equals(newItem.getAddress());
        }

        @Override
        public boolean areContentsTheSame(@NonNull BluetoothDevice oldItem, @NonNull BluetoothDevice newItem) {
            return oldItem.equals(newItem);
        }
    }

    private class BluetraceScanCallback extends ScanCallback{
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            if(result!=null) {
               if(!adapter.getCurrentList().contains(result.getDevice())){
                   List<BluetoothDevice>devices=new ArrayList<>(adapter.getCurrentList());
                   devices.add(result.getDevice());
                   adapter.submitList(devices);
                }
            }


        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Log.e(TAG,"Scann Failed");
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            Log.i(TAG, "onBatchScanResult called");
            if(results!=null) {
                Log.i(TAG, "onBatchScanResults :" + results.size());
                List<BluetoothDevice>devices=new ArrayList<>();
                for(ScanResult scanResult:results){
                    devices.add(scanResult.getDevice());
                }
                adapter.submitList(devices);
            }

        }
    }

}
