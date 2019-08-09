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
import com.candraibra.moviecatalog4.model.Tv;
import com.candraibra.moviecatalog4.network.OnGetDetailTv;
import com.candraibra.moviecatalog4.network.OnGetGenresCallback;
import com.candraibra.moviecatalog4.network.TvRepository;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailTvActivity extends AppCompatActivity {

    public static final String EXTRA_TV = "extra_tv";
    private static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w780";
    private static String YOUTUBE_VIDEO_URL = "http://www.youtube.com/watch?v=%s";
    private static String YOUTUBE_THUMBNAIL_URL = "http://img.youtube.com/vi/%s/0.jpg";
    public int tvId;
    private Tv selectedTv;
    private ProgressBar progressBar;
    private ImageView imgBanner, imgPoster;
    private String banner, poster, vote;
    private float rating;
    private TextView tvTitle, tvOverview, tvRealease, tvGenre;
    private RatingBar ratingBar;
    private TvRepository tvRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailtv);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        getTv();
    }


    private void getTv() {
        selectedTv = getIntent().getParcelableExtra(EXTRA_TV);
        tvId = selectedTv.getId();
        tvRepository = TvRepository.getInstance();

        tvRepository.getTv(tvId, new OnGetDetailTv() {
            @Override
            public void onSuccess(Tv tv) {
                getGenres2(tv);
                poster = tv.getPosterPath();
                tvGenre = findViewById(R.id.tv_genre_text);
                imgBanner = findViewById(R.id.img_poster);
                imgPoster = findViewById(R.id.img_poster2);
                tvTitle = findViewById(R.id.tv_title);
                banner = tv.getBackdropPath();
                tvTitle.setText(tv.getName());
                tvOverview = findViewById(R.id.tv_overview_text);
                tvOverview.setText(tv.getOverview());
                ratingBar = findViewById(R.id.rb_userrating);
                vote = Double.toString(tv.getVoteAverage() / 2);
                rating = Float.parseFloat(vote);
                ratingBar.setRating(rating);
                tvRealease = findViewById(R.id.tv_realease_text);
                tvRealease.setText(tv.getFirstAirDate());

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

    private void getGenres2(final Tv tv) {
        tvRepository.getGenres(new OnGetGenresCallback() {
            @Override
            public void onSuccess(List<Genre> genres) {
                if (tv.getGenres() != null) {
                    List<String> currentGenres = new ArrayList<>();
                    for (Genre genre : tv.getGenres()) {
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
        Toast.makeText(DetailTvActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
    }

}

