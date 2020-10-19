package com.galagdev.moviesmtmdb.view.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.galagdev.moviesmtmdb.R;
import com.galagdev.moviesmtmdb.model.pojo.FavouriteMovie;
import com.galagdev.moviesmtmdb.model.pojo.Movie;
import com.galagdev.moviesmtmdb.model.pojo.Review;
import com.galagdev.moviesmtmdb.model.pojo.Trailer;
import com.galagdev.moviesmtmdb.model.util.Constant;
import com.galagdev.moviesmtmdb.view.adapters.ReviewsAdapter;
import com.galagdev.moviesmtmdb.view.adapters.TrailersAdapter;
import com.galagdev.moviesmtmdb.viewmodel.FavouriteMovieViewModel;
import com.galagdev.moviesmtmdb.viewmodel.MovieComponentsViewModel;
import com.galagdev.moviesmtmdb.viewmodel.MovieViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


// РАЗОБРАТЬСЯ С ДОБАВЛЕНИЕМ В ИЗБРАННОЕ

public class DetailActivity extends AppCompatActivity {

    private ImageView imageViewBigPoster;
    private ImageView imageViewAddToFavourite;
    private TextView textViewTitle;
    private TextView textViewOriginalTitle;
    private TextView textViewReleaseDate;
    private TextView textViewOverview;
    private RecyclerView recyclerViewTrailers;
    private RecyclerView recyclerViewReviews;

    private FavouriteMovieViewModel favouriteMovieViewModel;
    private MovieComponentsViewModel movieComponentsViewModel;
    private MovieViewModel movieViewModel;
    private ReviewsAdapter reviewsAdapter;
    private TrailersAdapter trailersAdapter;
    private Movie movie;
    private int idMovie;

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
        setContentView(R.layout.activity_detail);
        assignment();
        getMovieFromIntent();
        favouriteMovieViewModel.getFavouriteMovieById(idMovie);
        setFavourite();
        trailersAdapter.setOnTrailerClickListener(url -> {
            Intent intentToTrailer = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intentToTrailer);
        });
    }

    private void assignment() {
        favouriteMovieViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(FavouriteMovieViewModel.class);
        movieViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(MovieViewModel.class);
        movieComponentsViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(MovieComponentsViewModel.class);
        recyclerViewTrailers = findViewById(R.id.recyclerViewTrailers);
        recyclerViewReviews = findViewById(R.id.recyclerViewReviews);
        imageViewBigPoster = findViewById(R.id.imageViewBigPoster);
        imageViewAddToFavourite = findViewById(R.id.imageViewAddToFavourite);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewOriginalTitle = findViewById(R.id.textViewOriginalTitle);
        textViewReleaseDate = findViewById(R.id.textViewReleaseDate);
        textViewOverview = findViewById(R.id.textViewOverview);
        reviewsAdapter = new ReviewsAdapter();
        trailersAdapter = new TrailersAdapter();
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTrailers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewReviews.setAdapter(reviewsAdapter);
        recyclerViewTrailers.setAdapter(trailersAdapter);
        trailersAdapter.setTrailers(new ArrayList<>());
        reviewsAdapter.setReviews(new ArrayList<>());
    }

    private void getMovieFromIntent() {
        final Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Constant.MOVIE_ID) && intent.hasExtra(Constant.ACTIVITY_FROM)) {
            idMovie = intent.getIntExtra(Constant.MOVIE_ID, -1);
            int activityFrom = intent.getIntExtra(Constant.ACTIVITY_FROM, -1);
            if (activityFrom == Constant.FROM_MOVIE_ACTIVITY) {
                movieViewModel.getMovieById(idMovie);
                movieViewModel.getMutableMovie().observe(this, movieFromViewModel -> {
                    if (movieFromViewModel != null) {
                        movie = movieFromViewModel;
                        createMovieDetails(movie);
                    }
                });
            } else if (activityFrom == Constant.FROM_FAVOURITE_ACTIVITY) {
                favouriteMovieViewModel.getFavouriteMovieById(idMovie);
                favouriteMovieViewModel.getFavouriteMovieFromId().observe(this, favouriteMovieFromViewModel -> {
                    if (favouriteMovieFromViewModel != null) {
                        movie = favouriteMovieFromViewModel;
                        createMovieDetails(movie);
                    }
                });
            }
        } else
            finish();
    }

    private void setFavourite() {
        favouriteMovieViewModel.getIsFavourite().observe(this, isFavourite -> {
            if (!isFavourite) {
                imageViewAddToFavourite.setImageResource(R.drawable.favourite_add_to);
            }
            else {
                imageViewAddToFavourite.setImageResource(R.drawable.favourite_remove);
            }
        });
    }

    private void createMovieDetails(Movie movie) {
        textViewTitle.setText(movie.getTitle());
        textViewOriginalTitle.setText(movie.getOriginalTitle());
        textViewReleaseDate.setText(movie.getReleaseDate());
        textViewOverview.setText(movie.getOverview());
        Picasso.get().load(Constant.BASE_IMAGE_URL + Constant.BIG_IMAGE_SIZE + movie.getPosterPath()).into(imageViewBigPoster);
        movieComponentsViewModel.loadTrailers(idMovie);
        movieComponentsViewModel.getTrailers().observe(this, trailers -> trailersAdapter.setTrailers((ArrayList<Trailer>) trailers));
        movieComponentsViewModel.loadReviews(idMovie);
        movieComponentsViewModel.getReviews().observe(this, reviews -> reviewsAdapter.setReviews((ArrayList<Review>) reviews));
    }

    public void onClickAddMovieToFavourite(View view) {
        favouriteMovieViewModel.changeMovieStatus(new FavouriteMovie(movie), idMovie);
    }
}