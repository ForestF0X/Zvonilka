package com.eric.zvonilka.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.eric.zvonilka.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.emailText
import kotlinx.android.synthetic.main.activity_login.passText
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity() {

    private lateinit var name: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var user: FirebaseUser
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()
        auth = Firebase.auth
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    fun toLogin(view: View) {
        val loginIntent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent)
        super.finish()
    }
    fun register(view: View) {
        email = emailText.text.toString()
        password = passText.text.toString()
        name = nameText.text.toString()
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        if (email.matches(emailPattern.toRegex()) && password != "") {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        user = auth.currentUser!!
                        val homeIntent = Intent(this, HomeActivity::class.java).putExtra(
                            "userData",
                            user
                        )
                        val profileUpdate = UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .build()
                        user.updateProfile(profileUpdate)
                        user.sendEmailVerification()
                        startActivity(homeIntent)
                        super.finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(
                            baseContext, "Ошибка регистрации",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } else if (email == "" && password == "") {
            Toast.makeText(
                applicationContext, "Введите адрес электронной почты и пароль",
                Toast.LENGTH_SHORT
            ).show()
        } else if (email == "") {
            Toast.makeText(
                applicationContext, "Введите адрес электронной почты",
                Toast.LENGTH_SHORT
            ).show()
        } else if (password == ""){
            Toast.makeText(
                applicationContext, "Введите пароль",
                Toast.LENGTH_SHORT
            ).show()
        } else if (!email.matches(emailPattern.toRegex())){
            Toast.makeText(
                applicationContext, "Неправильный адрес электронной почты",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                applicationContext, "Введите корректные данные",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun registerGoogle(view: View) {val signInIntent: Intent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, 1)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 1) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account?.idToken!!)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    user = auth.currentUser!!
                    val homeIntent = Intent(this, HomeActivity::class.java).putExtra(
                        "userData",
                        user
                    )
                    startActivity(homeIntent)
                    super.finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        baseContext, "Ошибка входа",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}