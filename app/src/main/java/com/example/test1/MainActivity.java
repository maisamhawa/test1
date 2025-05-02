package com.example.test1;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.app.FragmentTransaction;
import com.example.test1.fragments.LoginFragment;
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        gotoLoginFragment();
    }
        private void gotoLoginFragment ()
        {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.Framelayoutmain, new LoginFragment());
            ft.commit();
        }
    }
