package com.candraibra.moviecatalog4.network;

import androidx.annotation.NonNull;

import com.candraibra.moviecatalog4.BuildConfig;
import com.candraibra.moviecatalog4.model.TvResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TvRepository {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String LANGUAGE = "en-US";
    private static TvRepository repository;

    private TMDbApi api;

    private TvRepository(TMDbApi api) {
        this.api = api;
    }

    public static TvRepository getInstance() {
        if (repository == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            repository = new TvRepository(retrofit.create(TMDbApi.class));
        }

        return repository;
    }

    public void getTvPopular(final OnGetTvCallback callback) {
        String apiKey = BuildConfig.ApiKey;
        api.getPopularTv(apiKey, LANGUAGE, 1)
                .enqueue(new Callback<TvResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<TvResponse> call, @NonNull Response<TvResponse> response) {
                        if (response.isSuccessful()) {
                            TvResponse TvResponse = response.body();
                            if (TvResponse != null && TvResponse.getTvs() != null) {
                                callback.onSuccess(TvResponse.getTvs());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<TvResponse> call, @NonNull Throwable t) {
                        callback.onError();
                    }
                });
    }

}
