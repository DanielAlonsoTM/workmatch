package cl.ponceleiva.workmatch.activities.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cl.ponceleiva.workmatch.R;
import cl.ponceleiva.workmatch.utils.UtilitiesKt;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.*;
import com.squareup.picasso.Picasso;

public class AnnounceActivity extends AppCompatActivity {

    private LinearLayout linearLikes;

    private TextView titleBar, place, salary, description, likesCount;
    private ImageButton backButton;
    private ImageView imageView;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announce);

        UtilitiesKt.changeFullColorAppBar(this, getWindow(), getSupportActionBar());

        linearLikes = findViewById(R.id.linearLikes);

        titleBar = findViewById(R.id.tvTitleOthers);
        imageView = findViewById(R.id.profileImage);
        place = findViewById(R.id.announcePlace);
        salary = findViewById(R.id.announceSalary);
        description = findViewById(R.id.announceDescription);
        likesCount = findViewById(R.id.announceLikes);

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
                    public void onSuccess(final DocumentSnapshot documentSnapshot) {
                        titleBar.setText(documentSnapshot.getString("title"));
                        place.setText(documentSnapshot.getString("place"));
                        salary.setText(documentSnapshot.getString("salary"));
                        description.setText(documentSnapshot.getString("description"));
                        int likes = (Integer.valueOf(documentSnapshot.get("likes").toString()) != null) ? Integer.valueOf(documentSnapshot.get("likes").toString()) : 0;
                        likesCount.setText(String.valueOf(likes));
                        Picasso.with(getApplicationContext()).load(documentSnapshot.getString("image")).into(imageView);

                        linearLikes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(AnnounceActivity.this, ListLikesActivity.class);
                                intent.putExtra("announceId", documentSnapshot.getId());
                                startActivity(intent);
                            }
                        });
                    }
                });
    }
}
