package com.abukh.flix.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abukh.flix.R;
import com.abukh.flix.models.Movie;
import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    Context context; // context to where the rv is actually being created
    List<Movie> movies; // our model

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    // Inflates a layout from XML (in our case item_movie.xml) and returns it inside the ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);

        return new ViewHolder(movieView); // wrap the movieView inside our ViewHolder and return it
    }

    // Involves populating the data into the item through the ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // we have the 'position' of the data so we take the data at that position and put it into the View contained inside the ViewHolder

        // get the movie at the position
        Movie movie = movies.get(position);

        // bind the movie data into the ViewHolder (take data from movie and populate each elements in the ViewHolder
        holder.bind(movie);
    }

    // Return the total count of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    // "ViewHolder" is the repr of one row in the RV.
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
        }

        public void bind(Movie movie) {

            String imageUrl;

            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());

            // use backdrop image in landscape mode
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imageUrl = movie.getBackdropPath();
            } else {
                imageUrl = movie.getPosterPath();
            }

            Glide.with(context).load(imageUrl).placeholder(R.drawable.placeholder_image).into(ivPoster);
        }
    }

}
