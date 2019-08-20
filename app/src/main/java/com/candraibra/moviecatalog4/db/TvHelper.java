package com.candraibra.moviecatalog4.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.candraibra.moviecatalog4.model.Tv;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.candraibra.moviecatalog4.db.DbContract.FavoriteTv.COLUMN_BACKDROP_PATH;
import static com.candraibra.moviecatalog4.db.DbContract.FavoriteTv.COLUMN_FIRST_REALISE;
import static com.candraibra.moviecatalog4.db.DbContract.FavoriteTv.COLUMN_MOVIEID;
import static com.candraibra.moviecatalog4.db.DbContract.FavoriteTv.COLUMN_OVERVIEW;
import static com.candraibra.moviecatalog4.db.DbContract.FavoriteTv.COLUMN_POSTER_PATH;
import static com.candraibra.moviecatalog4.db.DbContract.FavoriteTv.COLUMN_TITLE;
import static com.candraibra.moviecatalog4.db.DbContract.FavoriteTv.COLUMN_USERRATING;
import static com.candraibra.moviecatalog4.db.DbContract.FavoriteTv.COLUMN_VOTER;
import static com.candraibra.moviecatalog4.db.DbContract.FavoriteTv.TABLE_NAME;

public class TvHelper {
    private static final String DATABASE_TABLE = TABLE_NAME;
    private static DbHelper dataBaseHelper;
    private static TvHelper INSTANCE;
    private static SQLiteDatabase database;

    public TvHelper(Context context) {
        dataBaseHelper = new DbHelper(context);
    }

    public static TvHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TvHelper(context);
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

    public ArrayList<Tv> getAllTv() {
        ArrayList<Tv> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null,
                null,
                null,
                null,
                null,
                _ID + " ASC",
                null);
        cursor.moveToFirst();
        Tv tv;
        if (cursor.getCount() > 0) {
            do {
                tv = new Tv();
                tv.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DbContract.FavoriteTv.COLUMN_MOVIEID))));
                tv.setName(cursor.getString(cursor.getColumnIndex(DbContract.FavoriteTv.COLUMN_TITLE)));
                tv.setVoteAverage(Double.parseDouble(cursor.getString(cursor.getColumnIndex(DbContract.FavoriteTv.COLUMN_USERRATING))));
                tv.setPosterPath(cursor.getString(cursor.getColumnIndex(COLUMN_POSTER_PATH)));
                tv.setBackdropPath(cursor.getString(cursor.getColumnIndex(DbContract.FavoriteTv.COLUMN_BACKDROP_PATH)));
                tv.setOverview(cursor.getString(cursor.getColumnIndex(DbContract.FavoriteTv.COLUMN_OVERVIEW)));
                tv.setVoteCount(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DbContract.FavoriteTv.COLUMN_VOTER))));
                tv.setFirstAirDate(cursor.getString(cursor.getColumnIndex(DbContract.FavoriteTv.COLUMN_FIRST_REALISE)));

                arrayList.add(tv);

                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insertTv(Tv tv) {
        ContentValues args = new ContentValues();
        args.put(COLUMN_MOVIEID, tv.getId());
        args.put(COLUMN_TITLE, tv.getName());
        args.put(COLUMN_OVERVIEW, tv.getOverview());
        args.put(COLUMN_BACKDROP_PATH, tv.getBackdropPath());
        args.put(COLUMN_POSTER_PATH, tv.getPosterPath());
        args.put(COLUMN_USERRATING, tv.getVoteAverage());
        args.put(COLUMN_VOTER, tv.getVoteCount());
        args.put(COLUMN_FIRST_REALISE, tv.getFirstAirDate());
        return database.insert(DATABASE_TABLE, null, args);
    }

    public void deleteTv(int id) {
        database = dataBaseHelper.getWritableDatabase();
        database.delete(DbContract.FavoriteTv.TABLE_NAME, DbContract.FavoriteTv.COLUMN_MOVIEID + "=" + id, null);
    }
}
