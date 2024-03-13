package com.example.wikigames
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage


class GamesAdapter(
    var aContext : Context,
    var ressource : Int,
    var values : ArrayList<Game>,
) : ArrayAdapter<Game>(aContext, ressource, values) {

    private var db = Firebase.firestore
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val post = values[position]
        val itemView = LayoutInflater.from(aContext).inflate(ressource, parent, false)
        var titleg = itemView.findViewById<TextView>(R.id.titleg)
        var imageg = itemView.findViewById<ImageView>(R.id.imageg)
        titleg.text = post.title
        //imageg.setImageResource(post.image)

        Log.d("MainActivity", "Image name: ${post.image}")


        val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(post.image)
        val imageRef = storageRef.root.child(post.image)

        storageRef.downloadUrl.addOnSuccessListener{ uri ->
            Glide.with(context)
                .load(uri)
                .into(imageg)
        }


        itemView.setOnClickListener {
            // Lorsque l'utilisateur clique sur un élément de la liste
            val intent = Intent(aContext, GameInfo::class.java)
            intent.putExtra("game_id", post.id)
            aContext.startActivity(intent)
        }

        val heartImage = itemView.findViewById<ImageView>(R.id.favorite)
        if (post.isFavorite) {
            heartImage.setImageResource(R.drawable.heart_filled)
        } else {
            heartImage.setImageResource(R.drawable.heart)
        }
        var isHeartFilled = post.isFavorite// Variable pour suivre l'état actuel du cœur

        val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
        val docRef = db.collection("user").document(currentUser)


        heartImage.setOnClickListener {
            // Changer l'image du cœur en fonction de son état actuel
            if (isHeartFilled) {
                heartImage.setImageResource(R.drawable.heart) // Si le cœur est rempli, changez-le pour le cœur vide
                // Supprimer l'ID du jeu de la liste des favoris de l'utilisateur
                docRef.update("favori", FieldValue.arrayRemove(post.id))
            } else {
                heartImage.setImageResource(R.drawable.heart_filled) // Sinon, changez-le pour le cœur rempli
                // Ajouter l'ID du jeu à la liste des favoris de l'utilisateur
                docRef.update("favori", FieldValue.arrayUnion(post.id))
            }

            isHeartFilled = !isHeartFilled // Inversez l'état du cœur pour le prochain clic
        }

        return itemView
    }

}