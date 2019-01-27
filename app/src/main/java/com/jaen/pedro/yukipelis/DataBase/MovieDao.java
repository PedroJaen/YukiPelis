package com.jaen.pedro.yukipelis.DataBase;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.jaen.pedro.yukipelis.Item.Movie;

import java.util.List;

@Dao
public interface MovieDao {
    @Insert
    public long insertMovie(Movie movie);

    @Update
    public int updateMovie(Movie movie);

    @Delete
    public int deleteMovie(Movie movie);

    @Query("SELECT * FROM movies WHERE tmdbId=:id")
    public Movie getMovie(long id);

    @Query("SELECT * FROM movies")
    public LiveData<List<Movie>> getAllMovies();
}
