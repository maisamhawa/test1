package com.example.test1;

//import static android.os.Build.VERSION_CODES.R;

import android.app.FragmentTransaction;
import android.os.Bundle;

import androidx.annotation.NonNull;
import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link forgotPassword#newInstance} factory method to
 * create an instance of this fragment.
 */
public class forgotPassword extends Fragment {

    private EditText etFP;
    private Button btnReset;
    private FirebaseServices fbs;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public forgotPassword() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment forgotPassword.
     */
    // TODO: Rename and change types and number of parameters
    public static forgotPassword newInstance(String param1, String param2) {
        forgotPassword fragment = new forgotPassword();
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
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }
    @Override
    public void onStart() {
        super.onStart();
        connect();
    }

    public void connect() {
        fbs = FirebaseServices.getInstance();
        etFP = getView().findViewById(R.id.etResetforgotPassword);
        btnReset = getView().findViewById(R.id.btnResetforgotPassword);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String eeeemail = etFP.getText().toString();
                fbs.getAuth().sendPasswordResetEmail(eeeemail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!(etFP.getText().toString().isEmpty())) {
                            Toast.makeText(getActivity(), "email was sent", Toast.LENGTH_SHORT).show();
                            gotoLoginFragment();
                        } else {
                            Toast.makeText(getActivity(), "something is wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                    private void gotoLoginFragment() {
                        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                        ft.replace(R.id.Framelayoutmain, new LoginFragment());
                        ft.commit();
                    }
                });
            }
        });
    }
}

