package com.jaen.pedro.yukipelis.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jaen.pedro.yukipelis.Adapter.MovieListAdapter;
import com.jaen.pedro.yukipelis.DataBase.AppDataBase;
import com.jaen.pedro.yukipelis.Item.Movie;
import com.jaen.pedro.yukipelis.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieFragment extends Fragment {

    ImageView poster;
    ImageView gato;
    TextView titulo;
    TextView titulo_original;
    TextView year;
    TextView duracion;
    TextView generos;
    TextView descripcion;
    EditText comentarios;
    Button aceptar;
    Button web;
    AppDataBase db;
    LinearLayout movieData;

    public MovieFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.movie, container, false);

        //db
        db=AppDataBase.getInstance(getContext());

        //obtenemos las referencias
        poster=view.findViewById(R.id.movie_poster);
        titulo=view.findViewById(R.id.movie_titulo);
        titulo_original=view.findViewById(R.id.movie_titulo_original);
        year=view.findViewById(R.id.movie_year);
        duracion=view.findViewById(R.id.movie_duracion);
        generos=view.findViewById(R.id.movie_generos);
        descripcion=view.findViewById(R.id.movie_descripcion);
        comentarios=view.findViewById(R.id.movie_comentarios);
        aceptar=view.findViewById(R.id.movie_aceptar);
        web=view.findViewById(R.id.movie_web);
        movieData=view.findViewById(R.id.movie_data);
        gato=view.findViewById(R.id.movie_gato);

        Picasso.get().load(R.mipmap.ic_launcher).into(gato);

        movieData.setVisibility(View.GONE);

        return view;
    }

    public void fillFragment(final Movie movie){
        movieData.setVisibility(View.VISIBLE);
        gato.setVisibility(View.GONE);

        //parte principal
        Picasso.get().load("https://image.tmdb.org/t/p/w200"+movie.getPoster()).into(poster);
        titulo.setText(movie.getTitulo());
        titulo_original.setText(movie.getTitulo_original());
        year.setText(movie.getYear());
        duracion.setText(movie.getDuracion()+" "+getText(R.string.minutes));
        generos.setText(movie.getGenerosString());
        if(movie.getWeb().length()==0){
            web.setVisibility(View.INVISIBLE);
        }
        else{
            web.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri=Uri.parse(movie.getWeb());
                    Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(intent);
                }
            });
        }
        //parte secundaria
        if(movie.getDescripcion().length()==0){
            descripcion.setText(getString(R.string.noDescripcion));
        }
        else{
            descripcion.setText(movie.getDescripcion());
        }
        if(movie.isFavorito()){
            comentarios.setText(movie.getComentarios(),TextView.BufferType.EDITABLE);
            aceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    movie.setComentarios(comentarios.getText().toString());
                    UpdateAsynkMovie update=new UpdateAsynkMovie();
                    update.execute(movie);
                }
            });
        }
        else{
            comentarios.setVisibility(View.GONE);
            aceptar.setVisibility(View.GONE);
        }

    }

    private class UpdateAsynkMovie extends AsyncTask<Movie,Void,Integer>{
        @Override
        protected Integer doInBackground(Movie... movies) {
            int res=db.movieDao().updateMovie(movies[0]);
            return res;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            //super.onPostExecute(integer);
            if(integer==0){
                Toast.makeText(getContext(), R.string.noUpdate, Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getContext(), R.string.movieUpdated, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
