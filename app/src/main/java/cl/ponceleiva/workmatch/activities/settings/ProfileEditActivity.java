package cl.ponceleiva.workmatch.activities.settings;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import cl.ponceleiva.workmatch.R;
import cl.ponceleiva.workmatch.utils.UtilitiesKt;

import static cl.ponceleiva.workmatch.utils.Constants.*;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class ProfileEditActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    private String name, email, phone, country, description;
    private EditText editTextName, editTextEmail, editTextPhone, editTextCountry, editTextDescription;
    private Button btnSaveChanges;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        UtilitiesKt.changeFullColorAppBar(this, getWindow(), getSupportActionBar());

        editTextName = findViewById(R.id.profileEditNameUser);
        editTextEmail = findViewById(R.id.profileEditEmailUser);
        editTextPhone = findViewById(R.id.profileEditPhoneUser);
        editTextCountry = findViewById(R.id.profileEditCountryUser);
        editTextDescription = findViewById(R.id.profileEditDescriptionUser);
        btnSaveChanges = findViewById(R.id.btn_save_changes);

        backButton = findViewById(R.id.actionbar_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        firebaseFirestore
                .collection("Users")
                .document(firebaseAuth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();

                            name = (doc.get("name") != null) ? doc.get("name").toString() : "";
                            email = (doc.get("email") != null) ? doc.get("email").toString() : "";
                            phone = (doc.get("phone") != null) ? doc.get("phone").toString() : "";
                            country = (doc.get("country") != null) ? doc.get("country").toString() : "";
                            description = (doc.get("description") != null) ? doc.get("description").toString() : "";

                            editTextName.setText(name);
                            editTextEmail.setText(email);
                            editTextPhone.setText(phone);
                            editTextCountry.setText(country);
                            editTextDescription.setText(description);
                        }
                    }
                });

        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, Object> data = new HashMap<>();

                name = editTextName.getText().toString();
                phone = editTextPhone.getText().toString();
                country = editTextCountry.getText().toString();
                description = editTextDescription.getText().toString();

                data.put("name", name);
                data.put("phone", phone);
                data.put("country", country);
                data.put("description", description);

                try {
                    firebaseFirestore.collection("Users").document(firebaseAuth.getUid()).set(data, SetOptions.merge());
                    UtilitiesKt.toastMessage(getApplicationContext(), "Datos actualizados");
                } catch (Exception e) {
                    UtilitiesKt.toastMessage(getApplicationContext(), "No se pudo actualizar los datos");
                    UtilitiesKt.logE(ERROR, "Details: " + e);
                }
            }
        });
    }
}
