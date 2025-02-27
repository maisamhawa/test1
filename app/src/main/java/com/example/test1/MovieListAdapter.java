package com.example.test1;

//import static android.os.Build.VERSION_CODES.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test1.FirebaseServices;
import com.example.test1.Movie;


import java.util.ArrayList;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MyViewHolder> {
    Context context;
    //ArrayList<Movie> MovieList;
    ArrayList<Movie> MovieList;
    private FirebaseServices fbs;

    public MovieListAdapter(Context context, ArrayList<Movie> restList) {
        this.context = context;
        this.MovieList = restList;
        this.fbs = FirebaseServices.getInstance();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView movieName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            movieName = itemView.findViewById(R.id.tvName);
        }
    }

    @NonNull
    @Override
    public MovieListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_all_movie,parent,false);
        return  new MovieListAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieListAdapter.MyViewHolder holder, int position) {
        Movie movie = MovieList.get(position);
        holder.movieName.setText(movie.getMovieName());
    }

    @Override
    public int getItemCount(){
        return MovieList.size();
    }
}

