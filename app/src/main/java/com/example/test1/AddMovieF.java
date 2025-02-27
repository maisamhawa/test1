package com.example.test1;

//import static android.os.Build.VERSION_CODES.R;
import static android.app.Activity.RESULT_OK;
import static androidx.core.app.PendingIntentCompat.getActivity;

import android.app.FragmentTransaction;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import java.time.Instant;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddMovieF#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddMovieF extends Fragment {


    private EditText etName, etReleaseDate, etMovieLong, etAgeAllowed, etDescription, etCategory;
    private Button btnAdd;
    private FirebaseServices fbs;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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

                if (MDescription.trim().isEmpty() || MLong.trim().isEmpty() || MCategory.trim().isEmpty() ||
                        MAgeAllowed.trim().isEmpty() || MName.trim().isEmpty() || MReleaseDate.trim().isEmpty()) {
                    Toast.makeText(getActivity(), "Something is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                Movie movie = new Movie(MName, MReleaseDate, MLong, MAgeAllowed, MDescription, MCategory);
                fbs.getFire().collection("movies").add(movie).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getActivity(), "Movie added: " + documentReference.getId(), Toast.LENGTH_SHORT).show();
                        gotoallMovieFragment();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Failed to add movie: " + e.getMessage(), Toast.LENGTH_SHORT).show();  // Added Toast
                    }
                });
            }
        });
    }
        private void gotoallMovieFragment() {
            FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
            ft.replace(R.id.Framelayoutmain, new allMovieFragment());
            ft.commit();
        }

    }

