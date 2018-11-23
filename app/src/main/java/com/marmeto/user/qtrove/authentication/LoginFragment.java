package com.marmeto.user.qtrove.authentication;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.marmeto.user.qtrove.ProductViewFragment;
import com.marmeto.user.qtrove.R;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.Timer;
import java.util.TimerTask;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {
    ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;

    private static final int RC_SIGN_IN = 007;

    GoogleApiClient mGoogleApiClient;

    TextView google_btn,facebook_btn,continue_btn;
    LoginButton login_button;

    Button btn_email,btn_register;

    CallbackManager callbackManager;

    View view;

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        Log.d("TAG", "onConnectionFailed:" + connectionResult);
    }

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplicationContext());

        google_btn = view.findViewById(R.id.google);
        facebook_btn = view.findViewById(R.id.facebook);
        btn_email=view.findViewById(R.id.btn_email);
        btn_register=view.findViewById(R.id.btn_register);
        continue_btn = view.findViewById(R.id.continueAsGuest);

        google_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable() == true) {
                    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                } else {
                    Toast.makeText(getApplicationContext(), "Please Make Sure Internet Is Connected", Toast.LENGTH_SHORT).show();
                }
            }
        });

        login_button = view.findViewById(R.id.login_button);
        login_button.setReadPermissions("email", "public_profile");
        callbackManager = CallbackManager.Factory.create();

        login_button.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>()

                {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        System.out.println("onSuccess");

                        String accessToken = loginResult.getAccessToken()
                                .getToken();

                        Toast.makeText(getApplicationContext(),""+accessToken,Toast.LENGTH_SHORT).show();

                        Log.e("facebook_accessToken", "" + accessToken);

                    }

                    @Override
                    public void onCancel() {
                        System.out.println("onCancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        System.out.println("onError");
                        Log.v("LoginActivity", exception.getCause().toString());
                    }
                });

        facebook_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable() == true) {
                    if (AccessToken.getCurrentAccessToken() != null && com.facebook.Profile.getCurrentProfile() != null) {
                        //Logged in so show the login button

                        LoginManager.getInstance().logOut();

                        login_button.performClick();

                    } else {
                        login_button.performClick();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please Make Sure Internet Is Connected", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new EmailLoginFragment();
                FragmentManager fragmentManager=getFragmentManager();
                FragmentTransaction transactioncal = getFragmentManager().beginTransaction();
                transactioncal.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                transactioncal.replace(R.id.home_container, fragment, "email");
                if(fragmentManager.findFragmentByTag("email")==null)
                {
                    transactioncal.addToBackStack("email");
                    transactioncal.commit();
                }
                else
                {
                    transactioncal.commit();
                }
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new RegisterFragment();
                FragmentManager fragmentManager=getFragmentManager();
                FragmentTransaction transactioncal = getFragmentManager().beginTransaction();
                transactioncal.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                transactioncal.replace(R.id.home_container, fragment, "register");
                if(fragmentManager.findFragmentByTag("register")==null)
                {
                    transactioncal.addToBackStack("register");
                    transactioncal.commit();
                }
                else
                {
                    transactioncal.commit();
                }
            }
        });

        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ProductViewFragment();
                FragmentManager fragmentManager=getFragmentManager();
                FragmentTransaction transactioncal = getFragmentManager().beginTransaction();
                transactioncal.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                transactioncal.replace(R.id.home_container, fragment, "Product");
                if(fragmentManager.findFragmentByTag("Product")==null)
                {
                    transactioncal.addToBackStack("Product");
                    transactioncal.commit();
                }
                else
                {
                    transactioncal.commit();
                }
            }
        });

        init();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();

            String personName = acct.getDisplayName();

            Toast.makeText(getApplicationContext(),""+personName,Toast.LENGTH_SHORT).show();

        } else {
            Log.e("erroer", result.toString());
        }
    }


    public void init () {
        mPager = (ViewPager) view.findViewById(R.id.myimagepager);
//        myPager.setAdapter(adapter);
//        myPager.setCurrentItem(0);
        init1();
    }

    private int imageArra[] = {R.drawable.loginviewpager, R.drawable.loginviewpager};

    private String[] stringArray = new String[]{"Chemical free products good for you and your family", "Chemical free products good for you and your family"};

    private void init1 () {


        ImagePagerAdapter adapter = new ImagePagerAdapter(getActivity(), imageArra, stringArray);
        mPager.setAdapter(adapter);


        CirclePageIndicator indicator = view.findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

        indicator.setRadius(3 * density);


        NUM_PAGES = imageArra.length;


        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 7000, 7000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }

    private boolean isNetworkAvailable () {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
