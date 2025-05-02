package com.example.test1.Adapters;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.test1.MovieDetailsActivity;
import com.example.test1.R;
import com.example.test1.classes.Movie;
import java.util.List;
public class WatchListAdapter extends RecyclerView.Adapter<WatchListAdapter.WatchListViewHolder> {
    private List<Movie> watchListMovies;
    private Context context;
    private OnRemoveClickListener removeClickListener;
    public interface OnRemoveClickListener {
        void onRemoveClick(Movie movie);
    }
    public WatchListAdapter(Context context, List<Movie> watchListMovies, OnRemoveClickListener removeClickListener) {
        this.context = context;
        this.watchListMovies = watchListMovies;
        this.removeClickListener = removeClickListener;
    }
    public static class WatchListViewHolder extends RecyclerView.ViewHolder {
        TextView movieNameTextView;
        ImageButton movieImageView;
        Button btnRemove;
        public WatchListViewHolder(@NonNull View itemView) {
            super(itemView);
            movieNameTextView = itemView.findViewById(R.id.movie_name);
            movieImageView = itemView.findViewById(R.id.movie_image);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }
    @NonNull
    @Override
    public WatchListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.watchlist_item, parent, false);
        return new WatchListViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull WatchListViewHolder holder, int position) {
        Movie currentMovie = watchListMovies.get(position);
        holder.movieNameTextView.setText(currentMovie.getMovieName());
        holder.movieImageView.setClickable(true);
        holder.movieImageView.setFocusable(true);
        holder.movieImageView.setOnClickListener(v -> {
            Toast.makeText(context, "Opening " + currentMovie.getMovieName(), Toast.LENGTH_SHORT).show();
            gotoMovieDetails(currentMovie.getMovieName());
        });
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
        return watchListMovies.size();
    }
    public void updateList(List<Movie> newWatchList) {
        this.watchListMovies = newWatchList;
        notifyDataSetChanged();
    }
    private void gotoMovieDetails(String movieName) {
        Intent intent = new Intent(context, MovieDetailsActivity.class);
        intent.putExtra(MovieDetailsActivity.EXTRA_MOVIE_NAME, movieName);
        context.startActivity(intent);
    }
}
