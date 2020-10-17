package com.galagdev.moviesmtmdb.model.api;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFactoryMovie {

    private static ApiFactoryMovie apiFactoryMovie;
    private Retrofit retrofit;
    private static final String BASE_URL = "https://api.themoviedb.org/3/discover/";

    private ApiFactoryMovie() {
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
    }

    public static ApiFactoryMovie getInstance() {
        if (apiFactoryMovie == null) {
            apiFactoryMovie = new ApiFactoryMovie();
        }
        return apiFactoryMovie;
    }

    public ApiServiceMovie apiServiceMovie() {
        return retrofit.create(ApiServiceMovie.class);
    }
}
