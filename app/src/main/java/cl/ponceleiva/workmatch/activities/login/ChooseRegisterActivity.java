package cl.ponceleiva.workmatch.activities.login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import cl.ponceleiva.workmatch.R;
import cl.ponceleiva.workmatch.activities.home.MainActivity;
import cl.ponceleiva.workmatch.utils.FirebaseUtilsKt;
import cl.ponceleiva.workmatch.utils.UtilitiesKt;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;

import java.util.HashMap;

import static cl.ponceleiva.workmatch.utils.Constants.GOOGLE_SIGN_IN;

public class ChooseRegisterActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private Intent intentActivity;

    private Button btnRegisterGoogle;
    private Button btnRegisterEmail;

    //Google Configure Sign in
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("506073534138-fl48bjumhenjrejojier08acpn1rccr6.apps.googleusercontent.com")
            .requestEmail()
            .build();

    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_register);

        UtilitiesKt.changeColorInitialsViews(getApplicationContext(), getWindow());

        googleSignInClient = GoogleSignIn.getClient(this, gso);
        firebaseAuth = FirebaseAuth.getInstance();

        //Se valida si usuario ya esta logeado
//        authStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                if (user != null) {
//                    Intent intent = new Intent(ChooseRegisterActivity.this, UserTypeActivity.class);
//                    intent.putExtra("userUid", user.getUid());
//                    intent.putExtra("userEmail", user.getEmail());
//                    startActivity(intent);
//                    finish();
//                    return;
//                }
//            }
//        };

        btnRegisterEmail = findViewById(R.id.btn_register_email);
        btnRegisterEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentActivity = new Intent(ChooseRegisterActivity.this, LoginActivity.class);
                startActivity(intentActivity);
            }
        });

        btnRegisterGoogle = findViewById(R.id.btn_register_google);

        btnRegisterGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
//        firebaseAuth.addAuthStateListener(authStateListener); //Ver si esto se puede cambiar de lugar
    }

    @Override
    protected void onStop() {
        super.onStop();
//        firebaseAuth.addAuthStateListener(authStateListener);
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                UtilitiesKt.logE(GOOGLE_SIGN_IN, "Google sign in  failed. Details: " + e);
            }
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount account) {
        UtilitiesKt.logD(GOOGLE_SIGN_IN, "firebaseAuthWhitGoogle: " + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    UtilitiesKt.logD(GOOGLE_SIGN_IN, "Success");

                    HashMap<String, Object> userData = new HashMap<>();
                    userData.put("email", account.getEmail());
                    userData.put("name", account.getDisplayName());
                    userData.put("profileImageUrl", account.getPhotoUrl().toString());
                    userData.put("typeUser", "No definido");

                    FirebaseUtilsKt.createUser(userData, firebaseAuth.getUid());
                } else {
                    UtilitiesKt.logE(GOOGLE_SIGN_IN, "Failed " + task.getException());
                    UtilitiesKt.toastMessage(getApplicationContext(), "Authentication failed");
                }
            }
        });
    }
}
