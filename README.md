### Submission 4: Aplikasi Movie Catalogue (Local Storage)

![Demo](demo.gif)
##### Kriteria
1. Terdapat 2 (dua) halaman yang menampilkan daftar film (Movies dan Tv Show).
2. Menggunakan Fragment untuk menampung halaman Movies dan Tv Show.
3. Menggunakan RecyclerView untuk menampilkan daftar film.
4. Menggunakan BottomNavigationView, TabLayout, atau yang lainnya sebagai navigasi antara halaman Movies dan Tv Show.
5. Menampilkan indikator loading ketika data sedang dimuat.
6. Menampilkan poster dan informasi film pada halaman detail film.
7. Menggunakan ConstraintLayout untuk menyusun layout.
8. Menampilkan indikator loading ketika data sedang dimuat.
9. Dapat menyimpan film ke database favorite.
10. Dapat menghapus film dari database favorite.
11. Terdapat halaman untuk menampilkan daftar Favorite Movies.
12. Terdapat halaman untuk menampilkan daftar Favorite Tv Show.
13. Aplikasi harus mendukung bahasa Indonesia dan bahasa Inggris.
14. Aplikasi harus bisa menjaga data yang sudah dimuat ketika terjadi pergantian orientasi dari potrait ke landscape atau sebaliknya.

##### Kali Ini Pada Submission 4 Menggunakan Sql Lite Untuk Menyimpan Data Favorite Atau Biasa Disebut Bookmark
Untuk Implementasinya Sendiri Sebagai Berikut :

`DbContract.Java`
```java
package com.candraibra.moviecatalog4.db;
import android.provider.BaseColumns;
class DbContract {
    static final class FavoriteMovie implements BaseColumns {
        static final String TABLE_NAME = "favorite_movie";
        static final String COLUMN_MOVIEID = "movieid";
        static final String COLUMN_TITLE = "title";
        static final String COLUMN_USERRATING = "userrating";
        static final String COLUMN_BACKDROP_PATH = "backdroppath";
        static final String COLUMN_POSTER_PATH = "posterpath";
        static final String COLUMN_OVERVIEW = "overview";
        static final String COLUMN_VOTER = "voter";
        static final String COLUMN_REALISE = "realise";
    }
    static final class FavoriteTv implements BaseColumns {
        static final String TABLE_NAME = " tv";
        static final String COLUMN_MOVIEID = "tv_id";
        static final String COLUMN_TITLE = "title";
        static final String COLUMN_USERRATING = "userrating";
        static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        static final String COLUMN_POSTER_PATH = "poster_path";
        static final String COLUMN_OVERVIEW = "overview";
        static final String COLUMN_VOTER = "voter";
        static final String COLUMN_FIRST_REALISE = "realise";
    }
}
```
`DbHelper.Java`
```java
package com.candraibra.moviecatalog4.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static String DATABASE_NAME = "dbmoviecatalog";

    private static final int DATABASE_VERSION = 1;
    DbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    private static final String SQL_CREATE_TABLE_MOVIE =
            String.format("CREATE TABLE %s" +
                            "(%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                            " %s INTEGER," +
                            " %s TEXT NOT NULL," +
                            " %s TEXT NOT NULL," +
                            " %s TEXT NOT NULL," +
                            " %s TEXT NOT NULL," +
                            " %s TEXT NOT NULL," +
                            " %s INTEGER," +
                            " %s TEXT NOT NULL)",
                    DbContract.FavoriteMovie.TABLE_NAME,
                    DbContract.FavoriteMovie._ID,
                    DbContract.FavoriteMovie.COLUMN_MOVIEID,
                    DbContract.FavoriteMovie.COLUMN_TITLE,
                    DbContract.FavoriteMovie.COLUMN_REALISE,
                    DbContract.FavoriteMovie.COLUMN_BACKDROP_PATH,
                    DbContract.FavoriteMovie.COLUMN_POSTER_PATH,
                    DbContract.FavoriteMovie.COLUMN_USERRATING,
                    DbContract.FavoriteMovie.COLUMN_VOTER,
                    DbContract.FavoriteMovie.COLUMN_OVERVIEW
            );
    private static final String SQL_CREATE_TABLE_TV =
            String.format("CREATE TABLE %s" +
                            "(%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                            " %s INTEGER," +
                            " %s TEXT NOT NULL," +
                            " %s TEXT NOT NULL," +
                            " %s TEXT NOT NULL," +
                            " %s TEXT NOT NULL," +
                            " %s TEXT NOT NULL," +
                            " %s INTEGER," +
                            " %s TEXT NOT NULL)",
                    DbContract.FavoriteTv.TABLE_NAME,
                    DbContract.FavoriteTv._ID,
                    DbContract.FavoriteTv.COLUMN_MOVIEID,
                    DbContract.FavoriteTv.COLUMN_TITLE,
                    DbContract.FavoriteTv.COLUMN_FIRST_REALISE,
                    DbContract.FavoriteTv.COLUMN_BACKDROP_PATH,
                    DbContract.FavoriteTv.COLUMN_POSTER_PATH,
                    DbContract.FavoriteTv.COLUMN_USERRATING,
                    DbContract.FavoriteTv.COLUMN_VOTER,
                    DbContract.FavoriteTv.COLUMN_OVERVIEW
            );
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_MOVIE);
        db.execSQL(SQL_CREATE_TABLE_TV);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.FavoriteMovie.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.FavoriteTv.TABLE_NAME);
        onCreate(db);
    }
}

```
`MovieHelper.Java`
```java
package com.candraibra.moviecatalog4.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.candraibra.moviecatalog4.model.Movie;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static androidx.constraintlayout.motion.widget.MotionScene.TAG;
import static com.candraibra.moviecatalog4.db.DbContract.FavoriteMovie.COLUMN_BACKDROP_PATH;
import static com.candraibra.moviecatalog4.db.DbContract.FavoriteMovie.COLUMN_MOVIEID;
import static com.candraibra.moviecatalog4.db.DbContract.FavoriteMovie.COLUMN_OVERVIEW;
import static com.candraibra.moviecatalog4.db.DbContract.FavoriteMovie.COLUMN_POSTER_PATH;
import static com.candraibra.moviecatalog4.db.DbContract.FavoriteMovie.COLUMN_REALISE;
import static com.candraibra.moviecatalog4.db.DbContract.FavoriteMovie.COLUMN_TITLE;
import static com.candraibra.moviecatalog4.db.DbContract.FavoriteMovie.COLUMN_USERRATING;
import static com.candraibra.moviecatalog4.db.DbContract.FavoriteMovie.COLUMN_VOTER;
import static com.candraibra.moviecatalog4.db.DbContract.FavoriteMovie.TABLE_NAME;

public class MovieHelper {
    private static final String DATABASE_TABLE = TABLE_NAME;
    private static DbHelper dataBaseHelper;
    private static MovieHelper INSTANCE;
    private static SQLiteDatabase database;

    private MovieHelper(Context context) {
        dataBaseHelper = new DbHelper(context);
    }

    public static MovieHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MovieHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = dataBaseHelper.getWritableDatabase();
    }

    public void close() {
        dataBaseHelper.close();
        if (database.isOpen())
            database.close();
    }

    public ArrayList<Movie> getAllMovie() {
        ArrayList<Movie> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null,
                null,
                null,
                null,
                null,
                _ID + " ASC",
                null);
        cursor.moveToFirst();
        Movie movie;
        if (cursor.getCount() > 0) {
            do {
                movie = new Movie();
                movie.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DbContract.FavoriteMovie.COLUMN_MOVIEID))));
                movie.setTitle(cursor.getString(cursor.getColumnIndex(DbContract.FavoriteMovie.COLUMN_TITLE)));
                movie.setVoteAverage(Double.parseDouble(cursor.getString(cursor.getColumnIndex(DbContract.FavoriteMovie.COLUMN_USERRATING))));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndex(COLUMN_POSTER_PATH)));
                movie.setBackdropPath(cursor.getString(cursor.getColumnIndex(DbContract.FavoriteMovie.COLUMN_BACKDROP_PATH)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(DbContract.FavoriteMovie.COLUMN_OVERVIEW)));
                movie.setVoteCount(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DbContract.FavoriteMovie.COLUMN_VOTER))));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(DbContract.FavoriteMovie.COLUMN_REALISE)));

                arrayList.add(movie);

                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insertMovie(Movie movie) {
        ContentValues args = new ContentValues();
        args.put(COLUMN_MOVIEID, movie.getId());
        args.put(COLUMN_TITLE, movie.getTitle());
        args.put(COLUMN_OVERVIEW, movie.getOverview());
        args.put(COLUMN_BACKDROP_PATH, movie.getBackdropPath());
        args.put(COLUMN_POSTER_PATH, movie.getPosterPath());
        args.put(COLUMN_USERRATING, movie.getVoteAverage());
        args.put(COLUMN_VOTER, movie.getVoteCount());
        args.put(COLUMN_REALISE, movie.getReleaseDate());
        return database.insert(DATABASE_TABLE, null, args);
    }

    public void deleteMovie(int id) {
        database = dataBaseHelper.getWritableDatabase();
        database.delete(DbContract.FavoriteMovie.TABLE_NAME, DbContract.FavoriteMovie.COLUMN_MOVIEID + "=" + id, null);
    }

    public boolean checkMovie(String id) {
        database = dataBaseHelper.getWritableDatabase();
        String selectString = "SELECT * FROM " + DbContract.FavoriteMovie.TABLE_NAME + " WHERE " + DbContract.FavoriteMovie.COLUMN_MOVIEID + " =?";
        Cursor cursor = database.rawQuery(selectString, new String[]{id});
        boolean checkMovie = false;
        if (cursor.moveToFirst()) {
            checkMovie = true;
            int count = 0;
            while (cursor.moveToNext()) {
                count++;
            }
            Log.d(TAG, String.format("%d records found", count));
        }
        cursor.close();
        return checkMovie;
    }
}
```
##### Dan Untuk Contoh Penggunaanya
`FavoriteFragment.java`
```java
package com.candraibra.moviecatalog4.fragment;


import android.content.Intent;
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
import com.candraibra.moviecatalog4.activity.DetailMovieActivity;
import com.candraibra.moviecatalog4.activity.DetailTvActivity;
import com.candraibra.moviecatalog4.adapter.FavAdapter;
import com.candraibra.moviecatalog4.adapter.FavTvAdapter;
import com.candraibra.moviecatalog4.db.MovieHelper;
import com.candraibra.moviecatalog4.db.TvHelper;
import com.candraibra.moviecatalog4.model.Movie;
import com.candraibra.moviecatalog4.model.Tv;
import com.candraibra.moviecatalog4.utils.ItemClickSupport;
import com.candraibra.moviecatalog4.utils.LoadMovieCallback;
import com.candraibra.moviecatalog4.utils.LoadTvCallback;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment implements LoadMovieCallback, LoadTvCallback {
    private RecyclerView rvMovie, rvTv;
    private FavAdapter favAdapter;
    private FavTvAdapter favTvAdapter;
    private final static String LIST_STATE_KEY = "STATE";
    private final static String LIST_STATE_KEY2 = "STATE2";
    private ArrayList<Movie> movieArrayList = new ArrayList<>();
    private ArrayList<Tv> tvArrayList = new ArrayList<>();

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

        MovieHelper movieHelper = MovieHelper.getInstance(getActivity());
        movieHelper.open();

        TvHelper tvHelper = TvHelper.getInstance(getActivity());
        tvHelper.open();

        favAdapter = new FavAdapter(getActivity());
        rvMovie.setAdapter(favAdapter);

        favTvAdapter = new FavTvAdapter(getActivity());
        rvTv.setAdapter(favTvAdapter);

        if (savedInstanceState == null) {
            new LoadMoviesAsync(movieHelper, this).execute();
            new LoadTvAsync(tvHelper, this).execute();
        } else {
            final ArrayList<Movie> moviesState = savedInstanceState.getParcelableArrayList(LIST_STATE_KEY);
            assert moviesState != null;
            movieArrayList.addAll(moviesState);
            favAdapter.setMovieList(moviesState);
            ItemClickSupport.addTo(rvMovie).setOnItemClickListener((recyclerView, position, v) -> {
                Intent intent = new Intent(getActivity(), DetailMovieActivity.class);
                intent.putExtra(DetailMovieActivity.EXTRA_MOVIE, moviesState.get(position));
                startActivity(intent);
            });
            final ArrayList<Tv> tvState = savedInstanceState.getParcelableArrayList(LIST_STATE_KEY2);
            assert tvState != null;
            tvArrayList.addAll(tvState);
            favTvAdapter.setTvList(tvState);
            ItemClickSupport.addTo(rvTv).setOnItemClickListener((recyclerView, position, v) -> {
                Intent intent = new Intent(getActivity(), DetailTvActivity.class);
                intent.putExtra(DetailTvActivity.EXTRA_TV, tvState.get(position));
                startActivity(intent);
            });
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(LIST_STATE_KEY, movieArrayList);
        outState.putParcelableArrayList(LIST_STATE_KEY2, tvArrayList);
    }

    @Override
    public void preExecute() {

    }

    @Override
    public void postExecute2(ArrayList<Tv> tvs) {
        favTvAdapter.setTvList(tvs);
        rvTv.setAdapter(favTvAdapter);
        tvArrayList.addAll(tvs);
        ItemClickSupport.addTo(rvTv).setOnItemClickListener((recyclerView, position, v) -> {
            Intent intent = new Intent(getActivity(), DetailTvActivity.class);
            intent.putExtra(DetailTvActivity.EXTRA_TV, tvs.get(position));
            startActivity(intent);
        });
    }

    @Override
    public void postExecute(ArrayList<Movie> movies) {
        favAdapter.setMovieList(movies);
        rvMovie.setAdapter(favAdapter);
        movieArrayList.addAll(movies);
        ItemClickSupport.addTo(rvMovie).setOnItemClickListener((recyclerView, position, v) -> {
            Intent intent = new Intent(getActivity(), DetailMovieActivity.class);
            intent.putExtra(DetailMovieActivity.EXTRA_MOVIE, movies.get(position));
            startActivity(intent);
        });
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

```

>  ##### Btw Kali Ini Saya Agak Lama Menyelesaikan Ini Karena Fokus Memperbaiki Ui Dan Mood Yang Kadang Tiba Tiba Menghilang Kaya Dia -_ 
