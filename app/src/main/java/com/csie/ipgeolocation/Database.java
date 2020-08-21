package com.csie.ipgeolocation;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

@androidx.room.Database(entities = {IP.class},version = 1,exportSchema = false)
public abstract class Database extends RoomDatabase {

    private static Database instance;

    public static Database getInstance(Context conext){
        if(instance==null){
            instance = Room.databaseBuilder(conext,Database.class,"IPGeolocation.db")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract DAO getDAO();
}
