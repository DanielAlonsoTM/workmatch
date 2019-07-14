package cl.ponceleiva.workmatch.activities.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import cl.ponceleiva.workmatch.activities.chat.MatchListActivity;
import cl.ponceleiva.workmatch.activities.login.ChooseRegisterActivity;
import cl.ponceleiva.workmatch.activities.settings.ProfileActivity;
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
import com.google.firebase.firestore.EventListener;
import com.huxq17.swipecardsview.SwipeCardsView;

import javax.annotation.Nullable;
import java.util.*;

import static cl.ponceleiva.workmatch.utils.Constants.*;

public class MainProfessionalActivity extends AppCompatActivity {

    private Date date = new Date();
    private Timestamp timestamp = new Timestamp(date);

    private RelativeLayout relativeLayout;
    private SwipeCardsView swipeCardsView;
    private List<Card> cardList = new ArrayList<>();

    private ImageButton messagesButton, settingsButtons;

    private ProgressBar progressBar;
    private TextView messageStatus;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private String currentUserId;
    private String currentUserEmail;
    private String typeUserInterested;
    private SharedPreferences sharedPreferences;

    private Animation fadeIn;

    private Intent intent;

    @Override
    public void onStart() {
        super.onStart();

        try {
            currentUserId = firebaseAuth.getUid();
            currentUserEmail = firebaseAuth.getCurrentUser().getEmail();
        } catch (Exception e) {
            UtilitiesKt.logE(ERROR, "It's not possible get current user. Details: " + e);
            startActivity(new Intent(this, ChooseRegisterActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_professional);

        sharedPreferences = getSharedPreferences("pref", MODE_PRIVATE);

        UtilitiesKt.changeFullColorAppBar(this, getWindow(), getSupportActionBar());

        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_tobottom);
        fadeIn.setDuration(1500);

        messagesButton = findViewById(R.id.actionbar_messages);
        settingsButtons = findViewById(R.id.actionbar_settings);

        relativeLayout = findViewById(R.id.content_cards);
        progressBar = findViewById(R.id.progress_bar_professional);
        messageStatus = findViewById(R.id.textStatus);

        swipeCardsView = findViewById(R.id.swipe_cards);
        swipeCardsView.retainLastCard(false);
        swipeCardsView.enableSwipe(true);

        //Controlar excepción para esto
        if (sharedPreferences.getString("typeUser", null) == null) {
            startActivity(new Intent(this, ChooseRegisterActivity.class));
        } else {
            switch (sharedPreferences.getString("typeUser", null)) {
                case "Profesional":
                    UtilitiesKt.logD("SHAREDPREFERENCES", "Profesional");
                    typeUserInterested = "Empleador";
                    break;
                case "Empleador":
                    UtilitiesKt.logD("SHAREDPREFERENCES", "Empleador");
                    typeUserInterested = "Profesional";
                    break;
                default:
                    UtilitiesKt.logD("SHAREDPREFERENCES", "Something wrong");
                    break;
            }
        }

        firebaseFirestore
                .collection("Users")
                .whereEqualTo("typeUser", typeUserInterested)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            UtilitiesKt.logE(ERROR, "Listen failed. Details: " + e);
                            return;
                        }
                        for (DocumentSnapshot docQuery : queryDocumentSnapshots.getDocuments()) {
                            if (!docQuery.get("email").equals(currentUserEmail)) {
                                UtilitiesKt.logD(SUCCESS, docQuery.getId());
                                cardList.add(
                                        new Card(
                                                docQuery.get("name").toString(),
                                                docQuery.get("profileImageUrl").toString(),
                                                docQuery.getId()));
                            }
                        }
                        showCards(cardList);
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

    private void showCards(@NonNull List<Card> cards) {
        if (!cards.isEmpty() && cards != null) {

            relativeLayout.startAnimation(fadeIn);
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
                            checkMatch(cardList.get(index));
                            break;
                    }
                }

                @Override
                public void onItemClick(View cardImageView, int index) {
                    firebaseFirestore
                            .collection("Users")
                            .document(cardList.get(index).userId)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot doc = task.getResult();

                                        try {
                                            intent = new Intent(getApplicationContext(), ProfileActivity.class);
                                            intent.putExtra("userName", doc.get("name").toString());
                                            intent.putExtra("typeUser", doc.get("typeUser").toString());
                                            intent.putExtra("userImage", doc.get("profileImageUrl").toString());

                                            startActivity(intent);
                                        } catch (Exception e) {
                                            UtilitiesKt.logE(ERROR, "It's not possible get data from document");
                                            UtilitiesKt.toastMessage(getApplicationContext(), "No es posible cargar el perfil seleccionado. Intente más tarde");
                                        }
                                    }
                                }
                            });
                }
            });
        } else {
            UtilitiesKt.logD(LOAD, "Empty list or null");
//            String result = (cards.isEmpty()) ? "List don't have any elements" : "It's not possible load list elements";
            String result = (cards.isEmpty()) ? "Cargando" : "Ha ocurrido un error de conexión";
            messageStatus.setVisibility(View.VISIBLE);
            messageStatus.setText(result);
        }
    }

    private void checkMatch(final @NonNull Card card) {
        //Checkear en la lista del usuario B (al que se dio like), si el usuario A (usuario de la sesión actual)
        firebaseFirestore
                .collection("Users")
                .document(card.userId)
                .collection("likes")
                .whereEqualTo("userid", currentUserId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
            //Se añade documento a usuario con el que se dio match. --> Redactar mejor xd
            if (collectionName.equals("matches")) {
                //Se crea de igual manera el like en el usuario actual
                firebaseFirestore
                        .collection("Users")
                        .document(currentUserId)
                        .collection("likes")
                        .add(objectMap);

                //Se añade match a usuario actual
                firebaseFirestore
                        .collection("Users")
                        .document(currentUserId)
                        .collection(collectionName)
                        .document(card.userId + "+" + currentUserId).set(objectMap);

                //Se limpia objeto de datos, y se añade match a usuario con el que se hizo match
                objectMap.clear();
                objectMap.put("userid", currentUserId);
                objectMap.put("date", timestamp);
                firebaseFirestore.collection("Users")
                        .document(card.userId)
                        .collection(collectionName)
                        .document(card.userId + "+" + currentUserId).set(objectMap);

                //Se crea documento en chat, que comparte id de match
                firebaseFirestore
                        .collection("Chats")
                        .add(card.userId + "+" + currentUserId);
                UtilitiesKt.logD(FIRESTORE, "Documentos creados");

                UtilitiesKt.toastMessage(getApplicationContext(), message);
            } else if (collectionName.equals("likes")) {
                firebaseFirestore.collection("Users").document(currentUserId).collection(collectionName).add(objectMap);
                UtilitiesKt.logD(FIRESTORE, "Documento creado");

                UtilitiesKt.toastMessage(getApplicationContext(), message);
            }
        } catch (Exception ex) {
            UtilitiesKt.logE(FIRESTORE, "Error al crear documento. Detalle: \n" + ex);
        }
    }
}