package com.galagdev.moviesmtmdb.view.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.galagdev.moviesmtmdb.R;
import com.galagdev.moviesmtmdb.model.pojo.Movie;
import com.galagdev.moviesmtmdb.model.util.Constant;
import com.galagdev.moviesmtmdb.view.adapters.MovieAdapter;
import com.galagdev.moviesmtmdb.viewmodel.MovieViewModel;

import java.util.Objects;


public class MoviesActivity extends AppCompatActivity {


    private RecyclerView recyclerViewMovies;
    private SwitchCompat switchCompat;
    private TextView textViewTopRated;
    private TextView textViewPopular;
    private ProgressBar progressBarLoading;
    private MovieViewModel viewModel;
    private MovieAdapter adapter;

    private Parcelable savedRecyclerViewPosition;
    private boolean isLoading = false;
    private boolean savedSwitchCompatPosition;


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
        setContentView(R.layout.activity_main);
        assignment();
        switchCompat.setChecked(true);
        switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> setMethodOfSort(isChecked));
        if (savedInstanceState != null) {
            savedRecyclerViewPosition = savedInstanceState.getParcelable(Constant.RECYCLER_VIEW_POSITION);
            savedSwitchCompatPosition = savedInstanceState.getBoolean(Constant.SWITCH_COMPAT_POSITION);
            switchCompat.setChecked(savedSwitchCompatPosition);
        } else {
            switchCompat.setChecked(false);
        }
        adapter.setMovieListener(position -> {
            Movie movie = adapter.getMovies().get(position);
            Intent intentToDetailOfMovie = new Intent(MoviesActivity.this, DetailActivity.class);
            intentToDetailOfMovie.putExtra(Constant.MOVIE_ID, movie.getId());
            intentToDetailOfMovie.putExtra(Constant.ACTIVITY_FROM, Constant.FROM_MOVIE_ACTIVITY);
            startActivity(intentToDetailOfMovie);
        });
        adapter.setOnReachMovie(() -> {
            if (!isLoading) {
                viewModel.continueLoadMovie();
                setLoading(true);
            }
        });
        viewModel.getMovies().observe(this, movies -> {
            if (savedInstanceState != null) {
                Objects.requireNonNull(recyclerViewMovies.getLayoutManager()).onRestoreInstanceState(savedRecyclerViewPosition);
                savedRecyclerViewPosition = null;
            }
            setLoading(false);
            adapter.setMovies(movies);
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constant.RECYCLER_VIEW_POSITION, Objects.requireNonNull(recyclerViewMovies.getLayoutManager()).onSaveInstanceState());
        outState.putBoolean(Constant.SWITCH_COMPAT_POSITION, switchCompat.isChecked());
    }


    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Objects.requireNonNull(recyclerViewMovies.getLayoutManager()).onRestoreInstanceState(savedInstanceState.getParcelable(Constant.RECYCLER_VIEW_POSITION));
        switchCompat.setChecked(savedInstanceState.getBoolean(Constant.SWITCH_COMPAT_POSITION));
    }

    private void assignment() {
        recyclerViewMovies = findViewById(R.id.recyclerViewMovies);
        switchCompat = findViewById(R.id.switchSort);
        textViewTopRated = findViewById(R.id.textViewTopRated);
        textViewPopular = findViewById(R.id.textViewPopularity);
        progressBarLoading = findViewById(R.id.progressBarLoading);
        viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(MovieViewModel.class);
        adapter = new MovieAdapter();
        recyclerViewMovies.setLayoutManager(new GridLayoutManager(this, getColumnCount()));
        recyclerViewMovies.setAdapter(adapter);
        progressBarLoading.setVisibility(View.VISIBLE);
    }

    private int getColumnCount() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        int width = (int) (displayMetrics.widthPixels / displayMetrics.density);
        return Math.max(width / 185, 2);
    }

    private void setMethodOfSort(boolean isTopRated) {
        adapter.clear();
        if (isTopRated) {
            textViewTopRated.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
            textViewPopular.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            viewModel.startLoadTopRatedMovie();
            setLoading(true);
        } else {
            textViewTopRated.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            textViewPopular.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
            viewModel.startLoadPopularMovies();
            setLoading(false);
        }
    }

    public void onClickSwitchToPopular(View view) {
        setMethodOfSort(false);
        switchCompat.setChecked(false);
    }

    public void onClickSwitchToTopRated(View view) {
        setMethodOfSort(true);
        switchCompat.setChecked(true);
    }

    public void setLoading(boolean isLoading) {
        this.isLoading = isLoading;
        if (isLoading && progressBarLoading.getVisibility() == View.INVISIBLE)
            progressBarLoading.setVisibility(View.VISIBLE);
        if (!isLoading && progressBarLoading.getVisibility() == View.VISIBLE)
            progressBarLoading.setVisibility(View.INVISIBLE);
    }
}