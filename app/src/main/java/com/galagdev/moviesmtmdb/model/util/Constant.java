package com.galagdev.moviesmtmdb.model.util;

public abstract class Constant {

    //Poster
    public static final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/";
    public static final String BIG_IMAGE_SIZE = "w780";
    public static final String SMALL_IMAGE_SIZE = "w185";

    //Trailer
    public static final String BASE_YOUTUBE_URL = "https://www.youtube.com/watch?v=";

    //Movie and FavouriteMovie
    public static final String RECYCLER_VIEW_POSITION = "recycler_view_position";
    public static final String SWITCH_COMPAT_POSITION = "switch_compat_position";
    public static final String ACTIVITY_FROM = "activity";
    public static final String MOVIE_ID = "movieId";
    public static final int FROM_MOVIE_ACTIVITY = 0;
    public static final int FROM_FAVOURITE_ACTIVITY = 1;

    //ViewModels
    public static final String API_KEY = "6c4da8b7cf84656718eb859915d13521";
    public static final String POPULARITY = "popularity.desc";
    public static final String TOP_RATED = "vote_count.desc";
    public static final int MIN_VOTE_COUNT = 1000;
}
