package org.supremus.sych.sychnews.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
@Database(entities = {NewsEntity.class}, version = 2)
public abstract class NewsDB extends RoomDatabase {
    public abstract NewsDAO newsDAO();
    private static NewsDB singleton;

    private static final String DATABASE_NAME = "FilmRoomDb.db";

    public static NewsDB getAppDatabase(Context context) {
        if (singleton == null) {
            synchronized (NewsDB.class) {
                if (singleton == null) {
                    singleton = Room.databaseBuilder(context.getApplicationContext(),
                            NewsDB.class,
                            DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return singleton;
    }
}
