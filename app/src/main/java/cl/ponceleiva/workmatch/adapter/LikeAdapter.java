package cl.ponceleiva.workmatch.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cl.ponceleiva.workmatch.R;
import cl.ponceleiva.workmatch.model.Like;
import cl.ponceleiva.workmatch.utils.FirebaseUtilsKt;
import cl.ponceleiva.workmatch.utils.UtilitiesKt;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

public class LikeAdapter extends RecyclerView.Adapter<LikeAdapter.ViewHolder> {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private List<Like> likeList;
    private OnLikeListener mOnLikeListener;
    private Context context;

    public LikeAdapter(List<Like> likes, OnLikeListener mOnLikeListener, Context context) {
        this.likeList = likes;
        this.mOnLikeListener = mOnLikeListener;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView profileImage, matchImage;
        public TextView profileName;

        OnLikeListener onLikeListener;

        public ViewHolder(View itemView, OnLikeListener onLikeListener) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.like_image);
            matchImage = itemView.findViewById(R.id.like_match);
            profileName = itemView.findViewById(R.id.like_name);

            this.onLikeListener = onLikeListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onLikeListener.onLikeListenerClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public LikeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View likeView = inflater.inflate(R.layout.item_like, viewGroup, false);

        return new ViewHolder(likeView, mOnLikeListener);
    }

    @Override
    public void onBindViewHolder(LikeAdapter.ViewHolder viewHolder, int i) {
        final Like like = likeList.get(i);

        ImageView imageView = viewHolder.profileImage;
        final ImageView matchView = viewHolder.matchImage;
        TextView textView = viewHolder.profileName;

        matchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchView.setImageResource(R.drawable.ic_action_favorite_dark);
                UtilitiesKt.toastMessage(context, "Match! " + firebaseAuth.getUid());
                FirebaseUtilsKt.createChat(context, like.getIdProfessional(), like.getIdAnnounce(), firebaseAuth.getUid());
            }
        });
        textView.setText(like.getName());
        Picasso.with(context).load(like.getImage()).into(imageView);
    }

    @Override
    public int getItemCount() {
        return likeList.size();
    }

    public interface OnLikeListener {
        void onLikeListenerClick(int position);
    }
}
