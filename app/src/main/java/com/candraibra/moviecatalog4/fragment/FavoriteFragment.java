package com.candraibra.moviecatalog4.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.candraibra.moviecatalog4.R;
import com.candraibra.moviecatalog4.adapter.FavAdapter;
import com.candraibra.moviecatalog4.adapter.FavTvAdapter;
import com.candraibra.moviecatalog4.db.MovieHelper;
import com.candraibra.moviecatalog4.db.TvHelper;
import com.candraibra.moviecatalog4.model.Movie;
import com.candraibra.moviecatalog4.model.Tv;
import com.candraibra.moviecatalog4.utils.LoadMovieCallback;
import com.candraibra.moviecatalog4.utils.LoadTvCallback;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment implements LoadMovieCallback, LoadTvCallback {
    RecyclerView rvMovie, rvTv;
    FavAdapter favAdapter;
    MovieHelper movieHelper;
    FavTvAdapter favTvAdapter;
    TvHelper tvHelper;
    private static final String EXTRA_STATE = "EXTRA_STATE";

    public FavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvMovie = view.findViewById(R.id.rv_liked_movie);
        rvMovie.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rvMovie.setHasFixedSize(true);

        rvTv = view.findViewById(R.id.rv_liked_tv);
        rvTv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rvTv.setHasFixedSize(true);

        movieHelper = MovieHelper.getInstance(getActivity());
        movieHelper.open();

        tvHelper = TvHelper.getInstance(getActivity());
        tvHelper.open();

        favAdapter = new FavAdapter(getActivity());
        rvMovie.setAdapter(favAdapter);

        favTvAdapter = new FavTvAdapter(getActivity());
        rvTv.setAdapter(favTvAdapter);

        if (savedInstanceState == null) {
            new LoadMoviesAsync(movieHelper, this).execute();
            new LoadTvAsync(tvHelper, this).execute();
        } else {
            ArrayList<Movie> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null) {
                favAdapter.setMovieList(list);
            }
        }
    }

    @Override
    public void preExecute() {

    }

    @Override
    public void postExecute2(ArrayList<Tv> tvs) {
        favTvAdapter.setTvList(tvs);
    }

    @Override
    public void postExecute(ArrayList<Movie> movies) {
        favAdapter.setMovieList(movies);
    }

    private static class LoadMoviesAsync extends AsyncTask<Void, Void, ArrayList<Movie>> {
        private final WeakReference<MovieHelper> weakMovieHelper;
        private final WeakReference<LoadMovieCallback> weakCallback;

        private LoadMoviesAsync(MovieHelper movieHelper, LoadMovieCallback callback) {
            weakMovieHelper = new WeakReference<>(movieHelper);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<Movie> doInBackground(Void... voids) {
            return weakMovieHelper.get().getAllMovie();
        }


        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);
            weakCallback.get().postExecute(movies);
        }
    }

    private static class LoadTvAsync extends AsyncTask<Void, Void, ArrayList<Tv>> {
        private final WeakReference<TvHelper> weakTvHelper;
        private final WeakReference<LoadTvCallback> weakCallback;

        private LoadTvAsync(TvHelper tvHelper, LoadTvCallback callback) {
            weakTvHelper = new WeakReference<>(tvHelper);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<Tv> doInBackground(Void... voids) {
            return weakTvHelper.get().getAllTv();
        }


        @Override
        protected void onPostExecute(ArrayList<Tv> tvs) {
            super.onPostExecute(tvs);
            weakCallback.get().postExecute2(tvs);
        }
    }

}
