package com.candraibra.moviecatalog4.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.candraibra.moviecatalog4.R;
import com.candraibra.moviecatalog4.model.Genre;
import com.candraibra.moviecatalog4.model.Movie;
import com.candraibra.moviecatalog4.network.MoviesRepository;
import com.candraibra.moviecatalog4.network.OnGetDetailMovie;
import com.candraibra.moviecatalog4.network.OnGetGenresCallback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailMovieActivity extends AppCompatActivity {
    public static final String EXTRA_MOVIE = "extra_movie";

    private static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w780";
    private static String YOUTUBE_VIDEO_URL = "http://www.youtube.com/watch?v=%s";
    private static String YOUTUBE_THUMBNAIL_URL = "http://img.youtube.com/vi/%s/0.jpg";
    public int movieId;
    private Movie selectedMovie;
    private ProgressBar progressBar;
    private ImageView imgBanner, imgPoster;
    private String banner, poster, vote;
    private float rating;
    private TextView tvTitle, tvOverview, tvRealease, tvGenre;
    private RatingBar ratingBar;
    private MoviesRepository moviesRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        getMovie();

    }

    private void getMovie() {
        selectedMovie = getIntent().getParcelableExtra(EXTRA_MOVIE);
        movieId = selectedMovie.getId();
        moviesRepository = MoviesRepository.getInstance();

        moviesRepository.getMovie(movieId, new OnGetDetailMovie() {
            @Override
            public void onSuccess(Movie movie) {
                getGenres(movie);
                poster = movie.getPosterPath();
                tvGenre = findViewById(R.id.tv_genre_text);
                imgBanner = findViewById(R.id.img_poster);
                imgPoster = findViewById(R.id.img_poster2);
                tvTitle = findViewById(R.id.tv_title);
                banner = movie.getBackdropPath();
                tvTitle.setText(movie.getTitle());
                tvOverview = findViewById(R.id.tv_overview_text);
                tvOverview.setText(movie.getOverview());
                ratingBar = findViewById(R.id.rb_userrating);
                vote = Double.toString(movie.getVoteAverage() / 2);
                rating = Float.parseFloat(vote);
                ratingBar.setRating(rating);
                tvRealease = findViewById(R.id.tv_realease_text);
                tvRealease.setText(movie.getReleaseDate());

                if (!isFinishing()) {
                    Picasso.get().load(poster).placeholder(R.drawable.load).into(imgPoster);
                    Picasso.get().load(banner).placeholder(R.drawable.load).into(imgBanner);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }

    private void getGenres(final Movie movie) {
        moviesRepository.getGenres(new OnGetGenresCallback() {
            @Override
            public void onSuccess(List<Genre> genres) {
                if (movie.getGenres() != null) {
                    List<String> currentGenres = new ArrayList<>();
                    for (Genre genre : movie.getGenres()) {
                        currentGenres.add(genre.getName());
                    }
                    tvGenre.setText(TextUtils.join(", ", currentGenres));
                }
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }


    private void showError() {
        Toast.makeText(DetailMovieActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
    }

}

