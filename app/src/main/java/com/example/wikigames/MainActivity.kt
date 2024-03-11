package com.example.wikigames

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)


        val editText = findViewById<EditText>(R.id.textView7)

        editText.setOnClickListener {
            // Lorsque l'utilisateur clique sur le ConstraintLayout, le texte est vidé
            editText.text.clear()
        }
    }
    // fonction pour aller vers la page d'une plante
    fun onClick(view: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}