package com.jaen.pedro.yukipelis.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.jaen.pedro.yukipelis.Adapter.MovieListAdapter;
import com.jaen.pedro.yukipelis.DataBase.AppDataBase;
import com.jaen.pedro.yukipelis.Fragments.ListSearchFragment;
import com.jaen.pedro.yukipelis.Fragments.MovieFragment;
import com.jaen.pedro.yukipelis.Fragments.SearchFragment;
import com.jaen.pedro.yukipelis.Item.Movie;
import com.jaen.pedro.yukipelis.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SearchActivity extends AppCompatActivity
        implements SearchFragment.OnStringSent,ListSearchFragment.OnMovieSent{

    LinearLayout menu;
    LinearLayout movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        menu=findViewById(R.id.fragment_menu);
        movie=findViewById(R.id.fragment_movie);

        if(movie!=null){
            movie.setVisibility(View.GONE);
        }


        SearchFragment fragment=(SearchFragment)getSupportFragmentManager().findFragmentById(R.id.activity_search_fragment);
        if(fragment!=null){
            fragment.fillFragment();
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

    @Override
    public void onSent(String url) {
        ListSearchFragment listSearchFragment=(ListSearchFragment) getSupportFragmentManager().findFragmentById(R.id.activity_searchlist_fragment);
        if(listSearchFragment!=null){
            listSearchFragment.fillFragment(url);
        }
        else{
            Intent intent=new Intent(this,SearchListActivity.class);
            intent.putExtra("url",url);
            startActivity(intent);
        }
    }

    @Override
    public void onSent(Movie movie) {
        MovieFragment movieFragment= (MovieFragment) getSupportFragmentManager().findFragmentById(R.id.activity_movie_fragment);
        if(movieFragment!=null){
            menu.setVisibility(View.GONE);
            this.movie.setVisibility(View.VISIBLE);
            movieFragment.fillFragment(movie);
        }
        else{
            Intent intent=new Intent(this,MovieActivity.class);
            intent.putExtra("movie",movie);
            startActivity(intent);
        }

    }
}
