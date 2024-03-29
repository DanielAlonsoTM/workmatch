package cl.ponceleiva.workmatch.activities.home;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import cl.ponceleiva.workmatch.R;
import cl.ponceleiva.workmatch.activities.chat.MatchListActivity;
import cl.ponceleiva.workmatch.activities.settings.SettingsActivity;
import cl.ponceleiva.workmatch.adapter.AnnouncesAdapter;
import cl.ponceleiva.workmatch.model.Announce;
import cl.ponceleiva.workmatch.utils.UtilitiesKt;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static cl.ponceleiva.workmatch.utils.Constants.ERROR;

public class MainEmployerActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private GridView gridViewAnnounces;

    private ImageButton messagesButton, settingsButtons;

    private ProgressBar progressBar;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private Button publishAnnounceButton;

    private List<Announce> announces = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_employer);

        UtilitiesKt.changeFullColorAppBar(this, getWindow(), getSupportActionBar());

        swipeRefreshLayout = findViewById(R.id.content_announces);
        messagesButton = findViewById(R.id.actionbar_messages);
        settingsButtons = findViewById(R.id.actionbar_settings);
        publishAnnounceButton = findViewById(R.id.btn_publish_announce);
        gridViewAnnounces = findViewById(R.id.grid_announces);
        progressBar = findViewById(R.id.progress_bar_employer);

        final AnnouncesAdapter announcesAdapter = new AnnouncesAdapter(announces, this);
        gridViewAnnounces.setAdapter(announcesAdapter);

        firebaseFirestore
                .collection("Announces")
                .whereEqualTo("userId", firebaseAuth.getUid())
                .orderBy("date")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            UtilitiesKt.logE(ERROR, "Cannot get current announces. Details: " + e);
                            return;
                        }

                        UtilitiesKt.logD("GETELEMENTS", String.valueOf(queryDocumentSnapshots.size()));

                        progressBar.setVisibility(View.GONE);
//                        announces.clear();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            announces.add(
                                    new Announce(
                                            doc.getId(),
                                            doc.getString("title"),
                                            doc.getString("image"),
                                            doc.getDate("date").toLocaleString()
                                    )
                            );
                        }
                        announcesAdapter.notifyDataSetChanged();
                    }
                });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        messagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainEmployerActivity.this, MatchListActivity.class));
            }
        });

        settingsButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainEmployerActivity.this, SettingsActivity.class));
            }
        });

        publishAnnounceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainEmployerActivity.this, PublishAnnounceActivity.class));
            }
        });
    }
}
