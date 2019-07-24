package cl.ponceleiva.workmatch.activities.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import cl.ponceleiva.workmatch.activities.chat.MatchListActivity;
import cl.ponceleiva.workmatch.activities.login.ChooseRegisterActivity;
import cl.ponceleiva.workmatch.activities.settings.SettingsActivity;
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

public class MainProfessionalActivity extends AppCompatActivity {

    private Date date = new Date();
    private Timestamp timestamp = new Timestamp(date);

    private SwipeRefreshLayout swipeRefreshLayout;
    private SwipeCardsView swipeCardsView;
    private List<Card> cardList = new ArrayList<>();

    private ImageButton messagesButton, settingsButtons;

    private ProgressBar progressBar;
    private TextView messageStatus;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private String currentUserId = firebaseAuth.getUid();

    private Animation fadeIn;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_professional);

        UtilitiesKt.changeFullColorAppBar(this, getWindow(), getSupportActionBar());

        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_tobottom);
        fadeIn.setDuration(1500);

        messagesButton = findViewById(R.id.actionbar_messages);
        settingsButtons = findViewById(R.id.actionbar_settings);

        swipeRefreshLayout = findViewById(R.id.content_cards);
        progressBar = findViewById(R.id.progress_bar_professional);
        messageStatus = findViewById(R.id.textStatus);

        swipeCardsView = findViewById(R.id.swipe_cards);
        swipeCardsView.retainLastCard(false);
        swipeCardsView.enableSwipe(true);

        firebaseFirestore
                .collection("Announces")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (!task.isSuccessful()) {
                            UtilitiesKt.logE(ERROR, "Fail to get announces. Details " + task.getException());
                        } else {
                            cardList.clear();
                            for (DocumentSnapshot docQuery : task.getResult().getDocuments()) {
                                UtilitiesKt.logD(SUCCESS, docQuery.getId());
                                cardList.add(
                                        new Card(
                                                docQuery.getString("title"),
                                                docQuery.getString("image"),
                                                docQuery.getId()));
                            }

                            checkExistingLike(cardList);
//                            showCards(cardList);
                        }
                    }
                });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        messagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainProfessionalActivity.this, MatchListActivity.class));
            }
        });

        settingsButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainProfessionalActivity.this, SettingsActivity.class));
            }
        });
    }

    private void checkExistingLike(final List<Card> cards) {
        firebaseFirestore
                .collection("Users")
                .document(currentUserId)
                .collection("likes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> listLikesCurrentUser = task.getResult().getDocuments();
                            List<Card> cardsToShow = new ArrayList<>();

                            // Obtener lista de documentos a los cuales aún no se ha dado like, en caso de ser primera
                            // vez se cargan todos los documentos por defecto
                            if (listLikesCurrentUser.size() > 0) {
                                for (Card card : cards) {
                                    for (DocumentSnapshot doc : listLikesCurrentUser) {
                                        if (!card.announceId.equals(doc.getString("announceId"))) {
                                            cardsToShow.add(card);
                                        }
                                    }
                                }
                                showCards(cardsToShow);
                            } else {
                                showCards(cards);
                            }
                        } else {
                            UtilitiesKt.logE(ERROR, "It's not possible view 'likes' list for current user.");
                        }
                    }
                });
    }

    private void showCards(@NonNull List<Card> cards) {
        if (!cards.isEmpty() && cards != null) {
            swipeRefreshLayout.startAnimation(fadeIn);
            progressBar.setVisibility(View.GONE);

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
                            addUserActionDocument(cardList.get(index));
                            break;
                    }
                }

                @Override
                public void onItemClick(View cardImageView, int index) {
                    intent = new Intent(getApplicationContext(), AnnounceActivity.class);
                    intent.putExtra("announceId", cardList.get(index).announceId);
                    startActivity(intent);
                }
            });
        } else {
            UtilitiesKt.logD(LOAD, "Empty list or null");
            String result = (cards.isEmpty()) ? "Cargando" : "Ha ocurrido un error de conexión";
            messageStatus.setVisibility(View.VISIBLE);
            messageStatus.setText(result);
        }
    }

    private void addUserActionDocument(@NonNull Card card) {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("announceId", card.announceId);
        objectMap.put("userId", firebaseAuth.getUid());
        objectMap.put("date", timestamp);

        try {
            firebaseFirestore
                    .collection("Users")
                    .document(currentUserId)
                    .collection("likes")
                    .add(objectMap);

            firebaseFirestore
                    .collection("Announces")
                    .document(card.announceId)
                    .collection("likes")
                    .add(objectMap);

            objectMap.clear();
            objectMap.put("likes", FieldValue.increment(1));


            firebaseFirestore
                    .collection("Announces")
                    .document(card.announceId)
                    .set(objectMap, SetOptions.merge());

            UtilitiesKt.toastMessage(getApplicationContext(), "Like!");
        } catch (Exception ex) {
            UtilitiesKt.logE(FIRESTORE, "Error al crear documento. Detalle: \n" + ex);
        }
    }

}
