package com.galagdev.moviesmtmdb.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.galagdev.moviesmtmdb.model.api.ApiFactoryMovie;
import com.galagdev.moviesmtmdb.model.api.ApiServiceMovie;
import com.galagdev.moviesmtmdb.model.data.MovieDatabase;
import com.galagdev.moviesmtmdb.model.pojo.Movie;
import com.galagdev.moviesmtmdb.util.Constant;

import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MovieViewModel extends AndroidViewModel {

    private CompositeDisposable compositeDisposable;
    private static MovieDatabase database;
    private LiveData<List<Movie>> movies;
    private MutableLiveData<Movie> mutableMovie;

    private String language;
    private String methodOfSort;
    private int page;

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    public LiveData<Movie> getMutableMovie() {
        return mutableMovie;
    }

    public void startLoadTopRatedMovie() {
        page = 1;
        methodOfSort = Constant.TOP_RATED;
        loadMovies();
    }

    public void startLoadPopularMovies() {
        page = 1;
        methodOfSort = Constant.POPULARITY;
        loadMovies();
    }

    public MovieViewModel(@NonNull Application application) {
        super(application);
        database = MovieDatabase.getInstance(application);
        movies = database.getMovieDao().getAllMovies();
        compositeDisposable = new CompositeDisposable();
        language = Locale.getDefault().getLanguage();
        mutableMovie = new MutableLiveData<>();
    }


    public void getMovieById(int movieId) {
        Observable<Integer> observable = Observable.just(movieId);
        Disposable disposableId = observable
                .subscribeOn(Schedulers.io())
                .flatMap((Function<Integer, Observable<Movie>>) integer -> Observable.just(database.getMovieDao().getMovieById(integer)))
                .subscribe(movieFromBase -> mutableMovie.postValue(movieFromBase));
        compositeDisposable.add(disposableId);
    }

    private void updateMovies(final List<Movie> allMovies) {
        Observable<List<Movie>> observable = Observable.just(allMovies);
        Disposable disposableInsert = observable
                .subscribeOn(Schedulers.io())
                .subscribe(moviesForUpdate -> {
                    if (page == 1) {
                        database.getMovieDao().deleteAllMovies();
                    }
                    database.getMovieDao().insertMovies(moviesForUpdate);
                    page++;
                });
        compositeDisposable.add(disposableInsert);
    }

    public void continueLoadMovie() {
        loadMovies();
    }

    private void loadMovies() {
        ApiFactoryMovie apiFactoryMovie = ApiFactoryMovie.getInstance();
        ApiServiceMovie apiServiceMovie = apiFactoryMovie.apiServiceMovie();
        Disposable disposable = apiServiceMovie.getAllMovies(Constant.API_KEY, language, page, methodOfSort, Constant.MIN_VOTE_COUNT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(moviesResponse -> updateMovies(moviesResponse.getMovies()));
        compositeDisposable.add(disposable);
    }


    @Override
    protected void onCleared() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
        super.onCleared();
    }
}
