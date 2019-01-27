package com.jaen.pedro.yukipelis;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaen.pedro.yukipelis.Activities.FavoriteActivity;
import com.jaen.pedro.yukipelis.Activities.MovieActivity;
import com.jaen.pedro.yukipelis.Activities.SearchActivity;
import com.jaen.pedro.yukipelis.Adapter.MovieListAdapter;
import com.jaen.pedro.yukipelis.DataBase.AppDataBase;
import com.jaen.pedro.yukipelis.Fragments.ListFragment;
import com.jaen.pedro.yukipelis.Fragments.MovieFragment;
import com.jaen.pedro.yukipelis.Item.Genero;
import com.jaen.pedro.yukipelis.Item.Movie;
import com.jaen.pedro.yukipelis.JSONParser.GeneroParser;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements ListFragment.OnMovieSent{

    TextView hidden;
    AppDataBase db;
    LinearLayout linearList;
    LinearLayout linearMovie;

    private final String CARTELERAURL="https://api.themoviedb.org/3/movie/now_playing?api_key=e50d63dbcb5c1a6c703ea83cfed8cb7c";
    private final String GENEROSURL="https://api.themoviedb.org/3/genre/movie/list?api_key=e50d63dbcb5c1a6c703ea83cfed8cb7c";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //comprobar si hay internet
        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info=connectivityManager.getActiveNetworkInfo();
        boolean connected=info !=null && info.isConnected();

        //hidden view
        hidden=findViewById(R.id.main_hidden);

        //db
        db=AppDataBase.getInstance(this);

        //linear
        linearList=findViewById(R.id.main_linear_list);
        linearMovie=findViewById(R.id.main_linear_movie);

        //fragments
        ListFragment listFragment= (ListFragment) getSupportFragmentManager().findFragmentById(R.id.activity_list_fragment);

        if(connected){
            //nos deshacemos de hidden
            hidden.setVisibility(View.GONE);
            //obtenemos los generos
            GetAsyncGenres generos=new GetAsyncGenres();
            generos.execute(GENEROSURL);
            //iniciamos el fragment de las pelis
            listFragment.fillFragment(CARTELERAURL);
        }
        else{
            linearList.setVisibility(View.GONE);
            linearMovie.setVisibility(View.GONE);
            hidden.setText(R.string.noInternet);
        }

    }

    //metodos del menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            Intent intent=new Intent(getBaseContext(),SearchActivity.class);
            startActivity(intent);
            return true;
        }else if(id==R.id.action_favorites){
            Intent intent=new Intent(getBaseContext(),FavoriteActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //callbacks
    @Override
    public void onSent(Movie movie) {
        MovieFragment movieFragment= (MovieFragment) getSupportFragmentManager().findFragmentById(R.id.activity_movie_fragment);
        if(movieFragment!=null){
            movieFragment.fillFragment(movie);
        }
        else{
            Intent intent=new Intent(this,MovieActivity.class);
            intent.putExtra("movie",movie);
            startActivity(intent);
        }
    }

    //metodos para llamar al json
    private class GetAsyncGenres extends AsyncTask<String,Void,Void>{
        @Override
        protected Void doInBackground(String... strings) {
            String language="&language="+Locale.getDefault().toString();
            List<Genero> result = GeneroParser.fetchGeneros(strings[0]+language);

            for(Genero g:result){
                long idG=g.getId();
                Genero generoDB=db.generoDao().getGenero(idG);
                if(generoDB!=null){
                    //existe en la bd este genero
                    continue;
                }
                else{
                    //guardamos el genero
                    db.generoDao().insertGenero(g);
                }
            }
            return null;
        }

    }
}
