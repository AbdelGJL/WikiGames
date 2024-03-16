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

        // Initialize your UI elements
        usernameEditText = findViewById(R.id.editTextText)
        emailEditText = findViewById(R.id.textView7)
        passwordEditText = findViewById(R.id.textView8)
    }

    // Function to handle the registration process
    fun onRegister(view: View) {
        val username = usernameEditText.text.toString()
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        val bio = ""
        val favori: MutableList<String>? = null // mutableListOf("élément1", "élément2", "élément3")
        val profile = "gs://wikigames-be826.appspot.com/profile_pictures/profilepicture.png"

        if (email.isNotEmpty() && password.isNotEmpty() && username.isNotEmpty()) {
            // Firebase authentication for user registration
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {



                        //modification debut
                        val userMap= hashMapOf(
                            "username" to username,
                            "biography" to bio,
                            "favori" to favori,
                            "profile picture" to profile
                        )

                        val userId = firebaseAuth.currentUser!!.uid

                        db.collection("user").document(userId).set(userMap)


// modification fin
                        // Registration successful, navigate to the main activity or any other desired activity
                        val intent = Intent(this, MainActivity::class.java)
                        Toast.makeText(
                            this, "Registration succesful !!",
                            Toast.LENGTH_LONG /*on peut modifier le temps d'affichage de ma pop up*/
                        ).show()
                        startActivity(intent)
                        finish()
                    } else {
                        // If registration fails, display a message to the user
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


    // Function to navigate back to the main activity
    fun onReverse(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        this@RegisterActivity.overridePendingTransition(
            R.anim.animate_slide_in_left,
            R.anim.animate_slide_out_right
        )
    }

    fun togglePasswordVisibility(view: View) {
        //val heartImage = findViewById<ImageView>(R.id.heart)
        val editTextPassword = findViewById<EditText>(R.id.textView8)
        val imageViewEye = findViewById<ImageView>(R.id.imageView)

        if (editTextPassword.transformationMethod == PasswordTransformationMethod.getInstance()) {
            // Show password
            editTextPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            imageViewEye.setImageResource(R.drawable.openeye_removebg_preview)
        } else {
            // Hide password
            editTextPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            imageViewEye.setImageResource(R.drawable.closeeye_removebg_preview)
        }

        // Move cursor to the end of the text
        editTextPassword.setSelection(editTextPassword.text.length)
    }
}
