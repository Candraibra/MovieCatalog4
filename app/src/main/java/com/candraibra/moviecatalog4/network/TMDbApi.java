package com.candraibra.moviecatalog4.network;

import com.candraibra.moviecatalog4.model.MovieTrailer;
import com.candraibra.moviecatalog4.model.MoviesResponse;
import com.candraibra.moviecatalog4.model.TrailerResponse;
import com.candraibra.moviecatalog4.model.TvResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TMDbApi {

    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("movie/{movie_id}/videos")
    Call<TrailerResponse> getMovieTrailer(@Path("movie_id") int id, @Query("api_key") String apiKey);

    @GET("tv/popular")
    Call<TvResponse> getPopularTv(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );


}