package com.aihao.libnetwork.cache;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.aihao.libcommon.global.AppGlobals;

@Database(entities = {Cache.class},version = 1,exportSchema = true)
public abstract class CacheDatabase extends RoomDatabase {

    private static final CacheDatabase database;
    static  {
        // Database新規
        // Room.inMemoryDatabaseBuilder()
        database = Room.databaseBuilder(AppGlobals.getApplication(),CacheDatabase.class,"ppjoke_cache")
                .allowMainThreadQueries()
                .build();
                //.fallbackToDestructiveMigration()

    }

    public abstract CacheDao getCache();

    public static CacheDatabase get() {
        return database;
    }
}
