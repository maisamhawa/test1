package com.example.test1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MyViewHolder> {
    Context context;
    ArrayList<String> MovieList;
    ArrayList<String> ImagesList;
    private FirebaseServices fbs;


    public MovieListAdapter(Context context, ArrayList<String> movieList, ArrayList<String> imagesList) {
        this.context = context;
        this.MovieList = movieList;
        this.ImagesList = imagesList;
        this.fbs = FirebaseServices.getInstance();

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView movieName;
        ImageView movieImage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            movieName = (TextView) itemView.findViewById(R.id.movie_name);
            movieImage = (ImageView) itemView.findViewById(R.id.MoviePoster);
        }
    }

    @NonNull
    @Override
    public MovieListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movielist_item,parent,false);
        return new MovieListAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieListAdapter.MyViewHolder holder, int position) {
        holder.movieName.setText((String) MovieList.get(position));
        holder.movieName.setClickable(true);
        holder.movieName.setFocusable(true);
        holder.movieName.setOnClickListener(v -> {
            Toast.makeText(context, "Opening " + (String) MovieList.get(position), Toast.LENGTH_SHORT).show();
            gotoMovieDetails((String) MovieList.get(position));
        });
        String imageUrl = ImagesList.get(position);
        Glide.with(context).load(imageUrl).into(holder.movieImage);
    }

    @Override
    public int getItemCount(){
        return MovieList.size();
    }

    private void gotoMovieDetails(String movieName) {
        Intent intent = new Intent(context, MovieDetailsActivity.class);
        intent.putExtra(MovieDetailsActivity.EXTRA_MOVIE_NAME, movieName); // Pass movie name
        context.startActivity(intent); // Start new activity
    }

}

