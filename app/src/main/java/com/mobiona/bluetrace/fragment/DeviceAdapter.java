package com.mobiona.bluetrace.fragment;

import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.mobiona.bluetrace.OnItemClickListener;
import com.mobiona.bluetrace.R;

public class DeviceAdapter extends ListAdapter<BluetoothDevice, DeviceAdapter.DeviceviewHolder> {

    private OnItemClickListener<BluetoothDevice>onItemClickListener;
    protected DeviceAdapter(@NonNull DiffUtil.ItemCallback<BluetoothDevice> diffCallback, OnItemClickListener<BluetoothDevice>onItemClickListener) {
        super(diffCallback);
        this.onItemClickListener=onItemClickListener;
    }

    @NonNull
    @Override
    public DeviceviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.device_list_item,parent,false);
        return new DeviceviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceviewHolder holder, int position) {
        holder.bind(getItem(position));
    }

     class DeviceviewHolder extends RecyclerView.ViewHolder{

        private TextView deviceName,deviceAddress;
        public DeviceviewHolder(@NonNull View itemView) {
            super(itemView);
            deviceName=itemView.findViewById(R.id.deviceName);
            deviceAddress=itemView.findViewById(R.id.deviceAddress);
            itemView.setOnClickListener(view->{
                onItemClickListener.onItemClick(getItem(getAdapterPosition()));
            });
        }
        public void bind(BluetoothDevice device){
            deviceName.setText(device.getName());
            deviceAddress.setText(device.getAddress());
        }
    }

}
