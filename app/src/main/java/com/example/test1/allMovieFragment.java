package com.example.test1;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link allMovieFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class allMovieFragment extends Fragment {
    private Button newmovie;
    private FirebaseServices fbs;
    //private ArrayList<Movie>  movies; //TODO
    private ArrayList  movies;
    private RecyclerView recyclerView;
    private MovieListAdapter adapter;
    //private MovieAdapter adapter; TODO
    private RecyclerView.LayoutManager layoutManager;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public allMovieFragment() {
        // Required empty public constructor
    }
    public static allMovieFragment newInstance(String param1, String param2) {
        allMovieFragment fragment = new allMovieFragment();
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

        return inflater.inflate(R.layout.fragment_all_movie, container, false);

    }

    @Override
    public void onStart() {
        super.onStart();
        newmovie=getView().findViewById(R.id.btnaddNmovie);

        fbs=FirebaseServices.getInstance();
        recyclerView = getView().findViewById(R.id.rvMoviesmovieFragment);

        recyclerView.setHasFixedSize(true);
       // movies = new ArrayList<>(Arrays.asList("AA","BB","CC")); // TODO
        movies = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MovieListAdapter(getActivity(), movies);
        //adapter = new MovieAdapter(getActivity(), movies); TODO
        recyclerView.setAdapter(adapter);
        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider_layout));
        recyclerView.addItemDecoration(divider);
        fbs.getFire().collection("movies").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot dataSnapshot : queryDocumentSnapshots.getDocuments()) {
                    Movie movie = dataSnapshot.toObject(Movie.class);
                    movies.add(movie.getMovieName());
                }
                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
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

            private void gotoAddMovie() {
                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                ft.replace(R.id.Framelayoutmain, new AddMovieF());
                ft.commit();
            }
        });
    }
}
