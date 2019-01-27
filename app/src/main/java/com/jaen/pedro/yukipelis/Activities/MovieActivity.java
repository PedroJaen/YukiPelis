package com.jaen.pedro.yukipelis.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.jaen.pedro.yukipelis.Adapter.MovieListAdapter;
import com.jaen.pedro.yukipelis.Fragments.MovieFragment;
import com.jaen.pedro.yukipelis.Item.Movie;
import com.jaen.pedro.yukipelis.R;

public class MovieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Movie movie=(Movie) getIntent().getExtras().get("movie");

        MovieFragment fragment=(MovieFragment)getSupportFragmentManager().findFragmentById(R.id.activity_movie_fragment);
        if(fragment!=null){
            fragment.fillFragment(movie);
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

}
