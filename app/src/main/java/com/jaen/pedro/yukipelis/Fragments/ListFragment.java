package com.jaen.pedro.yukipelis.Fragments;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jaen.pedro.yukipelis.Adapter.MovieListAdapter;
import com.jaen.pedro.yukipelis.DataBase.AppDataBase;
import com.jaen.pedro.yukipelis.Item.Movie;
import com.jaen.pedro.yukipelis.JSONParser.JSONParser;
import com.jaen.pedro.yukipelis.R;
import com.jaen.pedro.yukipelis.ViewModel.JsonViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {

    AppDataBase db;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    MovieListAdapter movieListAdapter;
    List<Movie> listaMovies;
    ImageView gato;
    TextView hidden;
    ProgressBar progressBar;
    Context context;
    JsonViewModel model;

    OnMovieSent callback;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_list, container, false);

        //obtener las referencias
        //hidden view
        hidden=view.findViewById(R.id.list_hidden);
        //cartelera
        listaMovies=new ArrayList<>();
        //db
        db=AppDataBase.getInstance(getContext());
        gato=view.findViewById(R.id.list_gato);
        progressBar=view.findViewById(R.id.list_progressBar);

        //configuramos el recyclerview
        recyclerView=view.findViewById(R.id.list_cartelera);
        layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        context=view.getContext();

        return view;
    }

    public void fillFragment(String url){
        Picasso.get().load(R.mipmap.ic_launcher).into(gato);

        movieListAdapter =new MovieListAdapter(listaMovies,R.layout.row_item,context, new MovieListAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(View v, int position, Movie movie) {
                if(v.getId()==R.id.row_favorite){
                    if(movie.isFavorito()){
                        //borrar peli de activity_favoritos
                        model.delMovie(movie);
                    }
                    else{
                        //añadir peli a activity_favoritos
                        model.addMovie(movie);

                    }
                    movie.setFavorito();
                    movieListAdapter.notifyItemChanged(position);
                }
                else{
                    callback.onSent(movie);

                }

            }
        });

        recyclerView.setAdapter(movieListAdapter);

        model=ViewModelProviders.of(this).get(JsonViewModel.class);

        model.getListaPelis(url,getContext()).observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                gato.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);

                movieListAdapter.addAll(movies);
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            callback = (OnMovieSent) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+ " Debería implementar el interfaz OnNameSent");
        }
    }

    public interface OnMovieSent{
        public void onSent(Movie movie);
    }

}
