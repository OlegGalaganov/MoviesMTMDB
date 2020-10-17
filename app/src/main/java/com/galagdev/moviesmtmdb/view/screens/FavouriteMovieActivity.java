package com.galagdev.moviesmtmdb.view.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.galagdev.moviesmtmdb.R;
import com.galagdev.moviesmtmdb.model.pojo.Movie;
import com.galagdev.moviesmtmdb.util.Constant;
import com.galagdev.moviesmtmdb.view.adapters.MovieAdapter;
import com.galagdev.moviesmtmdb.viewmodel.FavouriteMovieViewModel;

import java.util.ArrayList;
import java.util.List;

public class FavouriteMovieActivity extends AppCompatActivity {

    private RecyclerView recyclerViewFavourites;
    private FavouriteMovieViewModel viewModel;
    private MovieAdapter adapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movies_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.main_menu:
                Intent intent = new Intent(this, MoviesActivity.class);
                startActivity(intent);
                break;
            case R.id.favourite_menu:
                Intent intentToFavourite = new Intent(this, FavouriteMovieActivity.class);
                startActivity(intentToFavourite);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_movie);
        assignment();
        loadFavouriteList();
        adapter.setMovieListener(position -> {
            Movie movie = adapter.getMovies().get(position);
            Intent intentToDetailOfMovie = new Intent(FavouriteMovieActivity.this, DetailActivity.class);
            intentToDetailOfMovie.putExtra(Constant.MOVIE_ID, movie.getId());
            intentToDetailOfMovie.putExtra(Constant.ACTIVITY_FROM, Constant.FROM_FAVOURITE_ACTIVITY);
            startActivity(intentToDetailOfMovie);
        });
    }

    private void assignment() {
        adapter = new MovieAdapter();
        viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(FavouriteMovieViewModel.class);
        recyclerViewFavourites = findViewById(R.id.recyclerViewFavouriteMovies);
        recyclerViewFavourites.setLayoutManager(new GridLayoutManager(this, getColumnCount()));
        recyclerViewFavourites.setAdapter(adapter);
    }

    private void loadFavouriteList() {
        viewModel.getFavourites().observe(this, favouriteMovies -> {
            if (favouriteMovies != null && favouriteMovies.size() > 0) {
                List<Movie> movies = new ArrayList<>(favouriteMovies);
                adapter.setMovies(movies);
            }
        });
    }

    private int getColumnCount() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        int width = (int) (displayMetrics.widthPixels / displayMetrics.density);
        return Math.max(width / 185, 2);
    }
}