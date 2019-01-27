package com.jaen.pedro.yukipelis.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jaen.pedro.yukipelis.DataBase.AppDataBase;
import com.jaen.pedro.yukipelis.Item.Movie;
import com.jaen.pedro.yukipelis.JSONParser.JSONParser;
import com.jaen.pedro.yukipelis.R;

import java.util.List;

public class SearchViewModel extends AndroidViewModel {
    private static MutableLiveData<List<Movie>> listaPelis;
    private static Application application;
    private static AppDataBase db;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        this.application=application;
        db=AppDataBase.getInstance(application);
    }

    public LiveData<List<Movie>> getListaPelis(String url, Context context) {
        if(listaPelis==null){
            listaPelis=new MutableLiveData<>();
            loadMovies(url,context);
        }
        return listaPelis;
    }

    //carga json
    private void loadMovies(String url, final Context context){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(application);

        Uri baseUri = Uri.parse(url);

        final RequestQueue requestQueue = Volley.newRequestQueue(application);
        StringRequest request = new StringRequest(Request.Method.GET, baseUri.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GetJsonData jsonData=new GetJsonData(context);
                jsonData.execute(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);
    }

    //parseamos el json

    public class GetJsonData extends AsyncTask<String,Void,List<Movie>>{

        Context context;

        public GetJsonData(Context context) {
            super();
            this.context=context;
        }

        @Override
        protected List<Movie> doInBackground(String... strings) {
            return JSONParser.extractIdFromJson(strings[0],context);
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            //super.onPostExecute(movies);
            listaPelis.setValue(movies);
        }
    }

    //metodos bd
    public void addMovie(Movie movie){
        new AddAsyncMovie().execute(movie);
    }

    public void delMovie(Movie movie){
        new DelAsyncMovie().execute(movie);
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

    private class DelAsyncMovie extends AsyncTask<Movie,Void,Integer> {

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
