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
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.initialize

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        FirebaseApp.initializeApp(this)


        val editText = findViewById<EditText>(R.id.textView7)
        firebaseAuth = FirebaseAuth.getInstance()

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
}
