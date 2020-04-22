package com.mobiona.bluetrace.repository.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {NearByDevice.class},version = 1)
public abstract class DeviceDataBase extends RoomDatabase {

    public  abstract DeviceDAO geDeviceDAO();

    private static volatile DeviceDataBase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static DeviceDataBase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DeviceDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            DeviceDataBase.class, "device_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
