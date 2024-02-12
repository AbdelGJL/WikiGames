package com.example.wikigames

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu)

        val listGames = findViewById<ListView>(R.id.gamesList)
        val GamesArray = arrayListOf(
            Game("Genshin Impact", R.drawable.genshin),
            Game("Rocket League", R.drawable.rl),
            Game("Apex Legends", R.drawable.apex),
            Game("Apex Legends", R.drawable.apex),
            Game("Apex Legends", R.drawable.apex),
            Game("Apex Legends", R.drawable.apex),
            Game("Apex Legends", R.drawable.apex)
        )
        val adapter = GamesAdapter(this, R.layout.item_games, GamesArray)
        listGames.adapter = adapter
    }
}