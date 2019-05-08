package cl.ponceleiva.workmatch;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import cl.ponceleiva.workmatch.Adapter.CardsAdapter;
import cl.ponceleiva.workmatch.Model.Card;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.huxq17.swipecardsview.SwipeCardsView;

import java.util.*;

import cl.ponceleiva.workmatch.Utils.Constants;

public class MainActivity extends AppCompatActivity {

    //private String currentUserId = "MilpZSzXqRX5ggZt4AX6pMBcTSq1";
    //private String currentUserEmail = "danielinogordo@gmail.com";
    private String currentUserId = "PavGvxyR0RVkgDV6Tnl6xKDudF42";
    private String currentUserEmail = "test@gmail.com";
    private String typeUserInterested;

    private Constants constants = new Constants();

    private Date date = new Date();
    private Timestamp timestamp = new Timestamp(date);

    private SwipeCardsView swipeCardsView;
    private List<Card> cardList = new ArrayList<>();

    private TextView mTextMessage;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        swipeCardsView = findViewById(R.id.swipe_cards);
        swipeCardsView.retainLastCard(false);
        swipeCardsView.enableSwipe(true);

//        typeUserInterested = "Empleador";
        typeUserInterested = "Profesional";

        firebaseFirestore.collection("Users").whereEqualTo("typeUser", typeUserInterested).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        if (!documentSnapshot.get("email").equals(currentUserEmail)) {
                            Log.d("SUCCESS", documentSnapshot.getId());
                            cardList.add(new Card(documentSnapshot.get("name").toString(), documentSnapshot.get("profileImageUrl").toString(), documentSnapshot.getId()));
                        }
                    }
                } else {
                    Log.w("ERROR", "No se puede obtener la data");
                }
                getData(cardList);
            }
        });
    }

    private void getData(@NonNull List<Card> cards) {
        if (!cards.isEmpty() && cards != null) {
            Log.d(constants.LIST, cards.size() + " elementos en la lista");
            CardsAdapter cardsAdapter = new CardsAdapter(cards, this);
            swipeCardsView.setAdapter(cardsAdapter);
            swipeCardsView.setCardsSlideListener(new SwipeCardsView.CardsSlideListener() {
                @Override
                public void onShow(int index) {
                }

                @Override
                public void onCardVanish(int index, SwipeCardsView.SlideType type) {
                    switch (type) {
                        case LEFT:
                            toastMessage("Nope!");
                            break;
                        case RIGHT:
                            checkMatch(cardList.get(index));
                            break;
                    }
                }

                @Override
                public void onItemClick(View cardImageView, int index) {
                }
            });
        } else {
            Log.d("LOAD", "Loading...");
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void checkMatch(final @NonNull Card card) {
        //Checkear en la lista del usuario B (al que se dio like), si el usuario A (usuario de la sesión actual)
        firebaseFirestore.collection("Users").document(card.userId).collection("likes").whereEqualTo("userid", currentUserId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d(constants.FIRESTORE, "Cantidad de cooincidencias: " + task.getResult().size());

                    if (task.getResult().size() == 1 && task.getResult() != null) {
                        addDocument(card, "matches", "Maaatch!");
                    } else if (task.getResult().size() == 0 && task.getResult() != null) {
                        addDocument(card, "likes", "Likeeeeee!");
                    } else {
                        Log.w(constants.FIRESTORE, "Ocurrio un problema con el documento actual");
                    }
                }
            }
        });
    }

    private void addDocument(@NonNull Card card, String collectionName, String message) {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("userid", card.userId);
        objectMap.put("date", timestamp);

        try {
            firebaseFirestore.collection("Users").document(currentUserId).collection(collectionName).add(objectMap);

            //Se añade documento a usuario con el que se dio match. --> Redactar mejor xd
            if (collectionName.equals("matches")) {
                firebaseFirestore.collection("Users").document(card.userId).collection(collectionName).add(objectMap);
            }

            toastMessage(message);
            Log.d(constants.FIRESTORE, "Documento/s creado");
        } catch (Exception ex) {
            Log.w(constants.FIRESTORE, "Error al crear documento. Detalle: \n" + ex);
        }
    }
}
