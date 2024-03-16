package com.example.wikigames

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Modification_profile : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.modification_profile)

        db = FirebaseFirestore.getInstance()



    }
    fun onModify(view: View) {
        val usernameEditText = findViewById<EditText>(R.id.text_username)
        val bioEditText = findViewById<EditText>(R.id.text_bio)

        val username = usernameEditText.text.toString()
        val bio = bioEditText.text.toString()

        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val userId = currentUser.uid

            val user = hashMapOf(
                "username" to username,
                "biography" to bio
            )

            db.collection("user").document(userId)
                .update(user as Map<String, Any>)
                .addOnSuccessListener {
                    Log.d("Modification_profile", "User data successfully written!")
                }
                .addOnFailureListener { e ->
                    Log.w("Modification_profile", "Error writing user data", e)
                }

            startActivity(Intent(this, Profile::class.java))
            this@Modification_profile.overridePendingTransition(
                R.anim.animate_slide_left_enter,
                R.anim.animate_slide_left_exit
            )


        }
    }
    fun onBack(view: View) {
        startActivity(Intent(this, Profile::class.java))
        this@Modification_profile.overridePendingTransition(
            R.anim.animate_slide_left_enter,
            R.anim.animate_slide_left_exit
        )
    }
}