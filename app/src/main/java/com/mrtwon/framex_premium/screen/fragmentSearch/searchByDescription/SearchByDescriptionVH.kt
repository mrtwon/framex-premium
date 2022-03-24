package com.mrtwon.framex_premium.screen.fragmentSearch.searchByDescription

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mrtwon.framex_premium.R
import com.mrtwon.framex_premium.domain.entity.ContentItemPage
import com.mrtwon.framex_premium.domain.entity.enum.ContentEnum
import com.mrtwon.framex_premium.screen.fragmentSearch.SearchCallback
import java.lang.StringBuilder

class SearchByDescriptionVH(private val view: View, private val callback: SearchCallback)
    : RecyclerView.ViewHolder(view){
    private val title: TextView = view.findViewById(R.id.title)
    private val description: TextView = view.findViewById(R.id.description)
    private val charCategory: TextView = view.findViewById(R.id.char_category)
    fun bind(content: ContentItemPage){
       var title = "\uD83C\uDFAC"
        title += content.content.ruTitle ?: "Нет данных"
        content.content.year?.let {
            title += " ($it)"
        }
        this.title.text = title
        description.text = content.content.description ?: "Нет данных"
        charCategory.text = when(content.content.contentType){
            ContentEnum.Movie -> "Ф"
            ContentEnum.Serial ->  "С"
            ContentEnum.Undefined -> "-"
        }
        view.setOnClickListener{
            callback.onOpen(content = content.content)
        }
    }
}