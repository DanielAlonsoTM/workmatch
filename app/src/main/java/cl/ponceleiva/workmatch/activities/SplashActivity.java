package cl.ponceleiva.workmatch.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import cl.ponceleiva.workmatch.R;
import cl.ponceleiva.workmatch.activities.home.MainActivity;
import cl.ponceleiva.workmatch.activities.login.ChooseRegisterActivity;
import cl.ponceleiva.workmatch.activities.login.UserTypeActivity;
import cl.ponceleiva.workmatch.utils.UtilitiesKt;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {
    private int SPLASH_TIME = 2000;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        UtilitiesKt.changeColorInitialsViews(this, getWindow());

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    System.out.println(user.getEmail());
                    SharedPreferences sharedPreferences = getSharedPreferences("pref", MODE_PRIVATE);
                    if (sharedPreferences.getString("typeUser", null) == null || sharedPreferences.getString("typeUser", null).isEmpty()) {
                        initialActivity(UserTypeActivity.class);
                    } else {
                        initialActivity(MainActivity.class);
                    }
                } else {
                    initialActivity(ChooseRegisterActivity.class);
                }
            }
        };

        firebaseAuth.addAuthStateListener(authStateListener);
    }

    private void initialActivity(final Class activityClass) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, activityClass));
            }
        }, SPLASH_TIME);
    }
}