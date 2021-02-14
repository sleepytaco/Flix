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

            vh2.loadBackdrop(movie.getBackdropPath());

        }

    }

    // Return the total count of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    // "ViewHolder" is the repr of one row in the RV.
    public class ViewHolder1 extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;

        public ViewHolder1(@NonNull View itemView) {
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

    public class ViewHolder2 extends RecyclerView.ViewHolder {

        private ImageView ivBackdrop;

        public ViewHolder2(@NonNull View itemView) {

            super(itemView);

            ivBackdrop = itemView.findViewById(R.id.ivBackdrop);

        }

        public void loadBackdrop(String backdropPath) {
            Glide.with(context).load(backdropPath).placeholder(R.drawable.placeholder_image).into(ivBackdrop);
        }
    }
}
