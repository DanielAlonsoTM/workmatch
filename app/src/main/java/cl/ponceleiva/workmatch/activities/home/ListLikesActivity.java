package cl.ponceleiva.workmatch.activities.home;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import cl.ponceleiva.workmatch.R;
import cl.ponceleiva.workmatch.adapter.LikeAdapter;
import cl.ponceleiva.workmatch.model.Like;
import cl.ponceleiva.workmatch.utils.UtilitiesKt;

import static cl.ponceleiva.workmatch.utils.Constants.*;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListLikesActivity extends AppCompatActivity implements LikeAdapter.OnLikeListener {

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    private ProgressBar progressBar;

    private TextView titleBar;
    private ImageButton backButton;

    private RecyclerView recyclerView;
    private ArrayList<Like> likeList = new ArrayList<>();
    LikeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_likes);

        UtilitiesKt.changeFullColorAppBar(this, getWindow(), getSupportActionBar());

        titleBar = findViewById(R.id.tvTitleOthers);
        backButton = findViewById(R.id.actionbar_back);
        progressBar = findViewById(R.id.progress_bar_lkes);
        recyclerView = findViewById(R.id.recyclerViewLikes);

        titleBar.setText("Likes");
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        final String announceId = intent.getStringExtra("announceId");
        adapter = new LikeAdapter(likeList, this, this);

        firebaseFirestore
                .collection("Announces")
                .document(announceId)
                .collection("likes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(final @NonNull Task<QuerySnapshot> task) {
                        if (!task.isSuccessful()) {
                            UtilitiesKt.logE(ERROR, "Detail: " + task.getException());
                            return;
                        } else {
                            firebaseFirestore
                                    .collection("Users")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> taskUsers) {
                                            List<DocumentSnapshot> announceLikes = task.getResult().getDocuments();
                                            List<DocumentSnapshot> users = taskUsers.getResult().getDocuments();

                                            for (DocumentSnapshot docUsers : users) {
                                                for (DocumentSnapshot docAnnounces : announceLikes) {
                                                    if (docAnnounces.getString("userId").equals(docUsers.getId())) {
                                                        System.out.println(docUsers.getString("name"));
                                                        likeList.add(
                                                                new Like(
                                                                        docUsers.getString("name"),
                                                                        docUsers.getString("profileImageUrl"),
                                                                        docUsers.getId(),
                                                                        announceId));
                                                    }
                                                }
                                            }

                                            if (likeList.size() > 0) {
                                                progressBar.setVisibility(View.GONE);
                                            }
                                            recyclerView.setAdapter(adapter);
                                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                        }
                                    });
                        }
                    }
                });
    }

    @Override
    public void onLikeListenerClick(int position) {
        UtilitiesKt.toastMessage(this, likeList.get(position).getIdUser());
    }
}
