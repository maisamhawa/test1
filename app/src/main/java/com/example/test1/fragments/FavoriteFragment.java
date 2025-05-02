package com.example.test1.fragments;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.test1.Adapters.FavoriteAdapter;
import com.example.test1.classes.Movie;
import com.example.test1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {
    private RecyclerView recyclerView;
    private FavoriteAdapter favoriteAdapter;
    private List<Movie> favoriteMovies;
    private Button Back;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_favorites);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        favoriteMovies = new ArrayList<>();
        favoriteAdapter = new FavoriteAdapter(getContext(), favoriteMovies, movie-> {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(userId)
                    .collection("watchlist")
                    .document(movie.getMovieName())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        favoriteMovies.remove(movie);
                        favoriteAdapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "Movie removed from favorites", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Failed to remove movie", Toast.LENGTH_SHORT).show();
                        Log.e("FavoriteFragment", "Error removing: " + e.getMessage());
                    });
        });
        recyclerView.setAdapter(favoriteAdapter);
        Back = view.findViewById(R.id.buttonBack);
        Back.setOnClickListener(v -> {
            gotoallMovieFragment();
        });
        loadFavoriteMovies();
        return view;
    }
    private void loadFavoriteMovies() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }
        String userId = currentUser.getUid();
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .collection("favorites")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    favoriteMovies.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String name = doc.getString("movieName");
                        String photo = doc.getString("photo");
                        favoriteMovies.add(new Movie(name, photo));
                    }
                    favoriteAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e("FavoriteFragment", "Error fetching favorites: " + e.getMessage());
                    Toast.makeText(getContext(), "Failed to load favorites", Toast.LENGTH_SHORT).show();
                });
        }
    private void gotoallMovieFragment() {
        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
        ft.replace(R.id.Framelayoutmain, new allMovieFragment());
        ft.commit();
    }
    }



