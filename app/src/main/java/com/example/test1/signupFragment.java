package com.example.test1;


import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import android.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link signupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class signupFragment extends Fragment {

    private EditText etUsername , etPassword;
    private Button btnSignup;
    private FirebaseServices fbs;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public signupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment signup.
     */
    // TODO: Rename and change types and number of parameters
    public static signupFragment newInstance(String param1, String param2) {
        signupFragment fragment = new signupFragment();
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
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }
    @Override
    public void onStart() {
        super.onStart();
        //connecting Components
        fbs = FirebaseServices.getInstance();
        etUsername = getView().findViewById(R.id.etUsernameSignup);
        etPassword = getView().findViewById(R.id.etPasswordSignup);
        btnSignup = getView().findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             //Data validation
             String username = etUsername.getText().toString();
             String password = etPassword.getText().toString();

             if (username.trim().isEmpty() || password.trim().isEmpty()) {
                 Toast.makeText(getActivity(), "Email or password is empty! Failed to sign up", Toast.LENGTH_LONG).show();
             } else if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
                 Toast.makeText(getActivity(), "Invalid email format! Failed to sign up", Toast.LENGTH_LONG).show();
             } else {
                 Log.d("TAG", "Before Firebase operation");
                 fbs.getAuth().createUserWithEmailAndPassword(username, password)
                         .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                             @Override
                             public void onComplete(@NonNull Task<AuthResult> task) {
                                 if (task.isSuccessful()) {
                                     if(password.length()<6){ Toast.makeText(getActivity(), "Password must be at least 6 characters! Failed to sign up", Toast.LENGTH_LONG).show();}
                                     Toast.makeText(getActivity(), "User created successfully", Toast.LENGTH_SHORT).show();
                                     gotoAddMovie();
                                   //  gotoAllMovie();TODO
                                 } else {
                                     Toast.makeText(getActivity(), "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                 }
                             }
                         })
                         .addOnFailureListener(new OnFailureListener() {
                             @Override
                             public void onFailure(@NonNull Exception e) {
                                 Toast.makeText(getActivity(), "Failure listener triggered: " + e.getMessage(), Toast.LENGTH_SHORT).show();  // Show failure message
                             }

                         });



             }
         }
     });
    }

    private void gotoAddMovie() {
        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
        ft.replace(R.id.Framelayoutmain, new AddMovieF());
        ft.commit();
    }
    private void gotoAllMovie() {
        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
        ft.replace(R.id.Framelayoutmain, new allMovieFragment());
        ft.commit();
    }
}
                    /*{
                    fbs.getAuth().fetchSignInMethodsForEmail(username)
                            .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                @Override
                                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                    if (task.isSuccessful()) {
                                        // If the email is already registered
                                        List<String> signInMethods = task.getResult().getSignInMethods();

// Print each sign-in method
                                        for (String method : signInMethods) {
                                            Toast.makeText(getActivity(), method, Toast.LENGTH_SHORT).show();
                                        }
                                        if (!signInMethods.isEmpty()) {
                                            Toast.makeText(getActivity(), "This email is already registered! Failed to sign up", Toast.LENGTH_SHORT).show();
                                        } else if (password.length() < 6) {
                                            Toast.makeText(getActivity(), "Password must be at least 6 characters! Failed to sign up", Toast.LENGTH_LONG).show();
                                        } else {
                                            //Signup procedure
                                            fbs.getAuth().createUserWithEmailAndPassword(username, password) // .getException().getMessage()
                                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                                            Toast.makeText(getActivity(), "Successfully signed up", Toast.LENGTH_LONG).show();
                                                            gotoAddMovie();
                                                        }

                                                        private void gotoAddMovie() {
                                                            FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                                                            ft.replace(R.id.Framelayoutmain, new AddMovieF());
                                                            ft.commit();
                                                        }
                                                    });
                                        }
                                    }  else {
                                        Toast.makeText(getActivity(), "Failed to sign up: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
                */

