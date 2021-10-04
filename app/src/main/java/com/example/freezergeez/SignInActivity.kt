package com.example.freezergeez

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_sign_in.*


class SignInActivity : AppCompatActivity() {

    companion object {
        const val RC_SIGN_IN = 120
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("your ID")
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        //Firebase Auth Instance
        auth = FirebaseAuth.getInstance()

        sign_in_button.setOnClickListener {
            signIn()
        }

    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if(task.isSuccessful){
                try{
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("SignInAcitvity", "firedbaseAuthWithGoogle" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                }
                catch( e: ApiException) {
                    Log.w("SignInActivity", "Google sign in failed", e)
                }
            } else{
                Log.w("SignInActivity",exception.toString())
            }
        }
    }
    //Authenticate with Firebase and Google
    private fun firebaseAuthWithGoogle(idToken:String){
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this){task ->
                if(task.isSuccessful) {
                    Log.d("SignInActivity", "signInWithCredential:sucess")
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else{
                    //If sign in fails display message
                    Log.d("SignInActivity", "signInWithCredential:failure")
                }
            }
    }
}

