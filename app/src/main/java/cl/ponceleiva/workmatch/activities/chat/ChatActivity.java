package cl.ponceleiva.workmatch.activities.chat;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import cl.ponceleiva.workmatch.R;
import cl.ponceleiva.workmatch.adapter.ChatMessageAdapter;
import cl.ponceleiva.workmatch.model.ChatMessage;
import cl.ponceleiva.workmatch.utils.UtilitiesKt;

import static cl.ponceleiva.workmatch.utils.Constants.*;

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

    private List<ChatMessage> messages = new ArrayList<>();

    private Intent intent;
    private String chatId;

    private Date currentDate;
    private Timestamp dateTimestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        intent = getIntent();
        chatId = intent.getStringExtra("chatId");

        UtilitiesKt.changeFullColorAppBar(this, getWindow(), getSupportActionBar());
        animation = AnimationUtils.loadAnimation(this, R.anim.fade_totop);

        linearLayout = findViewById(R.id.linear_layout_bottom);
        message = findViewById(R.id.edit_text_message);
        fab = findViewById(R.id.fab_send_message);
        listView = findViewById(R.id.list_messages);
        backButton = findViewById(R.id.actionbar_back);

        linearLayout.startAnimation(animation);


        listView.smoothScrollToPosition(messages.size() - 1);
        listView.setDividerHeight(0);
        listView.setDivider(null);
//        listView.setAdapter(chatMessageAdapter);

        firebaseFirestore
                .collection("Chats")
                .document(chatId)
                .collection("messages")
                .orderBy("date", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            UtilitiesKt.logE(ERROR, "Listen failed. Details: " + e);
                            return;
                        }

                        List<DocumentSnapshot> messagesSnapshots = queryDocumentSnapshots.getDocuments();

                        messages.clear();

                        for (DocumentSnapshot docMessages : messagesSnapshots) {
                            messages.add(
                                    new ChatMessage(
                                            docMessages.getId(),
                                            docMessages.getString("userId"),
                                            docMessages.getTimestamp("date"),
                                            docMessages.getString("content")));
                        }

                        ChatMessageAdapter chatMessageAdapter = new ChatMessageAdapter(getApplicationContext(), messages);
                        listView.setAdapter(chatMessageAdapter);
                        chatMessageAdapter.notifyDataSetChanged();

                    }
                });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message.setError(null);
                String text = message.getText().toString();
                if (!text.isEmpty()) {
                    message.getText().clear();

                    currentDate = new Date();
                    dateTimestamp = new Timestamp(currentDate);

                    Map<String, Object> messageContent = new HashMap<>();
                    messageContent.put("content", text);
                    messageContent.put("date", dateTimestamp);
                    messageContent.put("userId", firebaseAuth.getUid());

                    firebaseFirestore
                            .collection("Chats")
                            .document(chatId)
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
