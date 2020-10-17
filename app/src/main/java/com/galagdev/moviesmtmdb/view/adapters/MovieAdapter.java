package com.galagdev.moviesmtmdb.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.galagdev.moviesmtmdb.R;
import com.galagdev.moviesmtmdb.model.pojo.Movie;
import com.galagdev.moviesmtmdb.util.Constant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movies;
    private static OnClickMovieListener onMovieListener;
    private static OnReachMovieListener onReachMovie;

    public interface OnClickMovieListener {
        void onClickMovie(int position);
    }

    public interface OnReachMovieListener {
        void onReachMovie();
    }

    public void setMovieListener(OnClickMovieListener onMovieListener) {
        MovieAdapter.onMovieListener = onMovieListener;
    }

    public void setOnReachMovie(OnReachMovieListener onReachMovie) {
        MovieAdapter.onReachMovie = onReachMovie;
    }

    public MovieAdapter() {
        movies = new ArrayList<>();
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void addMovies(ArrayList<Movie> list) {
        movies.addAll(list);
        notifyDataSetChanged();
    }

    public void clear() {
        this.movies.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        Picasso.get().load(Constant.BASE_IMAGE_URL + Constant.SMALL_IMAGE_SIZE + movie.getPosterPath()).into(holder.imageViewMoviePoster);
        if (movies.size() >= 20 && position == movies.size() - 6 && onReachMovie != null) {
            onReachMovie.onReachMovie();
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewMoviePoster;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewMoviePoster = itemView.findViewById(R.id.imageViewMovie);

            itemView.setOnClickListener(v -> {
                if (onMovieListener != null) {
                    onMovieListener.onClickMovie(getAdapterPosition());
                }
            });
        }
    }
}
