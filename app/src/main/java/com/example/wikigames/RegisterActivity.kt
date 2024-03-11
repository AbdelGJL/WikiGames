package com.example.wikigames

import android.content.Intent
import android.os.Bundle
import android.view.View

import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)

    }
    //fonction pour retourner à la page d'accueil
    fun onReverse(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}