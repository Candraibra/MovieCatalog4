package com.candraibra.moviecatalog4.network;

import com.candraibra.moviecatalog4.model.Movie;

import java.util.ArrayList;

public interface OnGetPageMovie {
    void onSuccess(int page, ArrayList<Movie> movies);

    void onError();
}



