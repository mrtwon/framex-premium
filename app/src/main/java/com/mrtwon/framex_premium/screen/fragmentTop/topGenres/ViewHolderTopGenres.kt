package com.mrtwon.framex_premium.screen.fragmentTop.topGenres

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mrtwon.framex_premium.R
import com.mrtwon.framex_premium.domain.entity.ContentItemPage
import com.mrtwon.framex_premium.screen.fragmentTop.TopOpenCallback
import com.squareup.picasso.Picasso

class ViewHolderTopGenres(private val itemView: View, private val callback: TopOpenCallback, private val picasso: Picasso)
    : RecyclerView.ViewHolder(itemView) {
    private val title: TextView = itemView.findViewById(R.id.title)
    private val ratingKp: TextView = itemView.findViewById(R.id.kp_rating)
    private val ratingImdb: TextView = itemView.findViewById(R.id.imdb_rating)
    private val posterPreview: ImageView = itemView.findViewById(R.id.poster)
    fun bind(content: ContentItemPage){
        title.text = content.content.ruTitle ?: "Нет данных"
        ratingKp.text = content.content.kpRating?.toString() ?: "0.0"
        ratingImdb.text = content.content.imdbRating?.toString() ?: "0.0"
        itemView.setOnClickListener{ callback.onOpen(content.content) }
        picasso
            .load(content.content.posterPreview)
            .into(posterPreview)
    }
}