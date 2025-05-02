package com.example.test1.fragments;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.test1.R;
import com.example.test1.classes.FirebaseServices;
import com.example.test1.classes.Movie;
import com.example.test1.Adapters.MovieListAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

public class allMovieFragment extends Fragment {
    private Button newmovie, btnFavorites, btnWatchlist;
    private FirebaseServices fbs;
    private ArrayList<String> movies;
    private ArrayList<String> images;
    private RecyclerView recyclerView;
    private MovieListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_movie, container, false);
    }
    @Override
    public void onStart() {
        super.onStart();
        newmovie = getView().findViewById(R.id.btnaddNmovie);
        btnFavorites = getView().findViewById(R.id.btnFavorites);
        btnWatchlist = getView().findViewById(R.id.btnWatchlist);
        fbs = FirebaseServices.getInstance();
        movies = new ArrayList<>();
        images = new ArrayList<>();
        recyclerView = getView().findViewById(R.id.rvMoviesmovieFragment);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MovieListAdapter(getActivity(), movies, images);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider_layout));
        recyclerView.addItemDecoration(divider);
        fbs.getFire().collection("movies").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                            Movie movie = doc.toObject(Movie.class);
                            movies.add(movie.getMovieName());
                            images.add(movie.getphoto());
                        }
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "No data available", Toast.LENGTH_SHORT).show();
                        Log.e("AllMoviesFragment", e.getMessage());
                    }
                });
        newmovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoAddMovie();
            }
        });
        btnFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoFavoriteFragment();
            }
        });
        btnWatchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoWatchListFragment();
            }
        });
    }
    private void gotoAddMovie() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.Framelayoutmain, new AddMovieF());
        ft.addToBackStack(null);
        ft.commit();
    }
    private void gotoFavoriteFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.Framelayoutmain, new FavoriteFragment());
        ft.addToBackStack(null);
        ft.commit();
    }
    private void gotoWatchListFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.Framelayoutmain, new WatchListFragment());
        ft.addToBackStack(null);
        ft.commit();
    }
}
