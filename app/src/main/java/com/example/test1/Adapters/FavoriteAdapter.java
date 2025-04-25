package com.example.test1.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.test1.R;
import com.example.test1.classes.Movie;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {
    private List<Movie> favoriteMovies;
    private Context context;
    private OnRemoveClickListener removeClickListener;

    public interface OnRemoveClickListener {
        void onRemoveClick(Movie movie);
    }

    public FavoriteAdapter(Context context, List<Movie> favoriteMovies, OnRemoveClickListener removeClickListener) {
        this.context = context;
        this.favoriteMovies = favoriteMovies;
        this.removeClickListener = removeClickListener;
    }

    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        TextView movieNameTextView;
        ImageView movieImageView;
        Button btnRemove;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            movieNameTextView = itemView.findViewById(R.id.movie_name);
            movieImageView = itemView.findViewById(R.id.movie_image);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.favorite_item, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Movie currentMovie = favoriteMovies.get(position);
        holder.movieNameTextView.setText(currentMovie.getMovieName());

        Glide.with(context)
                .load(currentMovie.getphoto())
                .into(holder.movieImageView);

        holder.btnRemove.setOnClickListener(v -> {
            if (removeClickListener != null) {
                removeClickListener.onRemoveClick(currentMovie);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoriteMovies.size();
    }

    public void updateList(List<Movie> newFavoriteMovies) {
        this.favoriteMovies = newFavoriteMovies;
        notifyDataSetChanged();
    }
}