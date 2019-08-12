package com.candraibra.moviecatalog4.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.candraibra.moviecatalog4.R;
import com.candraibra.moviecatalog4.activity.DetailMovieActivity;
import com.candraibra.moviecatalog4.adapter.MoviePageAdapter;
import com.candraibra.moviecatalog4.model.Movie;
import com.candraibra.moviecatalog4.network.MoviesRepository;
import com.candraibra.moviecatalog4.network.OnGetPageMovie;
import com.candraibra.moviecatalog4.utils.ItemClickSupport;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment {

    private final static String LIST_STATE_KEY = "STATE";
    private ArrayList<Movie> movieArrayList = new ArrayList<>();
    private MoviesRepository moviesRepository;
    private RecyclerView recyclerView;
    private boolean isFetchingMovies;
    private int currentPage = 1;
    private MoviePageAdapter adapter;
    private final GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);

    public MovieFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        moviesRepository = MoviesRepository.getInstance();
        recyclerView = view.findViewById(R.id.rv_discover_movie);
        getMovies(currentPage);
        setupOnScrollListener();
        if (savedInstanceState != null) {
            //  progressBar.setVisibility(View.INVISIBLE);
            final ArrayList<Movie> moviesState = savedInstanceState.getParcelableArrayList(LIST_STATE_KEY);
            assert moviesState != null;
            movieArrayList.addAll(moviesState);
            adapter = new MoviePageAdapter(getActivity());
            adapter.setMovieList(moviesState);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(manager);
            ItemClickSupport.addTo(recyclerView).setOnItemClickListener((recyclerView, position, v) -> {
                Intent intent = new Intent(getActivity(), DetailMovieActivity.class);
                intent.putExtra(DetailMovieActivity.EXTRA_MOVIE, moviesState.get(position));
                startActivity(intent);
            });
        } else {
            getMovies(currentPage);
            setupOnScrollListener();
        }
    }

    private void setupOnScrollListener() {

        recyclerView.setLayoutManager(manager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int totalItemCount = manager.getItemCount();
                int visibleItemCount = manager.getChildCount();
                int firstVisibleItem = manager.findFirstVisibleItemPosition();

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    if (!isFetchingMovies) {
                        getMovies(currentPage + 1);
                    }
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(LIST_STATE_KEY, movieArrayList);
    }

    private void getMovies(int page) {
        isFetchingMovies = true;
        moviesRepository.getMoviesPage(page, new OnGetPageMovie() {
            @Override
            public void onSuccess(int page, ArrayList<Movie> movies) {
                if (adapter == null) {
                    adapter = new MoviePageAdapter(getContext());
                    adapter.setMovieList(movies);
                    movieArrayList.addAll(movies);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(manager);
                    ItemClickSupport.addTo(recyclerView).setOnItemClickListener((recyclerView, position, v) -> {
                        Intent intent = new Intent(getActivity(), DetailMovieActivity.class);
                        intent.putExtra(DetailMovieActivity.EXTRA_MOVIE, movies.get(position));
                        startActivity(intent);
                    });
                } else {
                    adapter.appendMovies(movies);
                }
                currentPage = page;
                isFetchingMovies = false;
            }

            @Override
            public void onError() {

            }

        });
    }
}
