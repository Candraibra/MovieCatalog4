package com.candraibra.moviecatalog4.utils;

import com.candraibra.moviecatalog4.model.Tv;

import java.util.ArrayList;

public interface LoadTvCallback {
    void preExecute();

    void postExecute2(ArrayList<Tv> tvs);
}
