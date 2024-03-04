package com.example.wikigames

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu)

        val listGames = findViewById<ListView>(R.id.gamesList)
        val gamesArray = arrayListOf(
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
            Intent(this, Details_Placeholder::class.java).also {
                //it.putExtra("titre", clickedPost.title)
                startActivity(it)
            }
        }
    }
}

