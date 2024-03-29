package cl.ponceleiva.workmatch.adapter;

import android.content.Context;
import android.support.constraint.Constraints;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cl.ponceleiva.workmatch.R;
import cl.ponceleiva.workmatch.model.ChatMessage;
import com.google.firebase.auth.FirebaseAuth;

import android.widget.RelativeLayout.LayoutParams;

import java.util.List;

public class ChatMessageAdapter extends BaseAdapter {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    Context context;
    List<ChatMessage> chatMessageList;

    public ChatMessageAdapter(Context context, List<ChatMessage> chatMessageList) {
        this.context = context;
        this.chatMessageList = chatMessageList;
    }

    @Override
    public int getCount() {
        return chatMessageList.size();
    }

    @Override
    public Object getItem(int position) {
        return chatMessageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ChatMessage chatMessage = chatMessageList.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;

        if (convertView == null) {
            view = new View(context);
            view = inflater.inflate(R.layout.item_message, null);

            TextView message = view.findViewById(R.id.message_content);

            //Si el mensaje ha sido por el usuario actual se aliena a la derecha
            if (chatMessage.getUserId().equals(firebaseAuth.getUid())) {
                LayoutParams layoutParams = (LayoutParams) message.getLayoutParams();
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END);
            }


            message.setText(chatMessage.getContent());
        } else {
            view = convertView;
        }

        return view;
    }
}
