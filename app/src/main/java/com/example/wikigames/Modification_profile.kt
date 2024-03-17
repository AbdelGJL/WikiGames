package com.example.wikigames

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class Modification_profile : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var profileImageView: ImageView
    private var profileImageUri: Uri? = null


    companion object {
        private const val PICK_IMAGE_REQUEST = 1
        private const val READ_EXTERNAL_STORAGE_REQUEST = 2
        private const val READ_EXTERNAL_STORAGE = 3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.modification_profile)
        db = FirebaseFirestore.getInstance()

        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        profileImageView = findViewById(R.id.profile_edit)
        val chooseImageButton = findViewById<ImageButton>(R.id.penedit)
        chooseImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        if (currentUser != null) {
            val userId = currentUser.uid

            db.collection("user").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        Log.d("Modification_profile", "DocumentSnapshot data: ${document.data}")
                        val username = document.getString("username")
                        val bio = document.getString("biography")
                        //val profileImageUrl = document.getString("profile picture")
                        val profileImageUrl = FirebaseStorage.getInstance().getReferenceFromUrl(document.getString("profile picture").toString())

                        val usernameEditText = findViewById<EditText>(R.id.text_username)
                        val bioEditText = findViewById<EditText>(R.id.text_bio)

                        usernameEditText.setText(username)
                        bioEditText.setText(bio)

                        profileImageUrl.downloadUrl.addOnSuccessListener { uri ->
                            Glide.with(this)
                                .load(uri)
                                .into(profileImageView)
                        }
                        profileImageUri = Uri.parse(profileImageUrl.toString())

                        // Load the profile image
                        /*if (profileImageUrl != null) {
                            Glide.with(this)
                                .load(profileImageUrl)
                                .into(profileImageView)
                            profileImageUri = Uri.parse(profileImageUrl)
                        }*/
                    } else {
                        Log.d("Modification_profile", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("Modification_profile", "get failed with ", exception)
                }
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            profileImageUri = data.data
            profileImageView.setImageURI(profileImageUri)
        }
    }
    /*
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
    }*/
    /* version 2
    fun onModify(view: View) {
        val usernameEditText = findViewById<EditText>(R.id.text_username)
        val bioEditText = findViewById<EditText>(R.id.text_bio)

        val username = usernameEditText.text.toString()
        val bio = bioEditText.text.toString()

        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val userId = currentUser.uid

            // Upload the profile image to Firebase Storage and update the profile image URL in the user document
            val storageRef = FirebaseStorage.getInstance().reference
            val profileImageRef = storageRef.child("profile_images/${UUID.randomUUID()}")
            profileImageRef.putFile(profileImageUri!!)
                .addOnSuccessListener {
                    profileImageRef.downloadUrl.addOnSuccessListener { uri ->
                        val user = hashMapOf(
                            "username" to username,
                            "biography" to bio,
                            "profileImageUrl" to uri.toString()
                        )

                        // Update the user document with the new username, biography, and profile image URL
                        db.collection("user").document(userId)
                            .update(user as Map<String, Any>)
                            .addOnSuccessListener {
                                Log.d("Modification_profile", "User data successfully written!")
                                startActivity(Intent(this, Profile::class.java))
                                this@Modification_profile.overridePendingTransition(
                                    R.anim.animate_slide_left_enter,
                                    R.anim.animate_slide_left_exit
                                )
                            }
                            .addOnFailureListener { e ->
                                Log.w("Modification_profile", "Error writing user data", e)
                            }
                    }
                }
                .addOnFailureListener { e ->
                    Log.w("Modification_profile", "Error uploading profile image", e)
                }
        }
    }*/

    fun onModify(view: View) {
        val usernameEditText = findViewById<EditText>(R.id.text_username)
        val bioEditText = findViewById<EditText>(R.id.text_bio)

        val username = usernameEditText.text.toString()
        val bio = bioEditText.text.toString()

        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val userId = currentUser.uid

            // Check if a new image has been chosen
            if (profileImageUri != null && profileImageUri!!.scheme == "content") {
                // A new image has been chosen, upload it to Firebase Storage
                val storageRef = FirebaseStorage.getInstance().reference
                val profileImageRef = storageRef.child("profile_images/${UUID.randomUUID()}")
                profileImageRef.putFile(profileImageUri!!)
                    .addOnSuccessListener {
                        profileImageRef.downloadUrl.addOnSuccessListener { uri ->
                            val user = hashMapOf(
                                "username" to username,
                                "biography" to bio,
                                "profile picture" to uri.toString()
                            )

                            // Update the user document with the new username, biography, and profile image URL
                            db.collection("user").document(userId)
                                .update(user as Map<String, Any>)
                                .addOnSuccessListener {
                                    Log.d("Modification_profile", "User data successfully written!")
                                    Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this, Profile::class.java))
                                    this@Modification_profile.overridePendingTransition(
                                        R.anim.animate_slide_down_enter,
                                        R.anim.animate_slide_down_exit
                                    )
                                }
                                .addOnFailureListener { e ->
                                    Log.w("Modification_profile", "Error writing user data", e)
                                    Toast.makeText(this, "Error updating user data", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.w("Modification_profile", "Error uploading profile image", e)
                        Toast.makeText(this, "Error uploading profile image", Toast.LENGTH_SHORT).show()
                    }
            } else {
                // No new image has been chosen, use the existing profile image URL
                val user = hashMapOf(
                    "username" to username,
                    "biography" to bio,
                    "profile picture" to "gs://wikigames-be826.appspot.com/profile_images/profilepicture.png"
                )

                // Update the user document with the new username and biography
                db.collection("user").document(userId)
                    .update(user as Map<String, Any>)
                    .addOnSuccessListener {
                        Log.d("Modification_profile", "User data successfully written!")
                        startActivity(Intent(this, Profile::class.java))
                        this@Modification_profile.overridePendingTransition(
                            R.anim.animate_slide_down_enter,
                            R.anim.animate_slide_down_exit
                        )
                    }
                    .addOnFailureListener { e ->
                        Log.w("Modification_profile", "Error writing user data", e)
                        Toast.makeText(this, "Error updating user data", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    fun onBack(view: View) {
        startActivity(Intent(this, Profile::class.java))
        this@Modification_profile.overridePendingTransition(
            R.anim.animate_slide_down_enter,
            R.anim.animate_slide_down_exit
        )
    }


}