package cl.ponceleiva.workmatch.activities.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import cl.ponceleiva.workmatch.R;
import cl.ponceleiva.workmatch.adapter.MatchContactAdapter;
import cl.ponceleiva.workmatch.model.MatchContact;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.List;

import cl.ponceleiva.workmatch.utils.UtilitiesKt;

import static cl.ponceleiva.workmatch.utils.Constants.*;

public class MatchListActivity extends AppCompatActivity implements MatchContactAdapter.OnMatchContactListener {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    private ArrayList<MatchContact> matchContacts = new ArrayList<>();
    private RecyclerView recyclerView;

    private ProgressBar progressBar;

    private TextView titleView;
    private ImageButton backButton;

    MatchContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_list);

        UtilitiesKt.changeFullColorAppBar(this, getWindow(), getSupportActionBar());
        titleView = findViewById(R.id.tvTitleOthers);
        backButton = findViewById(R.id.actionbar_back);
        progressBar = findViewById(R.id.progress_bar_match);

        titleView.setText("Matches");
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        try {
            adapter = new MatchContactAdapter(matchContacts, this, this);
            getDataMatches(firebaseAuth.getUid());
        } catch (Exception e) {
            UtilitiesKt.logE(ERROR, "Exception in getDataMatches. Details: " + e);
        }
    }

    private void getDataMatches(String currentUserId) {
        firebaseFirestore
                .collection("Users")
                .document(currentUserId)
                .collection("matches")
                .get()
                .addOnCompleteListener(
                        new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull final Task<QuerySnapshot> taskMatches) {
                                if (taskMatches.isSuccessful()) {
                                    final String message = (taskMatches.getResult().size() == 0) ? "List is empty" : taskMatches.getResult().size() + " element/s in the list";
                                    UtilitiesKt.logD(FIRESTORE, message);

                                    //Aquí se buscan los usuarios que que coincidan con la lista userMatchId anteriormente rescatada
                                    firebaseFirestore
                                            .collection("Users")
                                            .get()
                                            .addOnCompleteListener(
                                                    new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> taskUsers) {

                                                            if (taskUsers.getResult().getDocuments().size() > 0) {
                                                                progressBar.setVisibility(View.GONE);
                                                            }

                                                            // Se busca en la lista de usuarios, todos los userid de matchs para obtener
                                                            // la información de usuario correspondiente
                                                            for (DocumentSnapshot docMatch : taskMatches.getResult().getDocuments()) {
                                                                for (DocumentSnapshot userSnapshots : taskUsers.getResult().getDocuments()) {
                                                                    if (docMatch.getString("userid").equals(userSnapshots.getId())) {
                                                                        MatchContact contact = new MatchContact(
                                                                                userSnapshots.getString("profileImageUrl"),
                                                                                userSnapshots.getString("name"),
                                                                                docMatch.getId());

                                                                        matchContacts.add(contact);
                                                                    }
                                                                }
                                                            }
                                                            recyclerView.setAdapter(adapter);
                                                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                                                        }
                                                    });
                                } else {
                                    UtilitiesKt.logE(FIRESTORE, "It's not possible call bd: " + taskMatches);
                                }
                            }
                        }
                );
    }

    @Override
    public void onMatchContactClick(int position) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("chatId", matchContacts.get(position).getMatchId());
        startActivity(intent);
//        UtilitiesKt.toastMessage(this, matchContacts.get(position).getMatchId());
    }
}
