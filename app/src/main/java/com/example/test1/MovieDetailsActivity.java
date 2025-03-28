package com.example.test1;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class MovieDetailsActivity extends AppCompatActivity {
    private Button btnback;
    private FirebaseServices fbs;

    // Connect UI components
    private void connectComponents() {
        btnback = findViewById(R.id.btnbackD);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoallMovieFragment();
                finish();
            }
        });
    }

    public static final String EXTRA_MOVIE_NAME = "extra_movie_name"; // Key for Intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fbs = FirebaseServices.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details); // Create this layout
        connectComponents();

        // Get movie name from Intent
        String movieName = getIntent().getStringExtra(EXTRA_MOVIE_NAME);

        // Set movie name in TextView
        TextView movieNameText = findViewById(R.id.movieNameD);
        TextView movielongText = findViewById(R.id.longD);
        TextView descriptionText = findViewById(R.id.descriptionD);
        TextView releaseText = findViewById(R.id.releaseD);
        TextView categoryText = findViewById(R.id.categoryD);
        TextView ageallowedText = findViewById(R.id.ageallowedD);
        ImageView movieImageView = findViewById(R.id.imageViewD);

        if (movieName != null) {
            movieNameText.setText(movieName);
            fbs.getFire().collection("movies").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (DocumentSnapshot dataSnapshot : queryDocumentSnapshots.getDocuments()) {
                        Movie movie = dataSnapshot.toObject(Movie.class);
                        if (movie.getMovieName().equals(movieName)) {
                            movieNameText.setText(movieName);
                            movielongText.setText(movie.getMovieLong());
                            descriptionText.setText(movie.getDescription());
                            releaseText.setText(movie.getReleaseDate());
                            categoryText.setText(movie.getCategory());
                            ageallowedText.setText(movie.getAgeAllowed());

                            // Load the image from Firebase Storage using Glide
                            String imageUrl = movie.getphoto();
                            if (imageUrl != null && !imageUrl.isEmpty()) {
                                Glide.with(MovieDetailsActivity.this)
                                        .load(imageUrl) // Pass the image URL here
                                        .into(movieImageView); // Load the image into the ImageView
                            } else {
                                Toast.makeText(MovieDetailsActivity.this, "Image URL is empty or null", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MovieDetailsActivity.this, "No data available", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // Go back to the list of movies
    private void gotoallMovieFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.FramelayoutDetails, new allMovieFragment());
        ft.commit();
    }
}