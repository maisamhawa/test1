package com.example.test1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddMovieF extends Fragment {

    private static final int RESULT_LOAD_IMAGE = 1;
    private ImageButton image;
    private Uri imageUri;

    private EditText etName, etReleaseDate, etMovieLong, etAgeAllowed, etDescription, etCategory;
    private Button btnAdd, btnback;
    private FirebaseServices fbs;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    public AddMovieF() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_movie, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        connectComponents();
    }

    private void connectComponents() {
        etName = getActivity().findViewById(R.id.etMName);
        etReleaseDate = getActivity().findViewById(R.id.etMReleaseDate);
        etMovieLong = getActivity().findViewById(R.id.etMLong);
        etAgeAllowed = getActivity().findViewById(R.id.etMAgeAllowed);
        etDescription = getActivity().findViewById(R.id.etMDescription);
        etCategory = getActivity().findViewById(R.id.etMCategory);
        fbs = FirebaseServices.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        btnAdd = getActivity().findViewById(R.id.btnAddmovie);
        btnback = getActivity().findViewById(R.id.btnbacktolist);
        image = getActivity().findViewById(R.id.imagebtn);

        btnAdd.setOnClickListener(view -> {
            String MName = etName.getText().toString();
            String MReleaseDate = etReleaseDate.getText().toString();
            String MLong = etMovieLong.getText().toString();
            String MDescription = etDescription.getText().toString();
            String MAgeAllowed = etAgeAllowed.getText().toString();
            String MCategory = etCategory.getText().toString();

            if (MDescription.trim().isEmpty() || MLong.trim().isEmpty() || MCategory.trim().isEmpty() ||
                    MAgeAllowed.trim().isEmpty() || MName.trim().isEmpty() || MReleaseDate.trim().isEmpty()) {
                Toast.makeText(getActivity(), "Something is Empty", Toast.LENGTH_SHORT).show();
                return;
            }

            if (imageUri == null) {
                Toast.makeText(getActivity(), "Please select an image", Toast.LENGTH_SHORT).show();
                return;
            }

            // Upload image to Firebase Storage
            StorageReference imageRef = storageReference.child("movie_images/" + System.currentTimeMillis() + ".jpg");

            imageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString(); // Get the image URL

                        // Save the movie details in Firestore with the image URL
                        Movie movie = new Movie(MName, MReleaseDate, MLong, MAgeAllowed, MDescription, MCategory, imageUrl);
                        fbs.getFire().collection("movies").add(movie)
                                .addOnSuccessListener(documentReference -> {
                                    Toast.makeText(getActivity(), "Movie added!", Toast.LENGTH_SHORT).show();
                                    gotoallMovieFragment();
                                })
                                .addOnFailureListener(e -> Toast.makeText(getActivity(), "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    }))
                    .addOnFailureListener(e -> Toast.makeText(getActivity(), "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });

        btnback.setOnClickListener(view -> gotoallMovieFragment());

        image.setOnClickListener(v -> getImageFromAlbum());
    }

    private void getImageFromAlbum() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE);
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.getData();
            try {
                InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                image.post(() -> {
                    int targetWidth = image.getWidth();
                    int targetHeight = image.getHeight();
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(selectedImage, targetWidth, targetHeight, true);
                    image.setImageBitmap(scaledBitmap);
                });

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getActivity(), "You haven't picked an image", Toast.LENGTH_LONG).show();
        }
    }

    private void gotoallMovieFragment() {
        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
        ft.replace(R.id.Framelayoutmain, new allMovieFragment());
        ft.commit();
    }
}
