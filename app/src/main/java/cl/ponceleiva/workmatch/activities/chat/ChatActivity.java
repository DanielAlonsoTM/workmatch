package cl.ponceleiva.workmatch.activities.chat;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import cl.ponceleiva.workmatch.R;
import cl.ponceleiva.workmatch.utils.UtilitiesKt;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private EditText message;
    private FloatingActionButton fab;
    private ListView listView;

    private List<String> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        message = findViewById(R.id.edit_text_message);
        fab = findViewById(R.id.fab_send_message);
        listView = findViewById(R.id.list_messages);

        messages = new ArrayList<>();

        final ArrayAdapter adapter = new ArrayAdapter<>(this,
                R.layout.activity_listview, messages);

        listView.setAdapter(adapter);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message.setError(null);
                String text = message.getText().toString();
                if (!text.isEmpty()) {
                    messages.add(text);
                    adapter.notifyDataSetChanged();
                    listView.smoothScrollToPosition(messages.size()+1);
                    message.getText().clear();
                }
            }
        });
    }
}
