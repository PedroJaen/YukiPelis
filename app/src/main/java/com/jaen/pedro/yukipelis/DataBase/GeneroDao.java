package com.jaen.pedro.yukipelis.DataBase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.jaen.pedro.yukipelis.Item.Genero;

import java.util.List;

@Dao
public interface GeneroDao {
    @Insert
    public long insertGenero(Genero genero);

    @Update
    public int updateGenero(Genero genero);

    @Delete
    public int deleteGenero(Genero genero);

    @Query("SELECT * FROM generos WHERE id=:id")
    public Genero getGenero(long id);

    @Query("SELECT * FROM generos")
    public List<Genero> getAllGeneros();

    @Query("SELECT nombre FROM generos")
    public List<String> getAllGenerosNombre();

    @Query("SELECT id FROM generos WHERE nombre=:nombre")
    public long getGenroId(String nombre);

}
