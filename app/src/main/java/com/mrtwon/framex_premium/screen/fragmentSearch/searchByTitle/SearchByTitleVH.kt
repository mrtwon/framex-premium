package com.mrtwon.framex_premium.screen.fragmentSearch.searchByTitle

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mrtwon.framex_premium.R
import com.mrtwon.framex_premium.domain.entity.ContentItemPage
import com.mrtwon.framex_premium.domain.entity.enum.ContentEnum
import com.mrtwon.framex_premium.screen.fragmentSearch.SearchCallback
import kotlinx.android.synthetic.main.one_element_search.view.*

class SearchByTitleVH(private val view: View, private val callback: SearchCallback): RecyclerView.ViewHolder(view) {
    private val title: TextView = view.findViewById(R.id.title)
    private val charCategory: TextView = view.findViewById(R.id.char_category)

    fun bind(content: ContentItemPage){
        Log.i("self-test-search","bind data ${content.content.ruTitle}")
        var title = "\uD83C\uDFAC"
        title += content.content.ruTitle ?: "Нет данных"
        content.content.year?.let { title += " ($it)" }
        this.title.text = title
        charCategory.text = when(content.content.contentType){
            ContentEnum.Serial -> "С"
            ContentEnum.Movie -> "Ф"
            ContentEnum.Undefined -> "-"
        }
        view.linear_layout.setOnClickListener{
            callback.onOpen(content = content.content)
        }
    }
}