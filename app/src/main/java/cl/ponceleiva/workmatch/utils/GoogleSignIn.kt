package cl.ponceleiva.workmatch.utils

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import android.content.Context

lateinit var mGoogleSignInClient: GoogleSignInClient
lateinit var mGoogleSignInOptions: GoogleSignInOptions

private fun configureGoogleSignIn(context: Context) {
    mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("506073534138-fl48bjumhenjrejojier08acpn1rccr6.apps.googleusercontent.com")
            .requestEmail()
            .build()

    mGoogleSignInClient = GoogleSignIn.getClient(context, mGoogleSignInOptions)
}

private fun setUpUI() {
}

