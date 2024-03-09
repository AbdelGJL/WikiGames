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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage


class MainActivity : AppCompatActivity() {
    private var db = Firebase.firestore
    private var storage = Firebase.storage
    private lateinit var gamesArray: ArrayList<Game>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu)

        val listGames = findViewById<ListView>(R.id.gamesList)

        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        gamesArray = arrayListOf()

        // Récupérer les données depuis Cloud Firestore
        db.collection("games").get()
            .addOnSuccessListener {
                if(!it.isEmpty){
                    for(data in it.documents){
                        val title = data.getString("title") ?: ""
                        val image = data.getString("image") ?: ""
                        val game = Game(title, image)
                        if (game!=null){
                            gamesArray.add(game)
                        }
                    }
                    val adapter = GamesAdapter(this, R.layout.item_games, gamesArray)
                    listGames.adapter = adapter
                }


                /*listGames.setOnItemClickListener { _, _, position, _ ->
                    val clickedPost = gamesArray[position]
                    val intent = Intent(this, GameInfo::class.java)
                    intent.putExtra("game_title", clickedPost.title)
                    intent.putExtra("game_image_url", clickedPost.image) // Passer l'URL de l'image à l'activité suivante
                    startActivity(intent)
                }*/
            }





        /*val gamesArray = arrayListOf(
            Game("Genshin Impact", R.drawable.genshin),
            Game("Rocket League", R.drawable.rl),
            Game("Apex Legends", R.drawable.apex),
            Game("Apex Legends", R.drawable.apex),
            Game("Apex Legends", R.drawable.apex),
            Game("Apex Legends", R.drawable.apex),
            Game("Apex Legends", R.drawable.apex),
            Game("Apex Legends", R.drawable.apex),
            Game("Apex Legends", R.drawable.apex),
            Game("Apex Legends", R.drawable.apex),
            Game("Apex Legends", R.drawable.apex),
            Game("Apex Legends", R.drawable.apex),
            Game("Apex Legends", R.drawable.apex),
            Game("Apex Legends", R.drawable.apex)
        )
        val adapter = GamesAdapter(this, R.layout.item_games, gamesArray)
        listGames.adapter = adapter

        listGames.setOnItemClickListener { adpaterView, view, position, id ->
            val clickedPost = gamesArray[position]
            val intent = Intent(this, GameInfo::class.java)
            intent.putExtra("game_title", clickedPost.title)
            startActivity(intent)
        }*/

        findViewById<LinearLayout>(R.id.profile_navbar).setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }


    }

}

