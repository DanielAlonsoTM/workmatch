package cl.ponceleiva.workmatch.activities.chat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import cl.ponceleiva.workmatch.R;
import cl.ponceleiva.workmatch.model.ChatMessage;
import cl.ponceleiva.workmatch.utils.UtilitiesKt;

import static cl.ponceleiva.workmatch.utils.Constants.*;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;
import com.google.firebase.firestore.EventListener;

import javax.annotation.Nullable;
import java.util.*;

public class ChatActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private Animation animation;

    private LinearLayout linearLayout;
    private EditText message;
    private FloatingActionButton fab;
    private ListView listView;
    private ImageButton backButton;

    private List<String> messages;
    private List<Map<String, Object>> messagesFromFirestore;

    private Intent intent;
    private String contactUserId;

    private Date currentDate;
    private Timestamp dateTimestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        intent = getIntent();
        contactUserId = intent.getStringExtra("contactUserId");

        UtilitiesKt.changeFullColorAppBar(this, getWindow(), getSupportActionBar());
        animation = AnimationUtils.loadAnimation(this, R.anim.fade_totop);

        linearLayout = findViewById(R.id.linear_layout_bottom);
        message = findViewById(R.id.edit_text_message);
        fab = findViewById(R.id.fab_send_message);
        listView = findViewById(R.id.list_messages);
        backButton = findViewById(R.id.actionbar_back);

        linearLayout.startAnimation(animation);

        messages = new ArrayList<>();
        messagesFromFirestore = new ArrayList<>();

        final ArrayAdapter adapter = new ArrayAdapter<>(this,
                R.layout.item_message, messages);

        listView.setDividerHeight(0);
        listView.setDivider(null);
        listView.setAdapter(adapter);
        listView.smoothScrollToPosition(messages.size() - 1);

        firebaseFirestore
                .collection("Chats")
                .document(contactUserId)
                .collection("messages")
                .orderBy("date")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            UtilitiesKt.logE(ERROR, "Listen failed. Details: " + e);
                            return;
                        }

                        messages.clear();
                        for (QueryDocumentSnapshot queryDoc : queryDocumentSnapshots) {
                            if (queryDoc.get("content") != null) {
                                messages.add(queryDoc.get("content").toString());
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message.setError(null);
                String text = message.getText().toString();
                if (!text.isEmpty()) {
                    messages.add(text);
                    adapter.notifyDataSetChanged();
                    message.getText().clear();

                    currentDate = new Date();
                    dateTimestamp = new Timestamp(currentDate);

                    Map<String, Object> messageContent = new HashMap<>();
                    messageContent.put("content", text);
                    messageContent.put("date", dateTimestamp);
                    messageContent.put("userId", firebaseAuth.getUid());

                    firebaseFirestore
                            .collection("Chats")
                            .document(contactUserId)
                            .collection("messages")
                            .add(messageContent);
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
