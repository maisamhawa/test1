package com.example.test1;

//import static android.os.Build.VERSION_CODES.R;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test1.FirebaseServices;
import com.example.test1.Movie;


import java.util.ArrayList;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MyViewHolder> {
    Context context;
    //ArrayList<Movie> MovieList; //TODO
    ArrayList MovieList;
    private FirebaseServices fbs;


    public MovieListAdapter(Context context, ArrayList movieList) { // ArrayList<Movie> restList) { //TODO
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
       // Movie movie = movieList.get(position); //TODO
        //holder.movieName.setText(movie.getMovieName()); //TODO

        holder.movieName.setText((String) MovieList.get(position));

    }

    @Override
    public int getItemCount(){
        return MovieList.size();
    }
}

