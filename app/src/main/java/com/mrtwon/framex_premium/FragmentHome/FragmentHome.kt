package com.mrtwon.framex_premium.FragmentHome

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mrtwon.framex_premium.Content.CollectionContentEnum
import com.mrtwon.framex_premium.Content.GenresEnum
import com.mrtwon.framex_premium.Content.ParcelableEnum
import com.mrtwon.framex_premium.MainActivity
import com.mrtwon.framex_premium.MainViewModel
import com.mrtwon.framex_premium.R
import com.mrtwon.framex_premium.room.Content
import com.mrtwon.framex_premium.room.Recent
import com.squareup.picasso.Picasso
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import jp.wasabeef.recyclerview.animators.FadeInAnimator
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import kotlinx.android.synthetic.main.fragment_about_movie.*
import kotlinx.android.synthetic.main.fragment_about_movie.view.*
import kotlinx.android.synthetic.main.fragment_about_movie.view.tool_bar
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.layout_report_fragment.view.*
import kotlinx.android.synthetic.main.layout_welcome.view.*

class FragmentHome: Fragment() {
    val vm: MainViewModel by lazy { ViewModelProvider(requireActivity()).get(MainViewModel::class.java) }
    val controller by lazy { (requireActivity() as MainActivity).navController }
    lateinit var recent_rv: RecyclerView
    lateinit var card_view_recent: CardView
    lateinit var toolbar: Toolbar
    val listRecent = arrayListOf<Recent>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        recent_rv = view.recycler_view_recent
        card_view_recent = view.recent_card_view
        recent_rv.adapter = AdapterRecent(listRecent)
        recent_rv.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        toolbar = view.findViewById(R.id.tool_bar)
        toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.bug_report -> {
                    showBottomSheetDialog()
                    true
                }
                else ->{
                    true
                }
            }
        }

        recent_rv.adapter = ScaleInAnimationAdapter(AlphaInAnimationAdapter(AdapterRecent(listRecent))).apply {
            setDuration(400)
            setFirstOnly(false)
        }
        recent_rv.itemAnimator = FadeInAnimator()

        clickListener(view)
        return view
    }

    override fun onStart() {
        (activity as MainActivity).reselectedNavigationPosition()
        super.onStart()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observerRecent()
        vm.getRecent()
        super.onViewCreated(view, savedInstanceState)
    }
    fun observerRecent(){
        vm.listRecent.observe(viewLifecycleOwner, Observer {
            if(it.size > 0){
                listRecent.clear()
                listRecent.addAll(it)
                recent_rv.adapter?.notifyDataSetChanged()
                card_view_recent.visibility = View.VISIBLE
            }else{
                card_view_recent.visibility = View.GONE
            }
        })
    }

    fun showBottomSheetDialog(){
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(R.layout.layout_report_fragment)
        dialog.show()
    }

    fun clickListener(v: View){
        v.apply {
            findViewById<CardView>(R.id.new_type).setOnClickListener{
                controller.navigate(R.id.action_fragmentHome_to_fragmentTop, Bundle().apply {
                    putParcelable("collection_enum", ParcelableEnum(CollectionContentEnum.NEW))
                    //putInt("img_resource", R.drawable.new_content)
                    putInt("img_resource", CollectionContentEnum.NEW.image)
                })
            }
            findViewById<CardView>(R.id.comedy).setOnClickListener{
                controller.navigate(R.id.action_fragmentHome_to_fragmentTop, Bundle().apply {
                    putParcelable("genres_enum", ParcelableEnum(GenresEnum.COMEDY))
                    putInt("img_resource", GenresEnum.COMEDY.image)
                })
            }
            findViewById<CardView>(R.id.horror).setOnClickListener{
                controller.navigate(R.id.action_fragmentHome_to_fragmentTop, Bundle().apply {
                    putParcelable("genres_enum", ParcelableEnum(GenresEnum.HORROR))
                    putInt("img_resource", GenresEnum.HORROR.image)
                })
            }
            findViewById<CardView>(R.id.drama).setOnClickListener{
                controller.navigate(R.id.action_fragmentHome_to_fragmentTop, Bundle().apply {
                    putParcelable("genres_enum", ParcelableEnum(GenresEnum.DRAMA))
                    putInt("img_resource", GenresEnum.DRAMA.image)
                })
            }
            findViewById<CardView>(R.id.criminal).setOnClickListener{
                controller.navigate(R.id.action_fragmentHome_to_fragmentTop, Bundle().apply {
                    putParcelable("genres_enum", ParcelableEnum(GenresEnum.CRIMINAL))
                    putInt("img_resource", GenresEnum.CRIMINAL.image)
                })
            }
            findViewById<CardView>(R.id.detective).setOnClickListener{
                controller.navigate(R.id.action_fragmentHome_to_fragmentTop, Bundle().apply {
                    putParcelable("genres_enum", ParcelableEnum(GenresEnum.DETECTIVE))
                    putInt("img_resource", GenresEnum.DETECTIVE.image)
                })
            }
            findViewById<CardView>(R.id.adventure).setOnClickListener{
                controller.navigate(R.id.action_fragmentHome_to_fragmentTop, Bundle().apply {
                    putParcelable("genres_enum", ParcelableEnum(GenresEnum.ADVENTURE))
                    putInt("img_resource", GenresEnum.ADVENTURE.image)
                })
            }
            findViewById<CardView>(R.id.biography).setOnClickListener{
                controller.navigate(R.id.action_fragmentHome_to_fragmentTop, Bundle().apply {
                    putParcelable("genres_enum", ParcelableEnum(GenresEnum.BIOGRAPHY))
                    putInt("img_resource", GenresEnum.BIOGRAPHY.image)
                })
            }
            findViewById<CardView>(R.id.action).setOnClickListener{
                controller.navigate(R.id.action_fragmentHome_to_fragmentTop, Bundle().apply {
                    putParcelable("genres_enum", ParcelableEnum(GenresEnum.ACTION))
                    putInt("img_resource", GenresEnum.ACTION.image)
                })
            }
            findViewById<CardView>(R.id.documental).setOnClickListener{
                controller.navigate(R.id.action_fragmentHome_to_fragmentTop, Bundle().apply {
                    putParcelable("genres_enum", ParcelableEnum(GenresEnum.DOCUMENTARYFILM))
                    putInt("img_resource", GenresEnum.DOCUMENTARYFILM.image)
                })
            }
            findViewById<CardView>(R.id.fantasy).setOnClickListener{
                controller.navigate(R.id.action_fragmentHome_to_fragmentTop, Bundle().apply {
                    putParcelable("genres_enum", ParcelableEnum(GenresEnum.FANTASY))
                    putInt("img_resource", GenresEnum.FANTASY.image)
                })
            }
        }
    }

    inner class ViewHolderRecent(itemView: View): RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.poster)
        val contentType: TextView = itemView.findViewById(R.id.contentType)
        val contentLayout: LinearLayout = itemView.findViewById(R.id.content_layout)
        fun bind(recent: Recent){
            contentLayout.setOnClickListener{
                val bundle: Bundle = Bundle().apply {
                    putInt("id", recent.id_content)
                }
                when(recent.content_type){
                    "tv_series" -> {
                        (activity as MainActivity).navController.navigate(R.id.fragmentAboutSerial, bundle)
                    }
                    "movie" -> {
                        (activity as MainActivity).navController.navigate(R.id.fragmentAboutMovie, bundle)
                    }
                }
            }
            contentType.text = when(recent.content_type){
                "tv_series" -> "Сериал"
                "movie" -> "Фильм"
                else -> "Контент"
            }
            Picasso.get()
                .load(recent.poster)
                .into(image)
        }
    }
    inner class AdapterRecent(val list: List<Recent>): RecyclerView.Adapter<ViewHolderRecent>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderRecent {
            return ViewHolderRecent(
                layoutInflater.inflate(R.layout.layout_recent_element, parent, false)
            )
        }

        override fun onBindViewHolder(holder: ViewHolderRecent, position: Int) {
            holder.bind(list[position])
        }

        override fun getItemCount(): Int = list.size


    }

}