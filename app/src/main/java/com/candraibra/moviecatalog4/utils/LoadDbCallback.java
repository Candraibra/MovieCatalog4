package com.candraibra.moviecatalog4.utils;

import com.candraibra.moviecatalog4.model.Movie;
import com.candraibra.moviecatalog4.model.Tv;

import java.util.ArrayList;

public interface LoadDbCallback {
    void preExecute();

    void postExecute(ArrayList<Movie> movies, ArrayList<Tv> tvs);
}
