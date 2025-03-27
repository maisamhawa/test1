package com.example.test1;

//import static android.os.Build.VERSION_CODES.R;
import static android.app.Activity.RESULT_OK;
import static androidx.core.app.PendingIntentCompat.getActivity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import android.app.Fragment;


import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.StorageReference;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddMovieF#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddMovieF extends Fragment {

    private static final int GALLERY_REQUEST_CODE = 123;
    private static final int RESULT_LOAD_IMAGE = 1;
  //jadeed
    private ImageButton image;
    private Uri imageUri;


    private EditText etName, etReleaseDate, etMovieLong, etAgeAllowed, etDescription, etCategory;
    private Button btnAdd ,btnback;
    private FirebaseServices fbs;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //jadeed
    private Intent data;

    public AddMovieF() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment addMovieF.
     */
    // TODO: Rename and change types and number of parameters
    public static AddMovieF newInstance(String param1, String param2) {
        AddMovieF fragment = new AddMovieF();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
        btnAdd = getActivity().findViewById(R.id.btnAddmovie);
        btnback=getActivity().findViewById(R.id.btnbacktolist);
       //jadeed
        image = getActivity().findViewById(R.id.imagebtn);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String MName, MReleaseDate, MLong, MAgeAllowed, MDescription, MCategory;
                MName = etName.getText().toString();
                MReleaseDate = etReleaseDate.getText().toString();
                MLong = etMovieLong.getText().toString();
                MDescription = etDescription.getText().toString();
                MAgeAllowed = etAgeAllowed.getText().toString();
                MCategory = etCategory.getText().toString();
                //image=image.get

                    if (MDescription.trim().isEmpty() || MLong.trim().isEmpty() || MCategory.trim().isEmpty() ||
                            MAgeAllowed.trim().isEmpty() || MName.trim().isEmpty() || MReleaseDate.trim().isEmpty()){
                        Toast.makeText(getActivity(), "Something is Empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (imageUri == null) {
                        Toast.makeText(getActivity(), "Please select an image", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // Upload image first
                    uploadImageToFirebase(imageUri, new OnImageUploadListener() {
                        @Override
                        public void onUploadSuccess(String imageUrl) {
                            // Save movie with image URL
                            Movie movie = new Movie(MName, MReleaseDate, MLong, MAgeAllowed, MDescription, MCategory, imageUrl);
                            fbs.getFire().collection("movies").add(movie)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getActivity(), "Movie added!", Toast.LENGTH_SHORT).show();
                                            gotoallMovieFragment();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getActivity(), "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }

                        @Override
                        public void onUploadFailure(Exception e) {
                            Toast.makeText(getActivity(), "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            }
        });

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoallMovieFragment();
            }
        });

        //jadeed
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getImageFromAlbum();
            }
        });
    }

    private void uploadImageToFirebase(Uri imageUri, OnImageUploadListener listener) {
        Log.d("ImageUpload", "Starting upload...");
        StorageReference storageRef = fbs.getInstance().getReference("movie_images");
        String fileName = System.currentTimeMillis() + ".jpg";
        StorageReference imageRef = storageRef.child(fileName);

        Log.d("ImageUpload", "File name: " + fileName);

        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    Log.d("ImageUpload", "Upload successful!");
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        Log.d("ImageUpload", "Download URL: " + imageUrl);
                        listener.onUploadSuccess(imageUrl);
                    });
                })
                .addOnFailureListener(e -> {
                    Log.e("ImageUpload", "Upload failed: " + e.getMessage());
                    listener.onUploadFailure(e);
                });
    }

    // Callback interface for image upload success/failure
    interface OnImageUploadListener {
        void onUploadSuccess(String imageUrl);
        void onUploadFailure(Exception e);
    }

    //jadeed
    private void getImageFromAlbum() {
        try {
            //Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE);
        } catch (Exception exp) {
            Log.i("Error", exp.toString());
        }
    }
    //jadedd
        @Override
        public void onActivityResult(int reqCode, int resultCode, Intent data) {
            super.onActivityResult(reqCode, resultCode, data);
            if (resultCode == RESULT_OK) {
                try {
                    imageUri = data.getData();
                    final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    // Resize the image to fit the ImageButton
                    image.post(new Runnable() {
                        @Override
                        public void run() {
                            int targetWidth = image.getWidth();
                            int targetHeight = image.getHeight();
                            Bitmap scaledBitmap = Bitmap.createScaledBitmap(selectedImage, targetWidth, targetHeight, true);
                            image.setImageBitmap(scaledBitmap);
                        }
                    });
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getActivity(), "You haven't picked Image",Toast.LENGTH_LONG).show();
            }
    }

    private void gotoallMovieFragment() {
            FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
            ft.replace(R.id.Framelayoutmain, new allMovieFragment());
            ft.commit();
        }
    }

