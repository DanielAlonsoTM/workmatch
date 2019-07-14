package cl.ponceleiva.workmatch.activities.home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import cl.ponceleiva.workmatch.R;
import cl.ponceleiva.workmatch.utils.UtilitiesKt;

import static cl.ponceleiva.workmatch.utils.Constants.*;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PublishAnnounceActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    private TextView titleView;
    private EditText editTextJobTitle, editTextContactPhone, editTextPlace, editTextSalary, editTextJobDescription;
    private ImageButton backButton;
    private Button publishAnnounceButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_announce);

        UtilitiesKt.changeFullColorAppBar(this, getWindow(), getSupportActionBar());

        titleView = findViewById(R.id.tvTitleOthers);
        titleView.setText("Publicar Anuncio");

        backButton = findViewById(R.id.actionbar_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editTextJobTitle = findViewById(R.id.publishAnnounceTitle);
        editTextContactPhone = findViewById(R.id.publishAnnouncePhone);
        editTextSalary = findViewById(R.id.publishAnnounceSalary);
        editTextPlace = findViewById(R.id.publishAnnouncePlace);
        editTextJobDescription = findViewById(R.id.publishAnnounceDescription);

        publishAnnounceButton = findViewById(R.id.publishAnnounceButton);

        publishAnnounceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextJobTitle.getText().toString().isEmpty() ||
                        editTextContactPhone.getText().toString().isEmpty() ||
                        editTextSalary.getText().toString().isEmpty() ||
                        editTextPlace.getText().toString().isEmpty() ||
                        editTextJobDescription.getText().toString().isEmpty()) {
                    UtilitiesKt.toastMessage(getApplicationContext(), "Todos los campos deben estar completos");
                } else {
                    try {
                        Date date = new Date();
                        Timestamp timestamp = new Timestamp(date);

                        Map<String, Object> data = new HashMap<>();
                        data.put("userId", firebaseAuth.getUid());
                        data.put("title", editTextJobTitle.getText().toString());
                        data.put("phone", editTextContactPhone.getText().toString());
                        data.put("salary", editTextSalary.getText().toString());
                        data.put("place", editTextPlace.getText().toString());
                        data.put("description", editTextJobDescription.getText().toString());
                        data.put("date", timestamp);

                        firebaseFirestore.collection("Announces").add(data);
                        UtilitiesKt.toastMessage(getApplicationContext(), "Se ha publicado su anuncio.");
                    } catch (Exception e) {
                        UtilitiesKt.logE(ERROR, "Details: " + e);
                        UtilitiesKt.toastMessage(getApplicationContext(), "No ha sido posible publicar su anuncio.");
                    }
                }
            }
        });
    }
}