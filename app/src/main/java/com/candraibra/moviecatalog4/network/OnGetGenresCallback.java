package com.candraibra.moviecatalog4.network;

import com.candraibra.moviecatalog4.model.Genre;

import java.util.List;

public interface OnGetGenresCallback {

    void onSuccess(List<Genre> genres);

    void onError();
}