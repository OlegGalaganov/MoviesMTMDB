package com.galagdev.moviesmtmdb.model.api;

import com.galagdev.moviesmtmdb.model.pojo.MovieResponse;


import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiServiceMovie {
    @GET("movie")
    Single<MovieResponse> getAllMovies(@Query("api_key") String apiKey,
                                       @Query("language") String lang,
                                       @Query("page") int page,
                                       @Query("sort_by") String sortBy,
                                       @Query("vote_count.gte") int voteCountValue);
}
