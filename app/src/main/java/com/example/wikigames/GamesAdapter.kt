package com.example.wikigames
import android.widget.ArrayAdapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView


class GamesAdapter(
    var aContext : Context,
    var ressource : Int,
    var values : ArrayList<Game>
) : ArrayAdapter<Game>(aContext, ressource, values) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val post = values[position]
        val itemView = LayoutInflater.from(aContext).inflate(ressource, parent, false)
        val titleg = itemView.findViewById<TextView>(R.id.titleg)
        val imageg = itemView.findViewById<ImageView>(R.id.imageg)
        titleg.text = post.title
        imageg.setImageResource(post.image)

        return itemView
    }
}