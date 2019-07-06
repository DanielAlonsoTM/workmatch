package cl.ponceleiva.workmatch.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import cl.ponceleiva.workmatch.utils.UtilitiesKt;

import java.util.List;

public class AnnouncesAdapter extends BaseAdapter {

    List<String> listAnnounces;
    Context context;

    public AnnouncesAdapter(List<String> listAnnounces, Context context) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final Button button;
        if (convertView == null) {
            button = new Button(context);
            button.setText(listAnnounces.get(position));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UtilitiesKt.toastMessage(context, button.getText().toString());
                }
            });
        } else {
            button = (Button)convertView;
        }
        return button;
    }
}
