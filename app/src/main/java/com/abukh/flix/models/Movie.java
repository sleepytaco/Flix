package com.abukh.flix.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Movie {

    private Integer movieID;
    private String posterPath;
    private String backdropPath;
    private String title;
    private String overview;
    private double rating;

    public Movie() {
        // this empty constructor is needed by the Parceler Library
    }

    public Movie(JSONObject jsonObject) throws JSONException {
        movieID = jsonObject.getInt("id");
        posterPath = jsonObject.getString("poster_path");
        backdropPath = jsonObject.getString("backdrop_path");
        title =  jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        rating = jsonObject.getDouble("vote_average");
    }

    public static List<Movie> fromJSONArray(JSONArray movieJsonArray) throws JSONException {
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < movieJsonArray.length(); i++) {
            movies.add(new Movie(movieJsonArray.getJSONObject(i)));
        }
        return movies;
    }

    public String getPosterPath() {
        return "https://image.tmdb.org/t/p/w342/" + posterPath;
    }

    public String getBackdropPath() {
        return "https://image.tmdb.org/t/p/w342/" + backdropPath;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public double getRating() {
        return rating;
    }

    public Integer getMovieID() {
        return movieID;
    }
}
