package com.candraibra.moviecatalog4.network;

import com.candraibra.moviecatalog4.model.Movie;

import java.util.ArrayList;


public interface OnGetMoviesCallback {
    void onSuccess(final ArrayList<Movie> movies);

    void onError();
}
