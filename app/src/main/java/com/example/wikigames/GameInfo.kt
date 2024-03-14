package com.example.wikigames

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage


class GameInfo : AppCompatActivity() {
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_info)


        val id = intent.getStringExtra("game_id")
        val gameTitle = findViewById<TextView>(R.id.titleGame)
        val gameGenres = findViewById<TextView>(R.id.genres)
        val gameDate = findViewById<TextView>(R.id.releaseDate)
        val gameTeams = findViewById<TextView>(R.id.entreprise)
        val gameBio = findViewById<TextView>(R.id.biotxt)
        val imageGame = findViewById<ImageView>(R.id.imageGame)


        val docRef = db.collection("games").document(id.toString())
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val title = document.getString("title")
                    val genres = document.get("genres") as? List<String>
                    val releaseDate = document.getString("release date")
                    val teams = document.get("team") as? List<String>
                    val bio = document.getString("summary")

                    gameTitle.text = title
                    gameGenres.text = genres?.joinToString(" - ") // Convertir la liste en une seule chaîne de caractères
                    gameDate.text = "Release Date : " + releaseDate
                    gameTeams.text = "Teams : " + teams?.joinToString(", ") // Convertir la liste en une seule chaîne de caractères
                    gameBio.text = bio

                    val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(document.getString("image").toString())
                    val imageRef = storageRef.root.child(document.getString("image").toString())

                    storageRef.downloadUrl.addOnSuccessListener{ uri ->
                        Glide.with(this)
                            .load(uri)
                            .into(imageGame)
                    }
                }
            }

        //val heartImage = findViewById<ImageView>(R.id.heart)
        //var isHeartFilled = post.isFavorite // Variable pour suivre l'état actuel du cœur


        val ids = intent.getStringExtra("game_id")
        //val currentUser = FirebaseAuth.getInstance().currentUser!!.uid

        val heartImage = findViewById<ImageView>(R.id.heart)
        val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
        val docuRef = db.collection("user").document(currentUser)

        // Vérifiez si le jeu est déjà dans les favoris
        docuRef.get().addOnSuccessListener { document ->
            val favorites = document.get("favori") as? List<String>
            var isHeartFilled = favorites?.contains(id) ?: false

            // Mettre à jour l'image du cœur en fonction de isHeartFilled
            if (isHeartFilled) {
                heartImage.setImageResource(R.drawable.heart_filled)
            } else {
                heartImage.setImageResource(R.drawable.heart)
            }

            heartImage.setOnClickListener {
                // Changer l'image du cœur en fonction de son état actuel
                if (isHeartFilled) {
                    heartImage.setImageResource(R.drawable.heart) // Si le cœur est rempli, changez-le pour le cœur vide
                    // Supprimer l'ID du jeu de la liste des favoris de l'utilisateur
                    docuRef.update("favori", FieldValue.arrayRemove(id))
                } else {
                    heartImage.setImageResource(R.drawable.heart_filled) // Sinon, changez-le pour le cœur rempli
                    // Ajouter l'ID du jeu à la liste des favoris de l'utilisateur
                    docuRef.update("favori", FieldValue.arrayUnion(id))
                }

                isHeartFilled = !isHeartFilled // Inversez l'état du cœur pour le prochain clic
            }
        }



    }

    fun onBackArrowPressed(view: View){
        finish()
    }

}