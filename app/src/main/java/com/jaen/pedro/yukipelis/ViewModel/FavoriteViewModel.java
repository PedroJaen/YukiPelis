package com.jaen.pedro.yukipelis.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.jaen.pedro.yukipelis.DataBase.AppDataBase;
import com.jaen.pedro.yukipelis.Item.Movie;
import com.jaen.pedro.yukipelis.R;

import java.util.List;

public class FavoriteViewModel extends AndroidViewModel {

    private LiveData<List<Movie>> listaPelis;
    private static AppDataBase db;

    public FavoriteViewModel(@NonNull Application application) {
        super(application);
        db=AppDataBase.getInstance(application);
    }

    public LiveData<List<Movie>> getListaPelis() {
        getMovies();
        return listaPelis;
    }

    //metodos bd
    public void addMovie(Movie movie){
        new AddAsyncMovie().execute(movie);

    }

    public void delMovie(Movie movie){
        new DelAsyncMovie().execute(movie);
    }

    public void getMovies(){
        listaPelis=db.movieDao().getAllMovies();
    }

    public void updateMovie(Movie movie) {
        new UpdateAsyncMovie().execute(movie);
    }

    //asynctask
    private class AddAsyncMovie extends AsyncTask<Movie,Void,Long> {

        @Override
        protected Long doInBackground(Movie... movies) {
            long id=db.movieDao().insertMovie(movies[0]);
            return id;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            //super.onPostExecute(aLong);
            if(aLong>=0){
                Toast.makeText(getApplication(), R.string.movieFavs, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class DelAsyncMovie extends AsyncTask<Movie,Void,Integer>{

        @Override
        protected Integer doInBackground(Movie... movies) {
            int rows=db.movieDao().deleteMovie(movies[0]);
            return rows;
        }

        @Override
        protected void onPostExecute(Integer rows) {
            //super.onPostExecute(rows);

            if(rows>0){
                Toast.makeText(getApplication(), R.string.movieDel, Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplication(), R.string.noMovieAdded, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class UpdateAsyncMovie extends AsyncTask<Movie,Void,Integer>{

        @Override
        protected Integer doInBackground(Movie... movies) {
            return db.movieDao().updateMovie(movies[0]);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            //super.onPostExecute(integer);

        }
    }
}
