package com.jaen.pedro.yukipelis.DataBase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.jaen.pedro.yukipelis.Item.Genero;
import com.jaen.pedro.yukipelis.Item.Movie;

@Database(entities = {Genero.class,Movie.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {
    public abstract GeneroDao generoDao();
    public abstract MovieDao movieDao();
    private static AppDataBase INSTANCE=null;

    public static AppDataBase getInstance(final Context context){
        if(INSTANCE==null){
            INSTANCE=Room.databaseBuilder(context.getApplicationContext(),AppDataBase.class, "yukipelis").fallbackToDestructiveMigration().build();
        }
        return INSTANCE;
    }
}
