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

public class MatchContactAdapter extends RecyclerView.Adapter<MatchContactAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imageViewMatch;
        public TextView textViewMatch;

        OnMatchContactListener onMatchContactListener;

        public ViewHolder(View itemView, OnMatchContactListener onMatchContactListener) {
            super(itemView);

            imageViewMatch = itemView.findViewById(R.id.match_image);
            textViewMatch = itemView.findViewById(R.id.match_name);
            this.onMatchContactListener = onMatchContactListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onMatchContactListener.onMatchContactClick(getAdapterPosition());
        }
    }

    private List<MatchContact> matchContacts;
    private OnMatchContactListener mOnMatchContactsListener;

    public MatchContactAdapter(List<MatchContact> matchContacts, OnMatchContactListener mOnMatchContactsListener) {
        this.matchContacts = matchContacts;
        this.mOnMatchContactsListener = mOnMatchContactsListener;
    }

    @Override
    public MatchContactAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View matchContactView = inflater.inflate(R.layout.item_contact, parent, false);

        return new ViewHolder(matchContactView, mOnMatchContactsListener);
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

    public interface OnMatchContactListener {
        void onMatchContactClick(int position);
    }
}