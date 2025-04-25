package com.example.test1.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.app.Fragment;
//import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test1.Adapters.WatchListAdapter;
import com.example.test1.R;
import com.example.test1.classes.Movie;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class WatchListFragment extends Fragment {
    private RecyclerView recyclerView;
    private WatchListAdapter watchListAdapter;
    private List<Movie> watchListMovies;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_watch_list, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_watchlist);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        watchListMovies = new ArrayList<>();
        watchListAdapter = new WatchListAdapter(getContext(), watchListMovies, movie -> {

            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(userId)
                    .collection("watchlist")
                    .document(movie.getMovieName()) // or use movie.getId()
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        watchListMovies.remove(movie);
                        watchListAdapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "Movie removed from Watchlist", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Failed to remove movie", Toast.LENGTH_SHORT).show();
                        Log.e("WatchListFragment", "Error removing: " + e.getMessage());
                    });
        });

        recyclerView.setAdapter(watchListAdapter);
        loadWatchListMovies();
        return view;
    }

    private void loadWatchListMovies() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid();

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .collection("watchlist")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    watchListMovies.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String name = doc.getString("movieName");
                        String photo = doc.getString("photo");
                        watchListMovies.add(new Movie(name, photo));
                    }
                    watchListAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e("WatchListFragment", "Error fetching watchlist: " + e.getMessage());
                    Toast.makeText(getContext(), "Failed to load watchlist", Toast.LENGTH_SHORT).show();
                });
    }
}

