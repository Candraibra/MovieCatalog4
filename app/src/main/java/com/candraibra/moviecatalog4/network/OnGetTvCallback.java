package com.candraibra.moviecatalog4.network;

import com.candraibra.moviecatalog4.model.Tv;

import java.util.ArrayList;

public interface OnGetTvCallback {
    void onSuccess(final ArrayList<Tv> tvs);

    void onError();
}
