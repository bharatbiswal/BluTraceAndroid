package com.mobiona.bluetrace.repository.database;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class NearByDevice {

    @PrimaryKey
    @NonNull
    private String deviceUUID;
    private long lastContactTimestamp;
    private long distance;
    private String deviceName;

    public NearByDevice(@NonNull String deviceUUID, long lastContactTimestamp, long distance, String deviceName) {
        this.deviceUUID = deviceUUID;
        this.lastContactTimestamp = lastContactTimestamp;
        this.distance = distance;
        this.deviceName = deviceName;
    }

    public String getDeviceUUID() {
        return deviceUUID;
    }

    public long getLastContactTimestamp() {
        return lastContactTimestamp;
    }

    public long getDistance() {
        return distance;
    }

    public String getDeviceName() {
        return deviceName;
    }
}
