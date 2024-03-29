package cl.ponceleiva.workmatch.activities.login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import cl.ponceleiva.workmatch.R;
import cl.ponceleiva.workmatch.activities.home.MainProfessionalActivity;
import cl.ponceleiva.workmatch.utils.Constants;
import cl.ponceleiva.workmatch.utils.FirebaseUtilsKt;
import cl.ponceleiva.workmatch.utils.UtilitiesKt;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private AutoCompleteTextView mEmail, mName;
    private EditText mPassword;
    private RadioGroup mRadioGroup;
    private Button mRegister;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        UtilitiesKt.changeColorInitialsViews(getApplicationContext(), getWindow());

        mAuth = FirebaseAuth.getInstance();

        mEmail = findViewById(R.id.register_email);
        mName = findViewById(R.id.register_name);
        mPassword = findViewById(R.id.register_password);
        mRadioGroup = findViewById(R.id.radioGroup);
        mRegister = findViewById(R.id.register_button);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();
                final String name = mName.getText().toString();

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            UtilitiesKt.toastMessage(getApplicationContext(), "Sign up Error");
                            UtilitiesKt.logE(Constants.FIREBASEAUTH, task.getResult().toString());
                        } else {
                            HashMap<String, Object> userData = new HashMap<>();
                            userData.put("name", name);
                            userData.put("email", email);
                            userData.put("typeUser", "No definido");
                            userData.put("profileImageUrl", "default");


                            //Reemplartearse en como se ejecuta esto
                            FirebaseUtilsKt.createUser(userData, mAuth.getUid());
                            Intent intent = new Intent(RegisterActivity.this, MainProfessionalActivity.class);
                            intent.putExtra("userUid", mAuth.getUid());
                            intent.putExtra("userEmail", name);
                            startActivity(intent);
                        }
                    }
                });
            }
        });
    }
}
