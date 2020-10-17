package com.galagdev.moviesmtmdb.model.api;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFactoryMovieComponents {

    private static ApiFactoryMovieComponents apiFactoryMovieComponents;
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";

    public ApiFactoryMovieComponents() {
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
    }

    public static ApiFactoryMovieComponents getInstance() {
        if (apiFactoryMovieComponents == null) {
            apiFactoryMovieComponents = new ApiFactoryMovieComponents();
        }
        return apiFactoryMovieComponents;
    }

    public ApiServiceMovieComponents apiServiceMovieComponents() {
        return retrofit.create(ApiServiceMovieComponents.class);
    }
}
