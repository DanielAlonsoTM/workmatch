package cl.ponceleiva.workmatch.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cl.ponceleiva.workmatch.R;
import cl.ponceleiva.workmatch.model.MatchContact;

import java.util.List;

public class MatchContactAdapter extends
        RecyclerView.Adapter<MatchContactAdapter.ViewHolder> {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public ImageView imageViewMatch;
        public TextView textViewMatch;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            imageViewMatch = itemView.findViewById(R.id.match_image);
            textViewMatch = itemView.findViewById(R.id.match_name);
        }
    }

    private List<MatchContact> matchContacts;

    public MatchContactAdapter(List<MatchContact> contacts) {
        matchContacts = contacts;
    }

    @Override
    public MatchContactAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View matchContactView = inflater.inflate(R.layout.item_contact, parent, false);

        return new ViewHolder(matchContactView);
    }

    @Override
    public void onBindViewHolder(MatchContactAdapter.ViewHolder viewHolder, int position) {
        MatchContact matchContact = matchContacts.get(position);

        ImageView imageView = viewHolder.imageViewMatch;
        TextView textView = viewHolder.textViewMatch;

        imageView.setImageResource(R.drawable.common_google_signin_btn_icon_dark);
        textView.setText(matchContact.getName());
    }

    @Override
    public int getItemCount() {
        return matchContacts.size();
    }

}