package com.candraibra.moviecatalog4.network;

import com.candraibra.moviecatalog4.model.Tv;

import java.util.ArrayList;

public interface OnGetPageTv {
    void onSuccess(int page, ArrayList<Tv> tvs);

    void onError();
}



