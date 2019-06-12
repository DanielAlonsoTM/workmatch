package cl.ponceleiva.workmatch.activities.chat;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import cl.ponceleiva.workmatch.R;
import cl.ponceleiva.workmatch.utils.UtilitiesKt;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private Animation animation;

    private LinearLayout linearLayout;
    private EditText message;
    private FloatingActionButton fab;
    private ListView listView;
    private ImageButton backButton;

    private List<String> messages;

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

        final ArrayAdapter adapter = new ArrayAdapter<>(this,
                R.layout.item_message, messages);

        listView.setDividerHeight(0);
        listView.setDivider(null);
        listView.setAdapter(adapter);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message.setError(null);
                String text = message.getText().toString();
                if (!text.isEmpty()) {
                    messages.add(text);
                    adapter.notifyDataSetChanged();
                    listView.smoothScrollToPosition(messages.size() - 1);
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
