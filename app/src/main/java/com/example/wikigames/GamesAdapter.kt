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
import com.google.firebase.storage.FirebaseStorage


class GamesAdapter(
    var aContext : Context,
    var ressource : Int,
    var values : ArrayList<Game>,
) : ArrayAdapter<Game>(aContext, ressource, values) {


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
        var isHeartFilled = false // Variable pour suivre l'état actuel du cœur

        heartImage.setOnClickListener {
            // Changer l'image du cœur en fonction de son état actuel
            if (isHeartFilled) {
                heartImage.setImageResource(R.drawable.heart) // Si le cœur est rempli, changez-le pour le cœur vide
            } else {
                heartImage.setImageResource(R.drawable.heart_filled) // Sinon, changez-le pour le cœur rempli
            }

            isHeartFilled = !isHeartFilled // Inversez l'état du cœur pour le prochain clic
        }

        return itemView
    }

}