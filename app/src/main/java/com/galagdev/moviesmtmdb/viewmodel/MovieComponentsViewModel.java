package com.galagdev.moviesmtmdb.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.galagdev.moviesmtmdb.model.api.ApiFactoryMovieComponents;
import com.galagdev.moviesmtmdb.model.api.ApiServiceMovieComponents;
import com.galagdev.moviesmtmdb.model.pojo.Review;
import com.galagdev.moviesmtmdb.model.pojo.Trailer;
import com.galagdev.moviesmtmdb.util.Constant;

import java.util.List;
import java.util.Locale;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MovieComponentsViewModel extends AndroidViewModel {

    private CompositeDisposable compositeDisposable;
    private MutableLiveData<List<Trailer>> trailers;
    private MutableLiveData<List<Review>> reviews;
    private String language;

    public MovieComponentsViewModel(@NonNull Application application) {
        super(application);
        compositeDisposable = new CompositeDisposable();
        trailers = new MutableLiveData<>();
        reviews = new MutableLiveData<>();
        language = Locale.getDefault().getLanguage();
    }

    public LiveData<List<Trailer>> getTrailers() {
        return trailers;
    }

    public LiveData<List<Review>> getReviews() {
        return reviews;
    }


    public void loadReviews(int movieId) {
        ApiFactoryMovieComponents apiFactoryMovieComponents = ApiFactoryMovieComponents.getInstance();
        ApiServiceMovieComponents apiServiceMovieComponents = apiFactoryMovieComponents.apiServiceMovieComponents();
        Disposable disposableLoadReviews = apiServiceMovieComponents.getReviewResponse(movieId, Constant.API_KEY, language)
                .subscribeOn(Schedulers.io())
                .subscribe(reviewResponse -> reviews.postValue(reviewResponse.getReviews()));
        compositeDisposable.add(disposableLoadReviews);
    }


    public void loadTrailers(int movieId) {
        ApiFactoryMovieComponents apiFactoryMovieComponents = ApiFactoryMovieComponents.getInstance();
        ApiServiceMovieComponents apiServiceMovieComponents = apiFactoryMovieComponents.apiServiceMovieComponents();
        Disposable disposableLoadTrailers = apiServiceMovieComponents.getTrailerResponse(movieId, Constant.API_KEY, language)
                .subscribeOn(Schedulers.io())
                .subscribe(trailerResponse -> trailers.postValue(trailerResponse.getTrailers()));
        compositeDisposable.add(disposableLoadTrailers);
    }

    @Override
    protected void onCleared() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
        super.onCleared();
    }
}
