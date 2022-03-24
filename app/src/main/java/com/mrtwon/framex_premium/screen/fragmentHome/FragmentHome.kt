package com.mrtwon.framex_premium.screen.fragmentHome

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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mrtwon.framex_premium.BuildConfig
import com.mrtwon.framex_premium.screen.dialogDonat.DialogDonate
import com.mrtwon.framex_premium.screen.mainActivity.MainActivity
import com.mrtwon.framex_premium.screen.mainActivity.MainViewModel
import com.mrtwon.framex_premium.app.MyApplication
import com.mrtwon.framex_premium.R
import com.mrtwon.framex_premium.app.appComponent
import com.mrtwon.framex_premium.domain.entity.Recent
import com.mrtwon.framex_premium.domain.entity.enum.ContentEnum
import com.mrtwon.framex_premium.domain.entity.enum.GenresEnum
import com.mrtwon.framex_premium.domain.entity.enum.TopByEnum
import com.mrtwon.framex_premium.domain.functional.SubscriptionInterface
import com.mrtwon.framex_premium.screen.mainActivity.MainActivityCallback
import com.squareup.picasso.Picasso
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import jp.wasabeef.recyclerview.animators.FadeInAnimator
import kotlinx.android.synthetic.main.fragment_home.view.*
import javax.inject.Inject

class FragmentHome: Fragment() {
    @Inject
    lateinit var picasso: Picasso
    @Inject
    lateinit var viewModelFactory: MainViewModel.Factory

    private val vm: MainViewModel by lazy { ViewModelProvider(requireActivity(), viewModelFactory).get(
        MainViewModel::class.java) }
    private val controller by lazy { (requireActivity() as MainActivityCallback).getNavController() }
    private lateinit var recentRv: RecyclerView
    private lateinit var cardViewRecent: CardView
    private lateinit var toolbar: Toolbar
    private val listRecent = arrayListOf<Recent>()

    override fun onAttach(context: Context) {
        context
            .appComponent
            .createHomeComponent()
            .inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recentRv = view.recycler_view_recent
        cardViewRecent = view.recent_card_view
        recentRv.adapter = AdapterRecent(listRecent)
        toolbar = view.findViewById(R.id.tool_bar)
        recentRv.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

        recentRv.adapter = ScaleInAnimationAdapter(AlphaInAnimationAdapter(AdapterRecent(listRecent))).apply {
            setDuration(400)
            setFirstOnly(false)
        }
        recentRv.itemAnimator = FadeInAnimator()
        clickListener(view)
        setVersionCodeForTitle()
        return view
    }

    private fun setVersionCodeForTitle(){
        val appName = getString(R.string.app_name)
        val versionCode = BuildConfig.VERSION_NAME
        toolbar.title = "$appName $versionCode"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observerRecent()
        toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.donate -> {
                    showDonatDialog()
                }
            }
            true
        }
        super.onViewCreated(view, savedInstanceState)
    }


    private fun showDonatDialog(){
        val dialogDonate = DialogDonate()
        dialogDonate.show((requireActivity() as MainActivity).supportFragmentManager, "donateDialog")
    }

    private fun observerRecent(){
        vm.recentLiveData.observe(viewLifecycleOwner){
            it.setLifecycleOwner(viewLifecycleOwner)
            it.observe(object: SubscriptionInterface.Observer<List<Recent>>{
                override fun onChange(t: List<Recent>) {
                    if(t.isEmpty()){
                        cardViewRecent.visibility = View.GONE
                    }else{
                        listRecent.clear()
                        listRecent.addAll(t.reversed())
                        recentRv.adapter?.notifyDataSetChanged()
                        cardViewRecent.visibility = View.VISIBLE
                    }
                }
            })
        }
    }


    private fun clickListener(v: View){
        v.apply {
            findViewById<CardView>(R.id.new_type).setOnClickListener{
                controller.navigate(R.id.action_fragmentHome_to_fragmentTop, Bundle().apply {
                    putString("topByEnum", TopByEnum.CurrentYear.toString())
                    /*putParcelable("collection_enum", ParcelableEnum(CollectionContentEnum.NEW))
                    putInt("img_resource", CollectionContentEnum.NEW.image)*/
                })
            }
            findViewById<CardView>(R.id.comedy).setOnClickListener{
                controller.navigate(R.id.action_fragmentHome_to_fragmentTop, Bundle().apply {
                    putString("topByEnum", TopByEnum.Comedy.toString())
                    putString("genresEnum", GenresEnum.Comedy.toString())
                    /*putParcelable("genres_enum", ParcelableEnum(GenresEnum.COMEDY))
                    putInt("img_resource", GenresEnum.COMEDY.image)*/
                })
            }
            findViewById<CardView>(R.id.horror).setOnClickListener{
                controller.navigate(R.id.action_fragmentHome_to_fragmentTop, Bundle().apply {
                    putString("topByEnum", TopByEnum.Horror.toString())
                    putString("genresEnum", GenresEnum.Horror.toString())
                    /*putParcelable("genres_enum", ParcelableEnum(GenresEnum.HORROR))
                    putInt("img_resource", GenresEnum.HORROR.image)*/
                })
            }
            findViewById<CardView>(R.id.drama).setOnClickListener{
                controller.navigate(R.id.action_fragmentHome_to_fragmentTop, Bundle().apply {
                    putString("topByEnum", TopByEnum.Drama.toString())
                    putString("genresEnum", GenresEnum.Drama.toString())
                    /*putParcelable("genres_enum", ParcelableEnum(GenresEnum.DRAMA))
                    putInt("img_resource", GenresEnum.DRAMA.image)*/
                })
            }
            findViewById<CardView>(R.id.criminal).setOnClickListener{
                controller.navigate(R.id.action_fragmentHome_to_fragmentTop, Bundle().apply {
                    putString("topByEnum", TopByEnum.Criminal.toString())
                    putString("genresEnum", GenresEnum.Criminal.toString())
                    /*putParcelable("genres_enum", ParcelableEnum(GenresEnum.CRIMINAL))
                    putInt("img_resource", GenresEnum.CRIMINAL.image)*/
                })
            }
            findViewById<CardView>(R.id.detective).setOnClickListener{
                controller.navigate(R.id.action_fragmentHome_to_fragmentTop, Bundle().apply {
                    putString("topByEnum", TopByEnum.Detective.toString())
                    putString("genresEnum", GenresEnum.Detective.toString())
                    /*putParcelable("genres_enum", ParcelableEnum(GenresEnum.DETECTIVE))
                    putInt("img_resource", GenresEnum.DETECTIVE.image)*/
                })
            }
            findViewById<CardView>(R.id.adventure).setOnClickListener{
                controller.navigate(R.id.action_fragmentHome_to_fragmentTop, Bundle().apply {
                    putString("topByEnum", TopByEnum.Adventure.toString())
                    putString("genresEnum", GenresEnum.Adventure.toString())
                    /*putParcelable("genres_enum", ParcelableEnum(GenresEnum.ADVENTURE))
                    putInt("img_resource", GenresEnum.ADVENTURE.image)*/
                })
            }
            findViewById<CardView>(R.id.biography).setOnClickListener{
                controller.navigate(R.id.action_fragmentHome_to_fragmentTop, Bundle().apply {
                    putString("topByEnum", TopByEnum.Biography.toString())
                    putString("genresEnum", GenresEnum.Biography.toString())
                    /*putParcelable("genres_enum", ParcelableEnum(GenresEnum.BIOGRAPHY))
                    putInt("img_resource", GenresEnum.BIOGRAPHY.image)*/
                })
            }
            findViewById<CardView>(R.id.action).setOnClickListener{
                controller.navigate(R.id.action_fragmentHome_to_fragmentTop, Bundle().apply {
                    putString("topByEnum", TopByEnum.Action.toString())
                    putString("genresEnum", GenresEnum.Action.toString())
                    /*putParcelable("genres_enum", ParcelableEnum(GenresEnum.ACTION))
                    putInt("img_resource", GenresEnum.ACTION.image)*/
                })
            }
            findViewById<CardView>(R.id.documental).setOnClickListener{
                controller.navigate(R.id.action_fragmentHome_to_fragmentTop, Bundle().apply {
                    putString("topByEnum", TopByEnum.DocFilm.toString())
                    putString("genresEnum", GenresEnum.DocFilm.toString())
                    /*putParcelable("genres_enum", ParcelableEnum(GenresEnum.DOCUMENTARYFILM))
                    putInt("img_resource", GenresEnum.DOCUMENTARYFILM.image)*/
                })
            }
            findViewById<CardView>(R.id.fantasy).setOnClickListener{
                controller.navigate(R.id.action_fragmentHome_to_fragmentTop, Bundle().apply {
                    putString("topByEnum", TopByEnum.Fantastic.toString())
                    putString("genresEnum", GenresEnum.Fantasy.toString())
                    /*putParcelable("genres_enum", ParcelableEnum(GenresEnum.FANTASY))
                    putInt("img_resource", GenresEnum.FANTASY.image)*/
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
                    putInt("id", recent.idContent)
                }
                when(recent.contentType){
                    ContentEnum.Serial -> {

                        controller.navigate(R.id.fragmentAboutSerial, bundle)
                    }
                    ContentEnum.Movie -> {
                        controller.navigate(R.id.fragmentAboutMovie, bundle)
                    }
                    ContentEnum.Undefined -> { }
                }
            }
            contentType.text = recent.contentType._name
            picasso
                .load(recent.posterPreview)
                .fit()
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