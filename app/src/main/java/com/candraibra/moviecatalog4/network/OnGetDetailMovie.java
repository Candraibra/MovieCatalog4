package com.candraibra.moviecatalog4.network;

import com.candraibra.moviecatalog4.model.Movie;

public interface OnGetDetailMovie {
    void onSuccess(Movie movie);

    void onError();
}
