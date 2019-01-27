package com.jaen.pedro.yukipelis.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.jaen.pedro.yukipelis.Activities.SearchListActivity;
import com.jaen.pedro.yukipelis.Adapter.MovieListAdapter;
import com.jaen.pedro.yukipelis.DataBase.AppDataBase;
import com.jaen.pedro.yukipelis.Item.Movie;
import com.jaen.pedro.yukipelis.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SearchFragment extends Fragment implements AdapterView.OnItemSelectedListener,
        MovieListAdapter.OnItemClickListener{

    CheckBox checkTitulo;
    EditText titulo;
    CheckBox checkGenero;
    Spinner genero;
    CheckBox checkYear;
    Spinner year;
    CheckBox checkRate;
    Spinner rate;
    Button aceptar;
    AppDataBase db;
    List<String> generosString;
    List<Integer> yearsString;
    List<Integer> rateString;
    ArrayAdapter<String> generosAdapter;
    ArrayAdapter<Integer> yearsAdapter;
    ArrayAdapter<Integer> rateAdapter;
    String generoSelected;
    int rateSelected;
    long idGenero;
    int yearSelected;
    String base="";
    List<Movie> resultados;

    OnStringSent callback;

    public SearchFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_search_menu, container, false);

        //obtenemos los elementos
        checkTitulo=view.findViewById(R.id.busqueda_checkBox_titulo);
        titulo=view.findViewById(R.id.busqueda_edittext_titulo);
        checkGenero=view.findViewById(R.id.busqueda_checkBox_genero);
        genero=view.findViewById(R.id.busqueda_spinner_genero);
        checkYear=view.findViewById(R.id.busqueda_checkBox_year);
        year=view.findViewById(R.id.busqueda_spinner_year);
        checkRate=view.findViewById(R.id.busqueda_checkBox_rate);
        rate=view.findViewById(R.id.busqueda_spinner_rate);
        aceptar=view.findViewById(R.id.busqueda_button);
        db=AppDataBase.getInstance(getContext());
        resultados=new ArrayList<>();

        return view;
    }

    public void fillFragment(){
        //checkTitulo
        checkTitulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkGenero.setChecked(false);
                checkYear.setChecked(false);
                checkRate.setChecked(false);
            }
        });
        //otrosCheck
        checkGenero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkTitulo.setChecked(false);
            }
        });
        checkYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkTitulo.setChecked(false);
            }
        });
        checkRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkTitulo.setChecked(false);
            }
        });

        //spinner genero
        genero.setOnItemSelectedListener(this);
        generosString=new ArrayList<>();
        GetAsyncGeneros generosAs=new GetAsyncGeneros();
        generosAs.execute();

        //spinner year
        year.setOnItemSelectedListener(this);
        yearsString=new ArrayList<>();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy");
        int fecha = Integer.parseInt(dtf.format(LocalDate.now()));
        for(int i=fecha;i>=1874;i--){
            yearsString.add(i);
        }
        yearsAdapter=new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,yearsString);
        year.setAdapter(yearsAdapter);

        //spinner de rate
        rate.setOnItemSelectedListener(this);
        rateString=new ArrayList<>();
        for(int i=10;i>=1;i--){
            rateString.add(i);
        }
        rateAdapter=new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,rateString);
        rate.setAdapter(rateAdapter);

        //listener aceptar
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //para la activity_search
                //o por titulo
                if(checkTitulo.isChecked()==true){
                    base="https://api.themoviedb.org/3/search/movie?api_key=YOUR APY KEY";
                    if(titulo.length()!=0){
                        base+="&query="+titulo.getText();
                    }
                }
                else{
                    base="https://api.themoviedb.org/3/discover/movie?api_key=YOUR APY KEY";
                    //o por campos
                    if(checkGenero.isChecked()==true){
                        GetAsynkId asynk=new GetAsynkId();
                        asynk.execute(generoSelected);
                        //nos aseguramos que termine antes de continuar
                        try {
                            asynk.get();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        base+="&with_genres="+idGenero;
                    }
                    if(checkYear.isChecked()==true){
                        base+="&primary_release_year="+yearSelected;
                    }
                    if(checkRate.isChecked()==true){
                        base+="&vote_average.gte=";
                    }
                }

                base+="&sort_by=primary_release_date.asc";

                callback.onSent(base);

            }
        });
    }

    //metodos del spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(parent.getId()){
            case R.id.busqueda_spinner_genero:
                generoSelected=(String)genero.getSelectedItem();
                break;
            case R.id.busqueda_spinner_year:
                yearSelected=(int)year.getSelectedItem();
                break;
            case R.id.busqueda_spinner_rate:
                rateSelected=(int)rate.getSelectedItem();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //metodo del listener de cada fila ¿necesario?
    @Override
    public void OnItemClickListener(View v, int position, Movie movie) {

    }

    //metodos para la base de datos
    private class GetAsyncGeneros extends AsyncTask<Void,Void,List<String>>{

        @Override
        protected List<String> doInBackground(Void... voids) {
            return db.generoDao().getAllGenerosNombre();
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            //super.onPostExecute(strings);
            generosString=strings;
            generosAdapter=new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,generosString);
            genero.setAdapter(generosAdapter);
        }
    }

    private class GetAsynkId extends AsyncTask<String,Void,Long>{
        @Override
        protected Long doInBackground(String... strings) {
            long id=db.generoDao().getGenroId(strings[0]);
            return id;
        }

        @Override
        protected void onPostExecute(Long id) {
            //super.onPostExecute(integer);
            idGenero=id;
        }
    }

    //callbacks
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            callback= (OnStringSent) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+ " Debería implementar el interfaz OnNameSent");
        }
    }

    public interface OnStringSent{
        public void onSent(String url);
    }
}
