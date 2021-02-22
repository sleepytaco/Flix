package com.abukh.flix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import okhttp3.Headers;

import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abukh.flix.databinding.ActivityDetailBinding;
import com.abukh.flix.models.Movie;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

public class DetailActivity extends YouTubeBaseActivity {

    private static final String YOUTUBE_API_KEY = // YOUR API KEY HERE;
    private static final String VIDEOS_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    private int isPopularMovie = 0;
    TextView tvTitle;
    TextView tvOverview;
    RatingBar ratingBar;

    YouTubePlayerView youTubePlayerView;
    private ActivityDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_detail);

        // Inflate the `activity_detail` layout
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        youTubePlayerView = findViewById(R.id.player);

        // get data from the MainActivity
        Movie movie = Parcels.unwrap(getIntent().getParcelableExtra("movie"));

        binding.setMovie(movie);

        if (movie.getRating() > 7) {
            isPopularMovie = 1;
        }

        // from the movie db API we want to get the video id of the trailer for the movie
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format(VIDEOS_URL, movie.getMovieID()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                try {
                    JSONArray results = json.jsonObject.getJSONArray("results");

                    if (results.length() == 0) {
                        initializeYoutube("");
                        return; // ie, no trailers found for the movie
                    }

                    String youtubeKey = results.getJSONObject(0).getString("key");

                    initializeYoutube(youtubeKey);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        //To support reverse transitions when user clicks the device back button
        finishAfterTransition();
    }

    private void initializeYoutube(String youtubeKey) {

        if (youtubeKey.equals("")) {
            Toast.makeText(this, "Uh-oh! No trailer found for this movie :(", Toast.LENGTH_LONG).show();
            return;
        }

        youTubePlayerView.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                if (isPopularMovie == 1) { // play video automatically for popular movies
                    youTubePlayer.loadVideo(youtubeKey);
                    return;
                }
                youTubePlayer.cueVideo(youtubeKey);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });

    }
}
