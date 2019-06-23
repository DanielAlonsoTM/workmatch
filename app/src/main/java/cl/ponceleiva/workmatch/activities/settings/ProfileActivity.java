package cl.ponceleiva.workmatch.activities.settings;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import cl.ponceleiva.workmatch.R;
import cl.ponceleiva.workmatch.utils.UtilitiesKt;
import static cl.ponceleiva.workmatch.utils.Constants.*;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    private TextView titleBar, tvUserName, tvUserEmail, tvUserPhone, tvUserCountry, tvUserDescription;
    private ImageButton backButton;
    private Button btnEditProfile;
    private ImageView imageView;

    private String userName, userImage, userEmail, userPhone, userCountry, userDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        UtilitiesKt.changeFullColorAppBar(this, getWindow(), getSupportActionBar());

        Intent intent = getIntent();

        titleBar = findViewById(R.id.tvTitleOthers);
        tvUserName = findViewById(R.id.profileNameUser);
        tvUserEmail = findViewById(R.id.profileEmailUser);
        tvUserPhone = findViewById(R.id.profilePhoneUser);
        tvUserCountry = findViewById(R.id.profileCountryUser);
        tvUserDescription = findViewById(R.id.profileUserDescription);
        imageView = findViewById(R.id.profileImage);
        btnEditProfile = findViewById(R.id.edit_profile_btn);

        backButton = findViewById(R.id.actionbar_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (intent.hasExtra("userName")) {

            userName = intent.getStringExtra("userName");
            userImage = intent.getStringExtra("userName");
            userDescription = "Descripcion: la marmota carolina se compro una piscina en el la oficina de doña tina chiquitina para comer plasticina";

            titleBar.setText(userName);

            tvUserName.setText(userName);
            tvUserDescription.setText(userDescription);
            Picasso.with(this).load(userImage).into(imageView);
            btnEditProfile.setVisibility(View.GONE);

        } else {
            titleBar.setText("Mi Cuenta");

            firebaseFirestore
                    .collection("Users")
                    .document(firebaseAuth.getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot doc = task.getResult();

                                userName = doc.getString("name");
                                userImage = doc.get("profileImageUrl").toString();
                                userEmail = (doc.get("email") != null) ? doc.get("email").toString() : "E-mail no especificado";
                                userPhone = (doc.get("phone") != null) ? doc.get("phone").toString() : "Télefono no especificado";
                                userCountry = (doc.get("country") != null) ? doc.get("country").toString() : "Ciudad no especificada";
                                userDescription = (doc.get("description") != null) ? doc.get("description").toString() : "Descripción no especificada";

                                tvUserName.setText(userName);
                                Picasso.with(getApplicationContext()).load(userImage).into(imageView);
                                tvUserEmail.setText(userEmail);
                                tvUserPhone.setText(userPhone);
                                tvUserCountry.setText(userCountry);
                                tvUserDescription.setText(userDescription);
                            } else {
                                UtilitiesKt.logE(ERROR, "It's not possible load user data");
                            }

                        }
                    });
        }
    }
}
