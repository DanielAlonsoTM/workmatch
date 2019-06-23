package cl.ponceleiva.workmatch.activities.settings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import cl.ponceleiva.workmatch.R;
import cl.ponceleiva.workmatch.utils.UtilitiesKt;

public class PaymentActivity extends AppCompatActivity {

    private TextView titleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        UtilitiesKt.changeFullColorAppBar(this, getWindow(), getSupportActionBar());

        titleBar = findViewById(R.id.tvTitleOthers);
        titleBar.setText("Añadir Método de Pago");
    }
}
