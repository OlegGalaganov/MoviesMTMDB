package com.galagdev.moviesmtmdb.model.pojo;

import androidx.room.Entity;
import androidx.room.Ignore;
@Entity(tableName = "favourite")
public class FavouriteMovie extends Movie {
    public FavouriteMovie(int uniqueId,
                          double popularity,
                          int voteCount,
                          boolean video,
                          String posterPath,
                          int id,
                          boolean adult,
                          String backdropPath,
                          String originalLanguage,
                          String originalTitle,
                          String title,
                          double voteAverage,
                          String overview,
                          String releaseDate) {
        super(uniqueId, popularity, voteCount, video, posterPath, id, adult, backdropPath,
                originalLanguage, originalTitle, title, voteAverage, overview, releaseDate);
    }

    @Ignore
    public FavouriteMovie(Movie movie) {
        super(movie.getUniqueId(),
                movie.getPopularity(),
                movie.getVoteCount(),
                movie.isVideo(),
                movie.getPosterPath(),
                movie.getId(),
                movie.isAdult(),
                movie.getBackdropPath(),
                movie.getOriginalLanguage(),
                movie.getOriginalTitle(),
                movie.getTitle(),
                movie.getVoteAverage(),
                movie.getOverview(),
                movie.getReleaseDate());
    }
}
