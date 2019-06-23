package cl.ponceleiva.workmatch.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class SlideAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public String[] listTypes = {
            "Profesional",
            "Empleador"
    };

    @Override
    public int getCount() {
        return listTypes.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==(LinearLayout)object);
    }


}
