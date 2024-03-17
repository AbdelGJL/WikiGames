/*package com.example.wikigames

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)


        val editText = findViewById<EditText>(R.id.textView7)

        editText.setOnClickListener {
            // Lorsque l'utilisateur clique sur le ConstraintLayout, le texte est vidé
            editText.text.clear()
        }
    }
    // fonction pour aller vers la page d'une plante
    fun onClick(view: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
    fun onContinue(view: View) {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
    }
}*/

package com.example.wikigames

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import android.content.SharedPreferences
import android.widget.CheckBox


class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    // Déclaration d'une variable SharedPreferences afin de stocker les identifiants de connexions à la demande de l'utilisateur
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        FirebaseApp.initializeApp(this)
        firebaseAuth = FirebaseAuth.getInstance()

        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE)
        val checkBoxRememberMe = findViewById<CheckBox>(R.id.checkBox)
        val emailEditText = findViewById<EditText>(R.id.textView7)
        val passwordEditText = findViewById<EditText>(R.id.textView8)


        checkBoxRememberMe.setOnCheckedChangeListener { _, isChecked ->
            // Si bouton coché, récupération des informations de connexion
            if (isChecked) {
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString("email", emailEditText.text.toString())
                editor.putString("password", passwordEditText.text.toString())
                editor.putBoolean("rememberMe", true)
                editor.apply()
            } else {
                // Si bouton pas coché, suppression des informations de connexion enregistrées
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.remove("email")
                editor.remove("password")
                editor.remove("rememberMe")
                editor.apply()
            }
        }

        // Récupération des informations de connexion enregistrées lors du lancement de l'application
        val rememberMe = sharedPreferences.getBoolean("rememberMe", false)
        if (rememberMe) {
            val savedEmail = sharedPreferences.getString("email", "")
            val savedPassword = sharedPreferences.getString("password", "")
            emailEditText.setText(savedEmail)
            passwordEditText.setText(savedPassword)
            checkBoxRememberMe.isChecked = true
        }


    }

    // fonction pour aller vers la page register pour créer un compte
    fun onClick(view: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        this@MainActivity.overridePendingTransition(
            R.anim.animate_slide_left_enter,
            R.anim.animate_slide_left_exit
        )
    }

    //function mise sur le btn Login afin d'entrer dans l'application
    fun onContinue(view: View) {
        val emailEditText = findViewById<EditText>(R.id.textView7)
        val passwordEditText = findViewById<EditText>(R.id.textView8)

        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // L'authentification a réussi
                        val intent = Intent(this, MenuActivity::class.java)
                        Toast.makeText(
                            this,
                            "Authentication succesful !!",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(intent)
                    } else {
                        // L'authentification a échoué
                        Toast.makeText(
                            this,
                            "Authentication failed: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } else {
            // Les champs sont vides
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
        }
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
