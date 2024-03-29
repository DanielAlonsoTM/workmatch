package cl.ponceleiva.workmatch.activities.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import cl.ponceleiva.workmatch.R;
import cl.ponceleiva.workmatch.activities.SplashActivity;
import cl.ponceleiva.workmatch.utils.UtilitiesKt;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {

    private TextView titleBar;
    private ImageButton backButton;
    private Button profileButton, paymentButton, closeSessionButton, announcesButton;
    private Intent intentActivity;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        UtilitiesKt.changeFullColorAppBar(this, getWindow(), getSupportActionBar());

        titleBar = findViewById(R.id.tvTitleOthers);
        titleBar.setText("Configuración");

        profileButton = findViewById(R.id.edit_profile);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSettingActivity(ProfileActivity.class);
            }
        });

        announcesButton = findViewById(R.id.announces);
        announcesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        paymentButton = findViewById(R.id.payments);
        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSettingActivity(PaymentActivity.class);
            }
        });

        closeSessionButton = findViewById(R.id.close_session);
        closeSessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("pref", MODE_PRIVATE);
                preferences.edit().remove("typeUser").commit();
                preferences.edit().clear().commit();
                firebaseAuth.signOut();
                startActivity(new Intent(getApplicationContext(), SplashActivity.class));
                finish();
            }
        });

        backButton = findViewById(R.id.actionbar_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void startSettingActivity(Class classActivity) {
        startActivity(new Intent(SettingsActivity.this, classActivity));
    }
}
