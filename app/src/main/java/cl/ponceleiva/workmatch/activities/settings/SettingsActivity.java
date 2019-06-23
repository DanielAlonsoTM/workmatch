package cl.ponceleiva.workmatch.activities.settings;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import cl.ponceleiva.workmatch.R;
import cl.ponceleiva.workmatch.utils.UtilitiesKt;

public class SettingsActivity extends AppCompatActivity {

    private TextView titleBar;
    private ImageButton backButton;
    private Button profileButton, paymentButton;
    private Intent intentActivity;

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

        paymentButton = findViewById(R.id.payments);
        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSettingActivity(PaymentActivity.class);
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
        intentActivity = new Intent(SettingsActivity.this, classActivity);
        startActivity(intentActivity);
    }
}
