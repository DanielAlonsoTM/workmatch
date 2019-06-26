package cl.ponceleiva.workmatch.activities.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
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

    private TextView titleView, textViewMessage;
    private ImageButton backButton;

    MatchContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_list);

        UtilitiesKt.changeFullColorAppBar(this, getWindow(), getSupportActionBar());
        titleView = findViewById(R.id.tvTitleOthers);
        textViewMessage = findViewById(R.id.text_matchlist_message);
        backButton = findViewById(R.id.actionbar_back);

        titleView.setText("Matches");
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        try {
            adapter = new MatchContactAdapter(matchContacts, this);
            getDataMatches(firebaseAuth.getUid());
        } catch (Exception e) {
            UtilitiesKt.logE(ERROR, "Exception in getDataMatches. Details: " + e);
        }
    }

    private void getDataMatches(String userUid) {
        firebaseFirestore
                .collection("Users")
                .document(userUid)
                .collection("matches")
                .get()
                .addOnCompleteListener(
                        new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    final String message = (task.getResult().size() == 0) ? "List is empty" : task.getResult().size() + " element/s in the list";
                                    UtilitiesKt.logD(FIRESTORE, message);

                                    final MatchContact matchContact = new MatchContact("", "", "");

                                    final List<String> matchId = new ArrayList<>();
                                    for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                                        matchContact.setMatchId(doc.getId());
                                        matchId.add(doc.get("userid").toString());
                                    }

                                    //Aquí se buscan los usuarios que que coincidan con la lista matchId anteriormente rescatada
                                    firebaseFirestore
                                            .collection("Users")
                                            .get()
                                            .addOnCompleteListener(
                                                    new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                            if (task.getResult().getDocuments().size() > 0) {
                                                                textViewMessage.setVisibility(View.GONE);
                                                            }

                                                            for (DocumentSnapshot userSnapshots : task.getResult().getDocuments()) {
                                                                if (matchId.contains(userSnapshots.getId())) {
                                                                    matchContact.setImage("");
                                                                    matchContact.setName(userSnapshots.get("name").toString());
//                                                                    matchContacts.add(new MatchContact("", userSnapshots.get("name").toString(), userSnapshots.getId()));
                                                                    matchContacts.add(matchContact);
                                                                }
                                                            }
                                                            recyclerView.setAdapter(adapter);
                                                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                                                        }
                                                    });
                                } else {
                                    textViewMessage.setText("No se pudo cargar elementos");
                                    UtilitiesKt.logE(FIRESTORE, "It's not possible call bd: " + task);
                                }
                            }
                        }
                );
    }

    @Override
    public void onMatchContactClick(int position) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("contactUserId", matchContacts.get(position).getMatchId());
        startActivity(intent);
    }
}
