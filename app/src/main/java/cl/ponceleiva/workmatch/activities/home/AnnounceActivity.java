package cl.ponceleiva.workmatch.activities.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import cl.ponceleiva.workmatch.R;
import cl.ponceleiva.workmatch.utils.UtilitiesKt;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.*;

public class AnnounceActivity extends AppCompatActivity {

    private TextView titleBar;
    private ImageButton backButton;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announce);

        UtilitiesKt.changeFullColorAppBar(this, getWindow(), getSupportActionBar());

        titleBar = findViewById(R.id.tvTitleOthers);
        backButton = findViewById(R.id.actionbar_back);

        titleBar.setText("...");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        String announceId = intent.getStringExtra("announceId");

        firebaseFirestore
                .collection("Announces")
                .document(announceId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        titleBar.setText(documentSnapshot.getString("title"));

                    }
                });

    }
}
