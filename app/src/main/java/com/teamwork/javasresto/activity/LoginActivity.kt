package com.teamwork.javasresto.activity

import Config
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.teamwork.javasresto.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private  lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences
    var mGoogleSignInClient: GoogleSignInClient? = null
    var RC_SIGN_IN = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.signInButton.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            Log.d("RBA", "onShared: " + account.id.toString() +"\n" + account.displayName.toString() +"\n" + account.email.toString() +"\n" )

            // Signed in successfully, show authenticated UI.
            if (account.photoUrl == null) {
                sharedPreferences = getSharedPreferences(Config::SHARED_PREF_NAME.toString(), Context.MODE_PRIVATE)
                sharedPreferences.edit()
                    .putString(Config::LOGIN_TOKEN_SHARED_PREF.toString(), account.id.toString())
                    .putString(Config::LOGIN_NAME_SHARED_PREF.toString(), account.displayName.toString())
                    .putString(Config::LOGIN_EMAIL_SHARED_PREF.toString(), account.email.toString())
                    .apply()
            }else{
                sharedPreferences = getSharedPreferences(Config::SHARED_PREF_NAME.toString(), Context.MODE_PRIVATE)
                sharedPreferences.edit()
                    .putString(Config::LOGIN_TOKEN_SHARED_PREF.toString(), account.id.toString())
                    .putString(Config::LOGIN_NAME_SHARED_PREF.toString(), account.displayName.toString())
                    .putString(Config::LOGIN_EMAIL_SHARED_PREF.toString(), account.email.toString())
                    .putString(Config::LOGIN_PICT_SHARED_PREF.toString(), account.photoUrl.toString())
                    .apply()
            }

            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("RBA", "signInResult:failed code=" + e.statusCode)
        }
    }

}