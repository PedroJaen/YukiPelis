package com.jaen.pedro.yukipelis.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jaen.pedro.yukipelis.Adapter.MovieListAdapter;
import com.jaen.pedro.yukipelis.DataBase.AppDataBase;
import com.jaen.pedro.yukipelis.Fragments.ListFragment;
import com.jaen.pedro.yukipelis.Item.Movie;
import com.jaen.pedro.yukipelis.JSONParser.JSONParser;
import com.jaen.pedro.yukipelis.MainActivity;
import com.jaen.pedro.yukipelis.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity implements ListFragment.OnMovieSent{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //obtenemos la url pasada por intent si la mandan
        Intent intent=getIntent();
        if(intent!=null){
            String url=intent.getStringExtra("url");

            ListFragment fragment= (ListFragment) getSupportFragmentManager().findFragmentById(R.id.activity_list_fragment);
            if(fragment!=null){
                fragment.fillFragment(url);
            }

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
        Intent intent=new Intent(this,MovieActivity.class);
        intent.putExtra("movie",movie);
        startActivity(intent);
    }
}
