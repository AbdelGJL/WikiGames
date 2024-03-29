package com.example.wikigames

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.ListView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage


class MenuActivity : AppCompatActivity() {
    private var db = Firebase.firestore
    private var storage = Firebase.storage
    private lateinit var gamesArray: ArrayList<Game>
    private lateinit var listGames: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu)

        listGames = findViewById<ListView>(R.id.gamesList)
        gamesArray = arrayListOf()

        findViewById<LinearLayout>(R.id.profile_navbar).setOnClickListener {
            startActivity(Intent(this, Profile::class.java))
            this@MenuActivity.overridePendingTransition(
                R.anim.animate_slide_left_enter,
                R.anim.animate_slide_left_exit
            )
        }

        findViewById<LinearLayout>(R.id.favorite_navbar).setOnClickListener {
            startActivity(Intent(this, FavoriteActivity::class.java))
            this@MenuActivity.overridePendingTransition(
                R.anim.animate_slide_in_left,
                R.anim.animate_slide_out_right
            )
        }
    }

    override fun onResume() {
        super.onResume()
        updateListView()
    }

    private fun updateListView() {
        gamesArray.clear() // Clear the old data

        // Fetch the data from Firebase and update the ListView
        val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
        val docRef = db.collection("user").document(currentUser)

        docRef.get().addOnSuccessListener { document ->
            val favorites = document.get("favori") as? List<String>

            db.collection("games").get()
                .addOnSuccessListener {
                    if(!it.isEmpty){
                        for(gameDocument in it.documents){
                            val gameId = gameDocument.id
                            val title = gameDocument.getString("title") ?: ""
                            val image = gameDocument.getString("image") ?: ""
                            val isFavorite = favorites?.contains(gameId) ?: false
                            val game = Game(gameId, title, image, isFavorite)
                            gamesArray.add(game)
                        }
                        val adapter = GamesAdapter(this, R.layout.item_games, gamesArray)
                        listGames.adapter = adapter
                    }
                }
        }
    }


}

