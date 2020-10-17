package com.galagdev.moviesmtmdb.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.galagdev.moviesmtmdb.model.data.MovieDatabase;
import com.galagdev.moviesmtmdb.model.pojo.FavouriteMovie;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class FavouriteMovieViewModel extends AndroidViewModel {

    private CompositeDisposable compositeDisposable;
    private LiveData<List<FavouriteMovie>> favourites;
    private MutableLiveData<FavouriteMovie> favouriteMovieFromId;
    private MutableLiveData<Boolean> isFavourite;
    private MovieDatabase database;

    public FavouriteMovieViewModel(@NonNull Application application) {
        super(application);
        database = MovieDatabase.getInstance(application);
        favourites = database.getMovieDao().getAllFavouriteMovies();
        compositeDisposable = new CompositeDisposable();
        favouriteMovieFromId = new MutableLiveData<>();
        isFavourite = new MutableLiveData<>();
    }

    public LiveData<List<FavouriteMovie>> getFavourites() {
        return favourites;
    }

    public LiveData<FavouriteMovie> getFavouriteMovieFromId() {
        return favouriteMovieFromId;
    }

    public LiveData<Boolean> getIsFavourite() {
        return isFavourite;
    }

    public void getFavouriteMovieById(int favouriteMovieId) {
        Observable<Integer> observable = Observable.just(favouriteMovieId);
        Disposable disposableId = observable
                .subscribeOn(Schedulers.single())
                .flatMap((Function<Integer, Observable<FavouriteMovie>>) integer -> Observable.just(database.getMovieDao().getFavouriteMovieById(integer)))
                .subscribe(favouriteMovie -> {
                            favouriteMovieFromId.postValue(favouriteMovie);
                            isFavourite.postValue(true);
                        }, throwable -> {
                            favouriteMovieFromId.postValue(null);
                            isFavourite.postValue(false);
                        }
                );
        compositeDisposable.add(disposableId);
    }


    public void changeMovieStatus(FavouriteMovie favouriteMovie, int favouriteMovieId) {
        Observable<Integer> observable = Observable.just(favouriteMovieId);
        Disposable disposableChange = observable
                .subscribeOn(Schedulers.single())
                .flatMap((Function<Integer, Observable<FavouriteMovie>>) integer -> Observable.just(database.getMovieDao().getFavouriteMovieById(integer)))
                .subscribe(movie -> {
                    database.getMovieDao().deleteFavouriteMovie(movie);
                    isFavourite.postValue(false);
                }, throwable -> {
                    database.getMovieDao().insertFavouriteMovie(favouriteMovie);
                    isFavourite.postValue(true);
                });
        compositeDisposable.addAll(disposableChange);
    }

    @Override
    protected void onCleared() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
        super.onCleared();
    }
}
