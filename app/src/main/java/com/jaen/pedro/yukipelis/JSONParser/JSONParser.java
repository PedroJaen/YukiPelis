package com.jaen.pedro.yukipelis.JSONParser;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.jaen.pedro.yukipelis.DataBase.AppDataBase;
import com.jaen.pedro.yukipelis.Item.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class JSONParser {

    public JSONParser() {}

    //creamos un objeto URL valido
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e("Util.java", "Problem building the URL ", e);
        }
        return url;
    }

    //mandamos el url y obtenemos el json
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("Util.java", "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e("Util.java", "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    //convertimos el flujo a string
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    //parseamos el json
    public static ArrayList<Movie> extractIdFromJson(String earthquakeJSON,Context context) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(earthquakeJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        List<Movie> peliculas = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(earthquakeJSON);

            // Extract the JSONArray associated with the key called "features",
            // which represents a list of features (or earthquakes).
            JSONArray moviesArray = baseJsonResponse.getJSONArray("results");

            // recorremos las peliculas
            for (int i = 0; i < moviesArray.length(); i++) {

                // pelicula actual
                JSONObject currentMovie = moviesArray.getJSONObject(i);

                // idTMDB
                String tmdbId=currentMovie.getString("id");

                //comprobamos si la pelicula esta en la bd
                AppDataBase db=AppDataBase.getInstance(context);
                Movie movieDB=db.movieDao().getMovie(currentMovie.getLong("id"));
                if(movieDB!=null){
                    peliculas.add(movieDB);
                }
                else{
                    //rellenamos la pelicula
                    String language="&language="+Locale.getDefault().toString();
                    String query="https://api.themoviedb.org/3/movie/"+tmdbId+"?api_key=YOUR APY KEY"+language;
                    Movie movie=fetchMovie(query);

                    // Add the new {@link Earthquake} to the list of earthquakes.
                    peliculas.add(movie);
                }

            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return (ArrayList<Movie>) peliculas;
    }

    //main
    public static List<Movie> fetchMovies(String requestUrl,Context context) {
        Log.d("fetchEarthquakeData","cargaloader");
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e("Util.java", "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        List<Movie> peliculas = extractIdFromJson(jsonResponse,context);

        //convert to livedata
        //MutableLiveData<List<Movie>> movieLiveData = new MutableLiveData<>();
        //movieLiveData.postValue(peliculas);

        // Return the list of {@link Earthquake}s
        return peliculas;
    }

    //metodos especiales para obtener TODA la info de la peli
    //parseamos el json
    public static Movie extractFeatureFromJson(String earthquakeJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(earthquakeJSON)) {
            return null;
        }

        //pelicula nueva vacia
        Movie movie=new Movie();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(earthquakeJSON);

            // genero
            ArrayList<String> generos=new ArrayList<>();
            JSONArray generosArray = baseJsonResponse.getJSONArray("genres");
            for (int j=0;j<generosArray.length();j++){
                JSONObject currentGener=generosArray.getJSONObject(j);
                generos.add(currentGener.getString("name"));
            }
            movie.setGenerosString(String.join(", ",generos));

            //web
            movie.setWeb(baseJsonResponse.getString("homepage"));

            //id
            movie.setTmdbId(baseJsonResponse.getLong("id"));

            //titulo original
            movie.setTitulo_original(baseJsonResponse.getString("original_title"));

            //descripcion
            movie.setDescripcion(baseJsonResponse.getString("overview"));

            //poster
            movie.setPoster(baseJsonResponse.getString("poster_path"));

            //year
            movie.setYear(baseJsonResponse.getString("release_date"));

            //duracion
            movie.setDuracion(baseJsonResponse.getString("runtime"));

            //titulo
            movie.setTitulo(baseJsonResponse.getString("title"));

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return movie;
    }

    //main
    public static Movie fetchMovie(String requestUrl) {

        Log.d("fetchEarthquakeData","cargaloader");
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e("Util.java", "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        return extractFeatureFromJson(jsonResponse);
    }
}
