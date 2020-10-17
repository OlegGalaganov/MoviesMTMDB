package com.galagdev.moviesmtmdb.model.api;

import com.galagdev.moviesmtmdb.model.pojo.ReviewResponse;
import com.galagdev.moviesmtmdb.model.pojo.TrailerResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiServiceMovieComponents {
    @GET("{movie_id}/videos")
    Observable<TrailerResponse> getTrailerResponse(@Path("movie_id") int movieId,
                                               @Query("api_key") String apiKey,
                                               @Query("language") String lang);

    @GET("{movie_id}/reviews")
    Observable<ReviewResponse> getReviewResponse(@Path("movie_id") int movieId,
                                                  @Query("api_key") String apiKey,
                                                  @Query("language") String lang);
}
