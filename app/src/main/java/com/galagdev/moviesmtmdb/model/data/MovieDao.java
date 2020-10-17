package com.galagdev.moviesmtmdb.model.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.galagdev.moviesmtmdb.model.pojo.FavouriteMovie;
import com.galagdev.moviesmtmdb.model.pojo.Movie;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM movies")
    LiveData<List<Movie>> getAllMovies();


    @Query("SELECT * FROM favourite")
    LiveData<List<FavouriteMovie>> getAllFavouriteMovies();

    @Query("SELECT * FROM MOVIES WHERE id == :movieId")
    Movie getMovieById(int movieId);

    @Query("SELECT * FROM favourite WHERE id == :favouriteMovieId")
    FavouriteMovie getFavouriteMovieById(int favouriteMovieId);

    @Insert
    void insertMovies(List<Movie> movies);

    @Insert
    void insertFavouriteMovie(FavouriteMovie movie);

    @Delete
    void deleteFavouriteMovie(FavouriteMovie favouriteMovie);

    @Query("DELETE FROM movies")
    void deleteAllMovies();
}
