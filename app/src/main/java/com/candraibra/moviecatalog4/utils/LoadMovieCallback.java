package com.candraibra.moviecatalog4.utils;

import com.candraibra.moviecatalog4.model.Movie;

import java.util.ArrayList;

public interface LoadMovieCallback {
    void preExecute();

    void postExecute(ArrayList<Movie> movies);
}
