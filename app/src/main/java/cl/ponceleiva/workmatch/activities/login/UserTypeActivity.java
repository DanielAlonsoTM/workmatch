package cl.ponceleiva.workmatch.activities.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import cl.ponceleiva.workmatch.R;
import cl.ponceleiva.workmatch.activities.home.MainEmployerActivity;
import cl.ponceleiva.workmatch.activities.home.MainProfessionalActivity;
import cl.ponceleiva.workmatch.adapter.CardsAdapter;
import cl.ponceleiva.workmatch.model.Card;
import cl.ponceleiva.workmatch.utils.UtilitiesKt;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.huxq17.swipecardsview.SwipeCardsView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserTypeActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    private SwipeCardsView swipeCardsView;
    private List<Card> cardList = new ArrayList<>();

    private SharedPreferences pref;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usertype);

        UtilitiesKt.changeColorInitialsViews(this, getWindow());

        SharedPreferences sharedPreferences = getSharedPreferences("pref", MODE_PRIVATE);

        if (sharedPreferences.getString("typeUser", null) == null || sharedPreferences.getString("typeUser", null).isEmpty()) {
            swipeCardsView = findViewById(R.id.swipe_cards_user_type);
            swipeCardsView.retainLastCard(false);
            swipeCardsView.enableSwipe(true);


            cardList.add(new Card("¿Qué tipo de usuario eres?\nMueve hacia izquierda o derecha", "empty", "01"));

            CardsAdapter cardsAdapter = new CardsAdapter(cardList, this);
            swipeCardsView.setAdapter(cardsAdapter);
            swipeCardsView.setCardsSlideListener(new SwipeCardsView.CardsSlideListener() {
                @Override
                public void onShow(int index) {
                }

                @Override
                public void onCardVanish(int index, SwipeCardsView.SlideType type) {
                    switch (type) {
                        case LEFT:
                            goToHome("Profesional");
                            UtilitiesKt.toastMessage(getApplicationContext(), "Profesional");
                            break;
                        case RIGHT:
                            goToHome("Empleador");
                            UtilitiesKt.toastMessage(getApplicationContext(), "Empleador");
                            break;
                    }
                }

                @Override
                public void onItemClick(View cardImageView, int index) {
                }
            });
        }
//        else {
//            startActivity(new Intent(this, MainProfessionalActivity.class));
//        }

    }

    private void goToHome(String typeUser) {
        pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("typeUser", typeUser);

        firebaseFirestore.collection("Users").document(firebaseAuth.getUid()).update(objectMap);

        editor.putString("typeUser", typeUser);
        editor.commit();

        if (typeUser.equals("Profesional")) {
            startActivity(new Intent(this, MainProfessionalActivity.class));
        } else if (typeUser.equals("Empleador")) {
            startActivity(new Intent(this, MainEmployerActivity.class));
        }

    }
}
