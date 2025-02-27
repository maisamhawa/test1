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

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {
    Context context;
    ArrayList<Movie> MovieList;
    private FirebaseServices fbs;

    public MovieAdapter(Context context, ArrayList<Movie> restList) {
        this.context = context;
        this.MovieList = restList;
        this.fbs = FirebaseServices.getInstance();
    }

    @NonNull
    @Override
    public MovieAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(context).inflate(R.layout.movie_item,parent,false);
        return  new MovieAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.MyViewHolder holder, int position) {
        Movie movie = MovieList.get(position);
        holder.tvName.setText(movie.getMovieName());
        holder.tvDescription.setText(movie.getDescription());
    }

    @Override
    public int getItemCount(){
        return MovieList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvName, tvDescription;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tvName);
            tvDescription=itemView.findViewById(R.id.tvmovieDescription);

        }
    }
}

