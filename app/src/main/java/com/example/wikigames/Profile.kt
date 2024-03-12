package com.example.wikigames

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage


class Profile : AppCompatActivity() {
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val currentUser = FirebaseAuth.getInstance().currentUser!!.uid

        val pseudo = findViewById<TextView>(R.id.pseudo)
        val biotxt = findViewById<TextView>(R.id.biotxt)
        val profile = findViewById<ShapeableImageView>(R.id.profile)

        val docRef = db.collection("user").document(currentUser)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val username = document.getString("username")
                    val bio = document.getString("biography")

                    pseudo.text = username
                    biotxt.text = bio

                    val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(document.getString("profile picture").toString())
                    val imageRef = storageRef.root.child(document.getString("profile picture").toString())


                    if (storageRef.toString().isNotEmpty()) {
                        storageRef.downloadUrl.addOnSuccessListener { uri ->
                            Glide.with(this)
                                .load(uri)
                                .into(profile)
                        }
                    }

                }
            }




        findViewById<LinearLayout>(R.id.home_navbar).setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }

        val imageGame = findViewById<ImageView>(R.id.profile)
        imageGame.setImageResource(R.drawable.profile_icon)

    }



}

