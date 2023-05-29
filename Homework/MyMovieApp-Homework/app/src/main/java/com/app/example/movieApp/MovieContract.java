package com.app.example.movieApp;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.app.movieApp.movieApp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "movie";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();


        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String TABLE_NAME = "movie";
        public static final String MOVIE_ID = "movie_id";
        public static final String VOTE_AVERAGE = "vote_average";
        public static final String TITLE = "original_title";
        public static final String BACKDROP_PATH = "backdrop_path";
        public static final String OVERVIEW = "overview";
        public static final String RELEASE_DATE = "release_date";
        public static final String POSTER_PATH = "poster_path";


        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static final String[] MOVIE = {
                MOVIE_ID,
                VOTE_AVERAGE,
                TITLE,
                BACKDROP_PATH,
                OVERVIEW,
                RELEASE_DATE,
                POSTER_PATH
        };

        public static final int NR_MOVIE_ID = 0;
        public static final int NR_VOTE_AVERAGE = 1;
        public static final int NR_TITLE = 2;
        public static final int NR_BACKDROP_PATH = 3;
        public static final int NR_OVERVIEW = 4;
        public static final int NR_RELEASE_DATE = 5;
        public static final int NR_POSTER_PATH = 6;

    }
}