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
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import splitties.alertdialog.appcompat.*
import splitties.views.textColorResource


class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        auth = Firebase.auth
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    fun login(view: View) {
        val email = emailText.text.toString()
        val password = passText.text.toString()
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        if (email.matches(emailPattern.toRegex()) && password != "") {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = auth.currentUser
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

    private fun forgotPassword() {
        val email = emailText.text.toString()
        auth.sendPasswordResetEmail(email).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    baseContext, "Письмо для сброса пароля отправлено",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    baseContext, "Ошибка при сбросе пароля",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    fun doIrreversibleStuffOrCancel(view: View) {
        alertDialog {
            messageResource = R.string.alertForgotPass
            okButton { forgotPassword() }
            cancelButton()
        }.onShow {
            positiveButton.textColorResource = R.color.colorPrimary
        }.show()
    }
    fun toRegistration(view: View) {
        val regIntent = Intent(this, RegisterActivity::class.java)
        startActivity(regIntent)
        super.finish()
    }

    fun loginGoogle(view: View) {
        val signInIntent: Intent = googleSignInClient.signInIntent
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
                    val user = auth.currentUser
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