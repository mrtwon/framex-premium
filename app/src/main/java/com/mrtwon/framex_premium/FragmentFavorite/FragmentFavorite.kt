package com.mrtwon.framex_premium.FragmentFavorite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mrtwon.framex_premium.MainActivity
import com.mrtwon.framex_premium.R
import com.mrtwon.framex_premium.room.Content
import com.mrtwon.framex_premium.room.Favorite
import com.squareup.picasso.Picasso

class FragmentFavorite: Fragment() {
    val vm: FavoriteViewModel by lazy { ViewModelProvider(this).get(FavoriteViewModel::class.java) }
    lateinit var recycler_view: RecyclerView
    lateinit var tv_not_found: TextView
    val favoriteList = arrayListOf<Favorite>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.i("self-favorite","onCreateView favorite")
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)
        recycler_view = view.findViewById(R.id.recycler_view)
        tv_not_found = view.findViewById(R.id.textView_favorite_not_found)
        recycler_view.layoutManager = GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
        observerFavorite()
        recycler_view.adapter = Adapter(favoriteList)
        return view
    }

    override fun onDestroyView() {
        Log.i("self-favorite","onDestroyView() favorite")
        super.onDestroyView()
    }

    override fun onStart() {
        //(activity as MainActivity).reselectedNavigationPosition()
        log("onStart()")
        super.onStart()
    }


    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        log("onViewStateRestored()")
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        log("onSaveInstanceState()")
        super.onSaveInstanceState(outState)
    }
    override fun onResume() {
        log("onResume()")
        super.onResume()
    }

    override fun onStop() {
        log("onStop()")
        super.onStop()
    }

    override fun onDestroy() {
        log("onDestroy()")
        super.onDestroy()
    }


    fun observerFavorite(){
        vm.favoriteLiveData.observe(viewLifecycleOwner, Observer {
            if(it.isEmpty()){
                recycler_view.visibility = View.GONE
                tv_not_found.visibility = View.VISIBLE
            }else{
                favoriteList.clear()
                favoriteList.addAll(it.reversed())
                recycler_view.adapter?.notifyDataSetChanged()
                tv_not_found.visibility = View.GONE
                recycler_view.visibility = View.VISIBLE
            }
        })
    }

    inner class ViewHolder(val itemView: View): RecyclerView.ViewHolder(itemView){

        val title: TextView = itemView.findViewById(R.id.title)
        val poster: ImageView = itemView.findViewById(R.id.poster)
        val contentType: TextView = itemView.findViewById(R.id.contentType)
        val btn_delete: Button = itemView.findViewById(R.id.btn_delete_favorite)

        fun bind(favorite: Favorite){

            btn_delete.setOnClickListener{
                    vm.removeFavorite(favorite)
            }
            poster.setOnClickListener{
                val bundle = Bundle().apply { putInt("id", favorite.id_content) }
                when(favorite.content_type){
                    /*"movie" -> {
                        (requireActivity() as MainActivity).navController.navigate(R.id.action_fragmentFavorite_to_fragmentSearchDescription, bundle)
                    }
                    "tv_series" -> {
                        (requireActivity() as MainActivity).navController.navigate(R.id.action_fragmentFavorite_to_fragmentSearchDescription, bundle)
                    }*/
                    "movie" -> {
                        (requireActivity() as MainActivity).navController.navigate(R.id.action_fragmentFavorite_to_fragmentAboutMovie, bundle)
                    }
                    "tv_series" -> {
                        (requireActivity() as MainActivity).navController.navigate(R.id.action_fragmentFavorite_to_fragmentAboutSerial, bundle)
                    }
                }
            }
           title.text = favorite.ru_title
           contentType.text = when(favorite.content_type){
               "movie" -> "Фильм"
               "tv_series" -> "Сериал"
               else -> "Unknown"
           }
            Picasso.get()
                .load(favorite.poster)
                .into(poster)
        }
    }
    inner class Adapter(val listContent: List<Favorite>) : RecyclerView.Adapter<ViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(layoutInflater.inflate(R.layout.one_favorite_element, parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(listContent[position])
        }

        override fun getItemCount(): Int {
            return listContent.size
        }

    }
    fun log(s: String){
        Log.i("self-favorite", s)
    }
}