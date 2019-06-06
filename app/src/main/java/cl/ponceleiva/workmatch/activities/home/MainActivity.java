package cl.ponceleiva.workmatch.activities.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import cl.ponceleiva.workmatch.activities.chat.ChatActivity;
import cl.ponceleiva.workmatch.adapter.CardsAdapter;
import cl.ponceleiva.workmatch.model.Card;

import cl.ponceleiva.workmatch.R;
import cl.ponceleiva.workmatch.utils.UtilitiesKt;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;
import com.huxq17.swipecardsview.SwipeCardsView;

import java.util.*;

import static cl.ponceleiva.workmatch.utils.Constants.*;

public class MainActivity extends AppCompatActivity {

    private Date date = new Date();
    private Timestamp timestamp = new Timestamp(date);

    private SwipeCardsView swipeCardsView;
    private List<Card> cardList = new ArrayList<>();

    private TextView mTextMessage;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private String currentUserId;
    private String currentUserEmail;
    private String typeUser;
    private String typeUserInterested;
    private SharedPreferences sharedPreferences;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_profile);
                    return true;
                case R.id.navigation_messages:
                    mTextMessage.setText(R.string.title_messages);
                    startActivity(new Intent(MainActivity.this, ChatActivity.class));
                    return true;
            }
            return false;
        }
    };

    @Override
    public void onStart() {
        super.onStart();
//        currentUserId = getIntent().getStringExtra("userUid");
//        currentUserEmail = getIntent().getStringExtra("userEmail");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        currentUserId = firebaseAuth.getUid();
        currentUserEmail = firebaseAuth.getCurrentUser().getEmail();
//        typeUser = FirebaseUtilsKt.getTypeUser(currentUserId, getApplicationContext());
//        FirebaseUtilsKt.getTypeUser(currentUserId, getApplicationContext());
//        typeUser = sharedPreferences.getString("typeUser", null);

//        try {
//            switch (typeUser) {
//                case "Empleador":
//                    typeUserInterested = "Profesional";
//                    break;
//                case "Profesional":
//                    typeUserInterested = "Empleador";
//                    break;
//            }
//        } catch (Exception e) {
//            UtilitiesKt.logE(ERROR, "No se pudo obtener datos de tipo de usuario. Consulte log.");
//        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UtilitiesKt.changeFullColorAppBar(this, getWindow(), getSupportActionBar(), getResources());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        swipeCardsView = findViewById(R.id.swipe_cards);
        swipeCardsView.retainLastCard(false);
        swipeCardsView.enableSwipe(true);

        typeUserInterested = "Empleador";
//        UtilitiesKt.logD("TEST-TipoUsuario", typeUserInterested);
//        System.out.println(typeUserInterested);
//        typeUserInterested = "Profesional";

        firebaseFirestore.collection("Users").whereEqualTo("typeUser", typeUserInterested).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        if (!documentSnapshot.get("email").equals(currentUserEmail)) {
                            UtilitiesKt.logD(SUCCESS, documentSnapshot.getId());
                            cardList.add(new Card(documentSnapshot.get("name").toString(), documentSnapshot.get("profileImageUrl").toString(), documentSnapshot.getId()));
                        }
                    }
                } else {
                    UtilitiesKt.logE(ERROR, "No se puede obtener la data");
                }
                getData(cardList);
            }
        });
    }

    private void getData(@NonNull List<Card> cards) {
        if (!cards.isEmpty() && cards != null) {
            UtilitiesKt.logD(LIST, cards.size() + " elementos en la lista");
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
                            UtilitiesKt.toastMessage(getApplicationContext(), "Nope!");
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
            UtilitiesKt.logD(LOAD, "Loading...");
        }
    }

    private void checkMatch(final @NonNull Card card) {
        //Checkear en la lista del usuario B (al que se dio like), si el usuario A (usuario de la sesión actual)
        firebaseFirestore.collection("Users").document(card.userId).collection("likes").whereEqualTo("userid", currentUserId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    UtilitiesKt.logD(FIRESTORE, "Cantidad de cooincidencias: " + task.getResult().size());
                    if (task.getResult().size() == 1 && task.getResult() != null) {
                        addUserActionDocument(card, "matches", "Maaatch!");
                    } else if (task.getResult().size() == 0 && task.getResult() != null) {
                        addUserActionDocument(card, "likes", "Likeeeeee!");
                    } else {
                        UtilitiesKt.logE(FIRESTORE, "Ocurrio un problema con el documento actual");
                    }
                }
            }
        });
    }

    private void addUserActionDocument(@NonNull Card card, String collectionName, String message) {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("userid", card.userId);
        objectMap.put("date", timestamp);

        try {
            firebaseFirestore.collection("Users").document(currentUserId).collection(collectionName).add(objectMap);

            //Se añade documento a usuario con el que se dio match. --> Redactar mejor xd
            if (collectionName.equals("matches")) {
                firebaseFirestore.collection("Users").document(card.userId).collection(collectionName).add(objectMap);
            }
            UtilitiesKt.logD(FIRESTORE, "Documento/s creado");
            UtilitiesKt.toastMessage(getApplicationContext(), message);
        } catch (Exception ex) {
            UtilitiesKt.logE(FIRESTORE, "Error al crear documento. Detalle: \n" + ex);
        }
    }

}
