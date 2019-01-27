package com.jaen.pedro.yukipelis.Adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jaen.pedro.yukipelis.Item.Movie;
import com.jaen.pedro.yukipelis.R;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder>
            implements Serializable{
    private Context context;
    private List<Movie> listaM;
    private OnItemClickListener listener;
    int layout;

    public MovieListAdapter(List<Movie> listaM, int layout, Context context, OnItemClickListener listener) {
        this.context = context;
        this.listaM = listaM;
        this.listener = listener;
        this.layout=layout;
    }

    public Context getContext() {
        return context;
    }

    @NonNull
    @Override
    public MovieListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieListAdapter.ViewHolder viewHolder, final int i) {
        final Movie movie=listaM.get(i);

        if(movie.getPoster().length()==4){
            Picasso.get().load(R.mipmap.ic_launcher).into(viewHolder.poster);
        }
        else{
            Picasso.get().load("https://image.tmdb.org/t/p/w200"+movie.getPoster()).into(viewHolder.poster);
        }
        viewHolder.titulo.setText(movie.getTitulo());
        if(movie.isFavorito()){
            Picasso.get().load(android.R.drawable.btn_star_big_on).into(viewHolder.favorito);
        }
        else{
            Picasso.get().load(android.R.drawable.btn_star_big_off).into(viewHolder.favorito);
        }
        viewHolder.favorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnItemClickListener(v,i,movie);
            }
        });
        viewHolder.contenedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnItemClickListener(v,i,movie);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaM.size();
    }

    public void clear() {
        listaM.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Movie> movies) {
        listaM=movies;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void OnItemClickListener(View v, int position, Movie movie);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView poster;
        TextView titulo;
        ImageView favorito;
        ImageView gato;
        LinearLayout contenedor;
        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            contenedor=itemView.findViewById(R.id.row_row);
            poster=itemView.findViewById(R.id.row_poster);
            titulo=itemView.findViewById(R.id.row_titulo);
            favorito=itemView.findViewById(R.id.row_favorite);
            gato=itemView.findViewById(R.id.list_gato);
            progressBar=itemView.findViewById(R.id.list_progressBar);
        }
    }

}
