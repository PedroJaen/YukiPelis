package com.jaen.pedro.yukipelis.Item;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;

@Entity(tableName = "movies")
public class Movie implements Serializable {
    @PrimaryKey
    private long tmdbId;

    private String titulo;

    private String titulo_original;

    private String descripcion;

    private String comentarios;

    private String duracion;

    private String poster;

    private String year;

    private String web;

    private String generosString;

    private boolean favorito=false;

    public Movie() {}

    public long getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(long tmdbId) {
        this.tmdbId = tmdbId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public boolean isFavorito() {
        return this.favorito;
    }

    public void setFavorito() {
        if(favorito){
            favorito=false;
        }
        else{
            favorito=true;
        }
    }

    public void setFavorito(boolean favorito){
        this.favorito=favorito;
    }

    public String getTitulo_original() {
        return titulo_original;
    }

    public void setTitulo_original(String titulo_original) {
        this.titulo_original = titulo_original;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getGenerosString() {
        return generosString;
    }

    public void setGenerosString(String generosString) {
        this.generosString = generosString;
    }
}
