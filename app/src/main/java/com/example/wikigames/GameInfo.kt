package com.example.wikigames

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


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


        val backArrow = findViewById<ImageButton>(R.id.back_arrow)


    }

    fun onBackArrowClicked(view: View) {
        finish()
    }


}