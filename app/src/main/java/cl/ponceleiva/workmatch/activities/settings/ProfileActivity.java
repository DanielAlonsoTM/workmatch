package cl.ponceleiva.workmatch.activities.settings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import cl.ponceleiva.workmatch.R;
import cl.ponceleiva.workmatch.utils.UtilitiesKt;

public class ProfileActivity extends AppCompatActivity {

    private TextView titleBar;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        UtilitiesKt.changeFullColorAppBar(this, getWindow(), getSupportActionBar(), getResources());

        titleBar = findViewById(R.id.tvTitleOthers);
        titleBar.setText("Mi Cuenta");

        backButton = findViewById(R.id.actionbar_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
