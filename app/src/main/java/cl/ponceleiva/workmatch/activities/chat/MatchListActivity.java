package cl.ponceleiva.workmatch.activities.chat;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class MatchListActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    private ArrayList<MatchContact> matchContacts = new ArrayList<>();
    private RecyclerView recyclerView;

    private TextView titleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_list);

        UtilitiesKt.changeFullColorAppBar(this, getWindow(), getSupportActionBar());

        recyclerView = findViewById(R.id.recyclerView);
        try {
            getDataMatches(firebaseAuth.getUid());
        } catch (Exception e) {
            UtilitiesKt.logE(ERROR, "Exception in getDataMatches. Details: " + e);
        }
    }

    private void getDataMatches(String userUid) {
        firebaseFirestore
                .collection("Users")
                .document(userUid)
                .collection("likes")
                .get()
                .addOnCompleteListener(
                        new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    String message = (task.getResult().size() == 0) ? "List is empty" : task.getResult().size() + " elemtent/s in the list";
                                    UtilitiesKt.logD(FIRESTORE, message);

                                    final List<String> matchId = new ArrayList<>();
                                    for (DocumentSnapshot doc : task.getResult().getDocuments()) {
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
                                                            for (DocumentSnapshot userSnapshots : task.getResult().getDocuments()) {
                                                                if (matchId.contains(userSnapshots.getId())) {
                                                                    matchContacts.add(
                                                                            new MatchContact("", userSnapshots.get("name").toString()));
                                                                }
                                                            }
                                                            MatchContactAdapter adapter = new MatchContactAdapter(matchContacts);
                                                            recyclerView.setAdapter(adapter);
                                                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                                                        }
                                                    });
                                } else {
                                    UtilitiesKt.logE(FIRESTORE, "It's not possible call bd: " + task);
                                }
                            }
                        }
                );
    }
}
