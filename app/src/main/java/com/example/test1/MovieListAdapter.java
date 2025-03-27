package com.example.test1;

//import static android.os.Build.VERSION_CODES.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test1.FirebaseServices;
import com.example.test1.Movie;


import java.util.ArrayList;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MyViewHolder> {
    Context context;
    //ArrayList<Movie> MovieList; //TODO
    ArrayList<String> MovieList;
    private FirebaseServices fbs;


    public MovieListAdapter(Context context, ArrayList<String> movieList) { // ArrayList<Movie> restList) { //TODO
        this.context = context;
        this.MovieList = movieList;
        this.fbs = FirebaseServices.getInstance();

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView movieName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            movieName = (TextView) itemView.findViewById(R.id.movie_name);

        }
    }

    @NonNull
    @Override
    public MovieListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movielist_item,parent,false);
        return  new MovieListAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieListAdapter.MyViewHolder holder, int position) {
        //Movie movie = MovieList.get(position); //TODO
        //holder.movieName.setText(movie.getMovieName()); //TODO

        holder.movieName.setText((String) MovieList.get(position));
        holder.movieName.setClickable(true);
        holder.movieName.setFocusable(true);
        holder.movieName.setOnClickListener(v -> {
            // Handle the click event here (for example, navigate to MovieDetailActivity)
            //Intent intent = new Intent(context, MovieDetailActivity.class);
            //intent.putExtra("movie_id", movie.getId());  // Pass movie ID to the new activity
            //context.startActivity(intent);
            //Toast.makeText(context, "No data available", Toast.LENGTH_SHORT).show();
            Toast.makeText(context, "Opening " + (String) MovieList.get(position), Toast.LENGTH_SHORT).show();
            gotoMovieDetails((String) MovieList.get(position));
        });
    }

    @Override
    public int getItemCount(){
        return MovieList.size();
    }

    /*private void gotoMovieDetails(String movieName) {
        if (context instanceof FragmentActivity) {
            FragmentTransaction ft = ((FragmentActivity) context)
                    .getSupportFragmentManager()
                    .beginTransaction();
            ft.replace(R.id.Framelayoutmain, new movieDetails(movieName));
            ft.commit();
        } else {
            Toast.makeText(context, "Error: Unable to get FragmentManager", Toast.LENGTH_SHORT).show();
        }
    }*/

    private void gotoMovieDetails(String movieName) {
        Intent intent = new Intent(context, MovieDetailsActivity.class);
        intent.putExtra(MovieDetailsActivity.EXTRA_MOVIE_NAME, movieName); // Pass movie name
        context.startActivity(intent); // Start new activity
    }

}

