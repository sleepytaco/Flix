package com.abukh.flix.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Parcel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abukh.flix.DetailActivity;
import com.abukh.flix.R;
import com.abukh.flix.models.Movie;
import com.bumptech.glide.Glide;

import org.parceler.Parcels;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int POPULAR = 1, UNPOPULAR = 0;
    Context context; // context to where the rv is actually being created
    List<Movie> movies; // our model

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    //Returns the view type of the item at position for the purposes of view recycling.
    @Override
    public int getItemViewType(int position) {

        if (movies.get(position).getRating() > 7) {
            return POPULAR;
        }

        return UNPOPULAR;

    }

    // Inflates a layout from XML (in our case item_movie.xml) and returns it inside the ViewHolder
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View movieView;
        RecyclerView.ViewHolder viewHolder;

        if (viewType == POPULAR) {
            movieView = LayoutInflater.from(context).inflate(R.layout.item_movie_popular, parent, false);
            viewHolder = new ViewHolder2(movieView);
        } else {
            movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
            viewHolder = new ViewHolder1(movieView);
        }

        return viewHolder; // wrap the appropriate movieView inside our ViewHolder and return it
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Movie movie = movies.get(position);

        if (holder.getItemViewType() == UNPOPULAR) {

            ViewHolder1 vh1 = (ViewHolder1) holder;

            // bind the movie data into the ViewHolder (take data from movie and populate each elements in the ViewHolder)
            vh1.bind(movie);
        } else {

            ViewHolder2 vh2 = (ViewHolder2) holder;

            vh2.loadBackdrop(movie);

        }

    }

    // Return the total count of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    // "ViewHolder" is literal the representation of one row of movie in the RV
    public class ViewHolder1 extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;
        RelativeLayout container;

        public ViewHolder1(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            container = itemView.findViewById(R.id.container);

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

            // 1. Register an onclick listener on the whole row, so when a user clicks on a movie row they are taken to another activity
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toast.makeText(context, movie.getTitle(), Toast.LENGTH_LONG).show();

                    // 2. Navigate to a new activity on tap
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("movie", Parcels.wrap(movie));
                    context.startActivity(intent);
                }
            });
        }
    }

    // viewholder for popular movies
    public class ViewHolder2 extends RecyclerView.ViewHolder {

        private ImageView ivBackdrop;

        public ViewHolder2(@NonNull View itemView) {

            super(itemView);

            ivBackdrop = itemView.findViewById(R.id.ivBackdrop);

        }

        public void loadBackdrop(Movie movie) {
            Glide.with(context).load(movie.getBackdropPath()).placeholder(R.drawable.placeholder_image).into(ivBackdrop);

            // 1. Register an onclick listener on the whole row, so when a user clicks on a movie row they are taken to another activity
            ivBackdrop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toast.makeText(context, movie.getTitle(), Toast.LENGTH_LONG).show();

                    // 2. Navigate to a new activity on tap
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("movie", Parcels.wrap(movie));
                    context.startActivity(intent);
                }
            });
        }
    }
}
