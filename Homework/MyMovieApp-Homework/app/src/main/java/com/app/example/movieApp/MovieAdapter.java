package com.app.example.movieApp;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    public static final float POSTER_RATIO = 1.5f;

    private final ArrayList<Movie> mMovies;

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void send_details(Movie movie, int position);
    }


    public MovieAdapter(ArrayList<Movie> movies, OnItemClickListener mItemClickListener) {
        mMovies = movies;
        this.mOnItemClickListener = mItemClickListener;
    }


    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context parentContext = parent.getContext();
        int layoutForMovieItem = R.layout.movie_item;
        LayoutInflater inflater = LayoutInflater.from(parentContext);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(R.layout.movie_item, parent, shouldAttachToParentImmediately);
        final Context context = view.getContext();

        int gridColsNumber = context.getResources()
                .getInteger(R.integer.number_of_grid_columns);

        view.getLayoutParams().height = (int) (parent.getWidth() / gridColsNumber *
                POSTER_RATIO);


        MovieViewHolder viewHolder = new MovieViewHolder(view);
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull final MovieViewHolder holder, int position) {
        final Movie movie = mMovies.get(position);
        final Context context = holder.mView.getContext();

        holder.mMovie = movie;
        holder.mMovieTitle.setText(movie.getOriginalTitle());

        String posterUrl = movie.getPosterPath();

        if (posterUrl == null) {
            holder.mMovieTitle.setVisibility(View.VISIBLE);
        }

        Picasso.get()
                .load(movie.getPosterPath())
                .config(Bitmap.Config.RGB_565)
                .placeholder(R.drawable.image_placeholder)
                .into(holder.mMovieThumbnail,
                        new Callback() {
                            @Override
                            public void onSuccess() {
                                if (holder.mMovie.getId() != movie.getId()) {
                                    holder.cleanUp();
                                } else {
                                    holder.mMovieThumbnail.setVisibility(View.VISIBLE);
                                }
                            }
                            @Override
                            public void onError(Exception e) {
                                holder.mMovieTitle.setVisibility(View.VISIBLE);
                            }
                        }
                );

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mOnItemClickListener.send_details(movie,holder.getAdapterPosition());
            }
        });
    }


    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    @Override
    public void onViewRecycled(MovieViewHolder holder) {
        super.onViewRecycled(holder);
        holder.cleanUp();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        public Movie mMovie;

        @BindView(R.id.movie_thumbnail)
        ImageView mMovieThumbnail;

        @BindView(R.id.movie_title)
        TextView mMovieTitle;

        public MovieViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mView = view;

        }

        public void cleanUp() {
            final Context context = mView.getContext();
            Picasso.get().cancelRequest(mMovieThumbnail);
            mMovieThumbnail.setImageBitmap(null);
            mMovieThumbnail.setVisibility(View.INVISIBLE);
            mMovieTitle.setVisibility(View.GONE);
        }

    }
    public void add(List<Movie> movies) {
        mMovies.clear();
        mMovies.addAll(movies);
        notifyDataSetChanged();
    }

    public void add(Cursor cursor) {

        mMovies.clear();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(MovieContract.MovieEntry.NR_MOVIE_ID);
                String v_average = cursor.getString(MovieContract.MovieEntry.NR_VOTE_AVERAGE);
                String title = cursor.getString(MovieContract.MovieEntry.NR_TITLE);
                String backdropPath = cursor.getString(MovieContract.MovieEntry.NR_BACKDROP_PATH);
                String overview = cursor.getString(MovieContract.MovieEntry.NR_OVERVIEW);
                String releaseDate = cursor.getString(MovieContract.MovieEntry.NR_RELEASE_DATE);
                String posterPath = cursor.getString(MovieContract.MovieEntry.NR_POSTER_PATH);
                Movie movie = new Movie(id,v_average,title,backdropPath,overview,releaseDate,posterPath);
                mMovies.add(movie);
            } while (cursor.moveToNext());

        }
        notifyDataSetChanged();
    }

    public ArrayList<Movie> getMovies() {
        return mMovies;
    }


}