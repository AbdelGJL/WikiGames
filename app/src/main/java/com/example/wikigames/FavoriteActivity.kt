package com.example.wikigames

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore


class FavoriteActivity : AppCompatActivity(){
    private var db = Firebase.firestore
    private lateinit var gamesArray: ArrayList<Game>
    private lateinit var listGames: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.favorite)

        listGames = findViewById<ListView>(R.id.gamesListF)
        gamesArray = arrayListOf()

        findViewById<LinearLayout>(R.id.profile_navbar).setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
            this@FavoriteActivity.overridePendingTransition(
                R.anim.animate_slide_left_enter,
                R.anim.animate_slide_left_exit
            )
        }

        findViewById<LinearLayout>(R.id.home_navbar).setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            this@FavoriteActivity.overridePendingTransition(
                R.anim.animate_slide_left_enter,
                R.anim.animate_slide_left_exit
            )
        }
    }

    override fun onResume() {
        super.onResume()
        updateListView()
    }

    private fun updateListView() {
        // Fetch the data from Firebase and update the ListView
        val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
        val docRef = db.collection("user").document(currentUser)

        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                gamesArray.clear() // Clear the old data

                val favorites = snapshot.get("favori") as? List<String>

                favorites?.let {
                    for (gameId in it) {
                        db.collection("games").document(gameId).get()
                            .addOnSuccessListener { gameDocument ->
                                val title = gameDocument.getString("title") ?: ""
                                val image = gameDocument.getString("image") ?: ""
                                val game = Game(gameId, title, image, true)
                                gamesArray.add(game)

                                val adapter = GamesAdapter(this, R.layout.item_games, gamesArray)
                                listGames.adapter = adapter
                            }
                    }

                    findViewById<ImageView>(R.id.emptylist).visibility = if (favorites.isEmpty()) View.VISIBLE else View.GONE
                }
            } else {
                Log.d(TAG, "Current data: null")
            }
        }
    }
}