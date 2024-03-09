package com.example.wikigames

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.ListView


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
            val intent = Intent(this, GameInfo::class.java)
            intent.putExtra("game_title", clickedPost.title)
            startActivity(intent)
        }

        findViewById<LinearLayout>(R.id.profile_navbar).setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }


    }

}

