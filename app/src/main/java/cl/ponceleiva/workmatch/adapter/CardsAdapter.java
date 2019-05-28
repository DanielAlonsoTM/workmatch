package cl.ponceleiva.workmatch.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cl.ponceleiva.workmatch.R;
import cl.ponceleiva.workmatch.model.Card;
import com.huxq17.swipecardsview.BaseCardAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CardsAdapter extends BaseCardAdapter {

    private List<Card> cardList;
    private Context context;

    public CardsAdapter(List<Card> cardList, Context context) {
        this.cardList = cardList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return cardList.size();
    }

    @Override
    public int getCardLayoutId() {
        return R.layout.item_card;
    }

    @Override
    public void onBindData(int position, View cardview) {
        if (cardList == null || cardList.size() == 0) {
            return;
        }

        ImageView imageView = (ImageView) cardview.findViewById(R.id.imageView);
        TextView textView = (TextView) cardview.findViewById(R.id.textView);
        Card card = cardList.get(position);
        textView.setText(card.getTitle());
        Picasso.with(context).load(card.getImage()).into(imageView);

    }
}
