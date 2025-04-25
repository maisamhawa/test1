package com.example.test1;


import static androidx.test.InstrumentationRegistry.getContext;
import static com.google.android.material.internal.ContextUtils.getActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.FragmentTransaction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.test1.classes.FirebaseServices;
import com.example.test1.classes.Movie;
import com.example.test1.fragments.allMovieFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;


public class MovieDetailsActivity extends AppCompatActivity {
    private Button btnback, favoritebtn, watchlistbtn;
    private FirebaseServices fbs;
    private TextView movieNameText, movielongText, descriptionText, releaseText, categoryText, ageallowedText;
    private ImageView movieImageView;

    public static final String EXTRA_MOVIE_NAME = "extra_movie_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        movieNameText = findViewById(R.id.movieNameD);
        movielongText = findViewById(R.id.longD);
        descriptionText = findViewById(R.id.descriptionD);
        releaseText = findViewById(R.id.releaseD);
        categoryText = findViewById(R.id.categoryD);
        ageallowedText = findViewById(R.id.ageallowedD);
        movieImageView = findViewById(R.id.imageViewD);

        btnback = findViewById(R.id.btnbackD);
        fbs = FirebaseServices.getInstance();
        String movieName = getIntent().getStringExtra(EXTRA_MOVIE_NAME);
        if (movieName != null) {
            loadMovieDetails(movieName);
        }

        btnback.setOnClickListener(v -> {
            gotoAllMovieFragment();
            finish();
        });
    }

    private void loadMovieDetails(String movieName) {
        fbs.getFire().collection("movies").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot dataSnapshot : queryDocumentSnapshots.getDocuments()) {
                Movie movie = dataSnapshot.toObject(Movie.class);
                if (movie != null && movie.getMovieName().equals(movieName)) {
                    movieNameText.setText(movieName);
                    movielongText.setText(movie.getMovieLong());
                    descriptionText.setText(movie.getDescription());
                    releaseText.setText(movie.getReleaseDate());
                    categoryText.setText(movie.getCategory());
                    ageallowedText.setText(movie.getAgeAllowed());

                    String imageUrl = movie.getphoto();
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        Glide.with(MovieDetailsActivity.this)
                                .load(imageUrl)
                                .into(movieImageView);
                    } else {
                        Toast.makeText(MovieDetailsActivity.this, "Image URL is empty or null", Toast.LENGTH_SHORT).show();
                    }
                    connectComponents(MovieDetailsActivity.this, movie);
                }
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(MovieDetailsActivity.this, "No data available", Toast.LENGTH_SHORT).show();
        });
    }

    private void connectComponents(Context context, Movie movie) {
        favoritebtn = findViewById(R.id.favbtn);
        watchlistbtn = findViewById(R.id.watchlistbtn);

        favoritebtn.setOnClickListener(v -> {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                    if (currentUser != null) {
                        String userId = currentUser.getUid();
                        fbs.getFire().collection("users").document(userId)
                                .collection("favorites")
                                .document(movie.getMovieName())
                                .set(movie)
                                .addOnSuccessListener(aVoid ->
                                        Toast.makeText(this, "Movie Added to favorites", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Failed to add to favorites", Toast.LENGTH_SHORT).show();


                                    });
                    }
                });

            watchlistbtn.setOnClickListener(v -> {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null)
                {
                String userId = currentUser.getUid();
                fbs.getFire().collection("users")
                        .document(userId)
                        .collection("watchlist")
                        .document(movie.getMovieName())
                        .set(movie)
                        .addOnSuccessListener(aVoid ->
                                Toast.makeText(this, "Movie Added to Watchlist", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Failed to add to Watchlist", Toast.LENGTH_SHORT).show();
                        });
            }
    });
    }
    private void gotoAllMovieFragment () {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.FramelayoutDetails, new allMovieFragment());
                ft.commit();
            }
        }
