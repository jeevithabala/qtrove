package com.marmeto.user.qtrove;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.marmeto.user.qtrove.authentication.EmailLoginFragment;
import com.marmeto.user.qtrove.authentication.LoginFragment;

public class MainActivity extends AppCompatActivity {

    TextView btn_continue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_continue=findViewById(R.id.continueAsGuest);

        Fragment fragment = new LoginFragment();
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transactioncal = getSupportFragmentManager().beginTransaction();
        transactioncal.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
        transactioncal.replace(R.id.home_container, fragment, "Login");
        if(fragmentManager.findFragmentByTag("Login")==null)
        {
            transactioncal.addToBackStack("Login");
            transactioncal.commit();
        }
        else
        {
            transactioncal.commit();
        }


    }
}
