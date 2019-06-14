package cl.ponceleiva.workmatch.activities.chat;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        UtilitiesKt.changeFullColorAppBar(this, getWindow(), getSupportActionBar(), getResources());
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

        //Carga inicial de la lista de mensajes
        firebaseFirestore
                .collection("Chats")
                .document("QuTAlS9jbDZzp1d4DeCe")
                .get()
                .addOnCompleteListener(
                        new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                messagesFromFirestore = (List<Map<String, Object>>) task.getResult().get("test");

                                for (Map<String, Object> data : messagesFromFirestore) {
                                    messages.add(data.get("content").toString());
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }
                );

        //Actualización de la lista de mensajes
        firebaseFirestore
                .collection("Chats")
                .document("QuTAlS9jbDZzp1d4DeCe")
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                        if (e != null) {
                            UtilitiesKt.logE("ERROR", "NUULLL");
                            return;
                        }

                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            messagesFromFirestore = (List<Map<String, Object>>) documentSnapshot.get("test");
//                            int lastElement = messagesFromFirestore.size() -1;
//                            if (messagesFromFirestore.size() > 0) {
//                                messages.add(messagesFromFirestore.get(lastElement).get("content").toString()+"--");
//                                adapter.notifyDataSetChanged();
//                            }


                        } else {
                            UtilitiesKt.logE("ERROR", "Current data: null");
                        }
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
