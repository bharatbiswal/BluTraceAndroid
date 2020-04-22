package com.mobiona.bluetrace.repository.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;


@Dao
public interface DeviceDAO {

    @Insert
    void addDevice(NearByDevice device);

    @Query("Select * from NearByDevice")
    LiveData<List<NearByDevice>>getDevices();
}
