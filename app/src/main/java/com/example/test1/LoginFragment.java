package com.example.test1;

//import static android.os.Build.VERSION_CODES.R;

import android.os.Bundle;

import androidx.annotation.NonNull;
import android.app.Fragment;
import android.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import java.text.BreakIterator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    private EditText etUsernameL, etPasswordL;
    private Button btnLogin;
    private FirebaseServices fbs;
    private TextView signuplink, forgotPassword;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment login.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        //connecting Components
        fbs = FirebaseServices.getInstance();
        etUsernameL = getView().findViewById(R.id.etUsernameLogin);
        etPasswordL = getView().findViewById(R.id.etPasswordLogin);
        btnLogin = getView().findViewById(R.id.btnLogin);
        signuplink = getView().findViewById(R.id.tvSignupLogin);
        forgotPassword = getView().findViewById(R.id.tvForgotP);

        signuplink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoSignupFragment();
            }

            private void gotoSignupFragment() {
                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                ft.replace(R.id.Framelayoutmain, new signupFragment());
                ft.commit();
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoforgotPassword();
            }

            private void gotoforgotPassword() {
                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                ft.replace(R.id.Framelayoutmain, new forgotPassword());
                ft.commit();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Data validation

                String usernamel = etUsernameL.getText().toString();
                String passwordl = etPasswordL.getText().toString();
                if (usernamel.trim().isEmpty() || passwordl.trim().isEmpty()) {
                    Toast.makeText(getActivity(), "Username or Password is empty ", Toast.LENGTH_LONG).show();
                } else {
                    Task<AuthResult> authResultTask = fbs.getAuth().signInWithEmailAndPassword(usernamel, passwordl)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        Toast.makeText(getActivity(), "Successfully logged in", Toast.LENGTH_LONG).show();
                                        gotoAddMovie();
                                    } else {
                                        String errorMessage = task.getException().getMessage();

                                        if (errorMessage.contains("password is invalid")) {
                                            Toast.makeText(getActivity(), "Incorrect password! Try again.", Toast.LENGTH_LONG).show();
                                        } else if (errorMessage.contains("no user record")) {
                                            Toast.makeText(getActivity(), "Email not registered!", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(getActivity(), "failed to login" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }

                                    }
                                }

                                ;

                                private void gotoAddMovie() {
                                    FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                                    ft.replace(R.id.Framelayoutmain, new AddMovieF());
                                    ft.commit();
                                }
                            });

                }
            }
        });
    }
}


