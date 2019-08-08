package com.candraibra.moviecatalog4.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TrailerResponse {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private ArrayList<MovieTrailer> trailers = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ArrayList<MovieTrailer> getTrailers() {
        return trailers;
    }

    public void setResults(ArrayList<MovieTrailer> trailers) {
        this.trailers = trailers;
    }
}
