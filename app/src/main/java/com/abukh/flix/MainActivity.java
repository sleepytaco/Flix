package com.abukh.flix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Headers;

import android.os.Bundle;
import android.util.Log;

import com.abukh.flix.models.Movie;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    public static final String TAG = "MainActivity";

    private List<Movie> movies;
    RecyclerView rvMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movies = new ArrayList<>(); // our dataset is currently empty

        rvMovies = findViewById(R.id.rvMovies);

        // create the adapter
        MovieAdapter movieAdapter = new MovieAdapter(this, movies); // initially we pass in empty dataset

        // set the adapter on the RV
        rvMovies.setAdapter(movieAdapter);

        // set a layout manager on the RV (this is required by the RV in order to be able to know how to order different Views onto the screen)
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, Headers headers, JSON json) {
                        JSONObject jsonObject = json.jsonObject;

                        try {
                            JSONArray results = jsonObject.getJSONArray("results");

                            Log.i(TAG, "Results: " + results.toString());

                            // movies = Movie.fromJSONArray(results); dont do this to add movies as adapter will be pointing to something else
                            movies.addAll(Movie.fromJSONArray(results));

                            // whenever the dataset changes, let the adapter know
                            movieAdapter.notifyDataSetChanged();

                            Log.i(TAG, "Movies: " + movies.size());

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, "Hit JSON exception");
                        }

                        Log.d(TAG, "onSuccess");
                    }

                    @Override
                    public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                        Log.d(TAG, "onFailure");
                    }
                }
        );
    }
}