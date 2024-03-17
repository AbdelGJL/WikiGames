/* package com.example.wikigames

import android.content.Intent
import android.os.Bundle
import android.view.View

import androidx.appcompat.app.AppCompatActivity



class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)

    }
    //fonction pour retourner à la page d'accueil
    fun onReverse(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
    //fonction pour retourner à la page d'accueil à partir du bouton Register
    fun onRegister(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}*/
package com.example.wikigames


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.text.method.PasswordTransformationMethod
import android.text.method.HideReturnsTransformationMethod
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class RegisterActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    private var db= Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)

        firebaseAuth = FirebaseAuth.getInstance()

        usernameEditText = findViewById(R.id.editTextText)
        emailEditText = findViewById(R.id.textView7)
        passwordEditText = findViewById(R.id.textView8)
    }

    // fonction pour la création de compte
    fun onRegister(view: View) {
        val username = usernameEditText.text.toString()
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        val bio = ""
        val favori: MutableList<String>? = null // mutableListOf("élément1", "élément2", "élément3")
        val profile = "gs://wikigames-be826.appspot.com/profile_images/profilepicture.png"

        if (email.isNotEmpty() && password.isNotEmpty() && username.isNotEmpty()) {
            // création de l'utilisateur dans la base de données d'authentification deFirebase
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val userMap= hashMapOf(
                            "username" to username,
                            "biography" to bio,
                            "favori" to favori,
                            "profile picture" to profile
                        )

                        val userId = firebaseAuth.currentUser!!.uid

                        // création du document contenant les variables qui seront modifié dans les autres pages sur Firebase

                        db.collection("user").document(userId).set(userMap)

                        // la création de compte ayant réussi une pop up apparait pour le signaler pendant que l'on retourne sur la page principale pour s'authentifier
                        val intent = Intent(this, MainActivity::class.java)
                        Toast.makeText(
                            this, "Registration succesful !!",
                            Toast.LENGTH_LONG /*on peut modifier le temps d'affichage de ma pop up*/
                        ).show()
                        startActivity(intent)
                        finish()
                    } else {
                        // de même si la création de compte n'a pas fonctionné une pop apparait pour le signaler
                        Toast.makeText(
                            this, "Registration failed: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } else {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }


    // fonction pour retourner sur la page login si on a oublier qu'on avait déjà un compte
    fun onReverse(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        this@RegisterActivity.overridePendingTransition(
            R.anim.animate_slide_in_left,
            R.anim.animate_slide_out_right
        )
    }

    // function pour afficher/cacher le mot de passe
    fun togglePasswordVisibility(view: View) {
        val editTextPassword = findViewById<EditText>(R.id.textView8)
        val imageViewEye = findViewById<ImageView>(R.id.imageView)

        if (editTextPassword.transformationMethod == PasswordTransformationMethod.getInstance()) {
            // montre le mot de passe
            editTextPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            imageViewEye.setImageResource(R.drawable.openeye_removebg_preview)
        } else {
            // cache le mot de passe
            editTextPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            imageViewEye.setImageResource(R.drawable.closeeye_removebg_preview)
        }

        // met le curseur en fin de mot afin de ne pas entraver la saisie
        editTextPassword.setSelection(editTextPassword.text.length)
    }
}
