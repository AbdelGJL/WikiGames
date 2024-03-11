package com.example.wikigames

import android.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

class GameInfo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_info)

        val titre = intent.getStringExtra("game_title")
        val gameTitle = findViewById<TextView>(R.id.titleGame)
        gameTitle.text = titre

        val backArrow = findViewById<ImageButton>(R.id.back_arrow)
        backArrow.setOnClickListener(null)

        val imageGame = findViewById<ImageView>(R.id.imageGame)
        imageGame.setImageResource(R.drawable.apex)
    }

    fun onBackArrowClicked(view: View) {
        finish()
    }


}