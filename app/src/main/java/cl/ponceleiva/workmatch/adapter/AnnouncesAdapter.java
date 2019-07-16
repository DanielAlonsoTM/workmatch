package cl.ponceleiva.workmatch.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cl.ponceleiva.workmatch.R;
import cl.ponceleiva.workmatch.activities.home.AnnounceActivity;
import cl.ponceleiva.workmatch.model.Announce;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AnnouncesAdapter extends BaseAdapter {

    private List<Announce> listAnnounces;
    private Context context;

    public AnnouncesAdapter(List<Announce> listAnnounces, Context context) {
        this.listAnnounces = listAnnounces;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listAnnounces.size();
    }

    @Override
    public Object getItem(int position) {
        return listAnnounces.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;

        if (convertView == null) {
            gridView = new View(context);
            gridView = inflater.inflate(R.layout.item_announce, null);
            TextView title, date;
            title = gridView.findViewById(R.id.announce_title);
            date = gridView.findViewById(R.id.announce_date);

            title.setText(listAnnounces.get(position).getTitle());
            date.setText(listAnnounces.get(position).getDate());

            gridView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AnnounceActivity.class);
                    intent.putExtra("announceId", listAnnounces.get(position).getAnnounceId());
                    context.startActivity(intent);
                }
            });

            ImageView imageView = gridView.findViewById(R.id.announce_image);
            Picasso.with(context).load(listAnnounces.get(position).getImage()).into(imageView);

        } else {
            gridView = convertView;
        }
        return gridView;
    }
}
