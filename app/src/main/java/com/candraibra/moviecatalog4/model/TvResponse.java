package com.candraibra.moviecatalog4.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TvResponse {

    @SerializedName("results")
    @Expose
    private ArrayList<Tv> tvs = null;

    public ArrayList<Tv> getTvs() {
        return tvs;
    }


}