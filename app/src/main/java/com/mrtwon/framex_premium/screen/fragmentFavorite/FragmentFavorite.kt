package com.mrtwon.framex_premium.screen.fragmentFavorite

import android.animation.Animator
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mrtwon.framex_premium.screen.mainActivity.MainActivity
import com.mrtwon.framex_premium.R
import com.mrtwon.framex_premium.app.appComponent
import com.mrtwon.framex_premium.domain.entity.Favorite
import com.mrtwon.framex_premium.domain.entity.enum.ContentEnum
import com.mrtwon.framex_premium.domain.functional.SubscriptionInterface
import com.mrtwon.framex_premium.screen.mainActivity.MainActivityCallback
import com.squareup.picasso.Picasso
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import jp.wasabeef.recyclerview.animators.holder.AnimateViewHolder
import javax.inject.Inject

class FragmentFavorite: Fragment() {
    @Inject
    lateinit var picasso: Picasso
    @Inject
    lateinit var viewModelFactory: FavoriteViewModel.Factory
    val vm: FavoriteViewModel by lazy { ViewModelProvider(this, viewModelFactory).get(FavoriteViewModel::class.java) }
    lateinit var recyclerView: RecyclerView
    lateinit var tvNotFound: TextView
    private val favoriteList = arrayListOf<Favorite>()
    private var saveState: Bundle? = null

    override fun onAttach(context: Context) {
        context
            .appComponent
            .createFavoriteComponent()
            .inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.i("self-favorite","onCreateView favorite")
        saveState = savedInstanceState
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)
        recyclerView = view.findViewById(R.id.recycler_view)
        tvNotFound = view.findViewById(R.id.textView_favorite_not_found)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
        observerFavorite()
        recyclerView.adapter = Adapter(favoriteList)
        recyclerView.itemAnimator = SlideInLeftAnimator()
        return view
    }

    override fun onDestroyView() {
        val state = recyclerView.layoutManager?.onSaveInstanceState()
        saveState?.putInt("test", 100)
        saveState?.putParcelable("rv", state)
        Log.i("self-favorite","onDestroyView() favorite")
        super.onDestroyView()
    }


    private fun observerFavorite(){
        vm.favoriteLiveData.observe(viewLifecycleOwner){ liveData ->
            liveData.setLifecycleOwner(viewLifecycleOwner)
            liveData.observe(object: SubscriptionInterface.Observer<List<Favorite>>{
                override fun onChange(t: List<Favorite>) {
                    if(t.isEmpty()){
                        recyclerView.visibility = View.GONE
                        tvNotFound.visibility = View.VISIBLE
                    }else{
                        favoriteList.clear()
                        favoriteList.addAll(t.reversed())
                        tvNotFound.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        recyclerView.adapter?.notifyDataSetChanged()
                    }
                }

            })
        }
    }

    inner class ViewHolder(private val itemView: View): RecyclerView.ViewHolder(itemView), AnimateViewHolder{

        private val title: TextView = itemView.findViewById(R.id.title)
        private val poster: ImageView = itemView.findViewById(R.id.poster)
        private val contentType: TextView = itemView.findViewById(R.id.contentType)
        private val btn_delete: Button = itemView.findViewById(R.id.btn_delete_favorite)

        fun bind(favorite: Favorite){
            btn_delete.setOnClickListener{
                    vm.removeFavorite(favorite.idContent, favorite.contentType)
            }
            poster.setOnClickListener{
                val bundle = Bundle().apply { putInt("id", favorite.idContent) }
                when(favorite.contentType){

                    ContentEnum.Movie -> {
                        Log.i("self-favorite-page","click movie")
                        (requireActivity() as MainActivityCallback).getNavController().navigate(R.id.action_fragmentFavorite_to_fragmentAboutMovie, bundle)
                    }
                    ContentEnum.Serial -> {
                        Log.i("self-favorite-page","click serial")
                        (requireActivity() as MainActivityCallback).getNavController().navigate(R.id.action_fragmentFavorite_to_fragmentAboutSerial, bundle)
                    }
                    ContentEnum.Undefined -> {
                        Log.i("self-favorite-page","click undefined")
                    }
                }
            }
           title.text = favorite.ruTitle
           contentType.text = favorite.contentType._name
            picasso
                .load(favorite.posterPreview)
                .into(poster)
        }
        override fun preAnimateRemoveImpl(holder: RecyclerView.ViewHolder) {
            // do something
        }

        override fun animateRemoveImpl(holder: RecyclerView.ViewHolder, listener: Animator.AnimatorListener) {
            itemView.animate().apply {
                translationY(-itemView.height * 0.3f)
                alpha(0f)
                duration = 300
                setListener(listener)
            }.start()
        }

        override fun preAnimateAddImpl(holder: RecyclerView.ViewHolder) {
            itemView.setTranslationY(-itemView.height * 0.3f)
            itemView.setAlpha(0f)
        }

        override fun animateAddImpl(holder: RecyclerView.ViewHolder, listener: Animator.AnimatorListener) {
            itemView.animate().apply {
                translationY(0f)
                alpha(1f)
                duration = 300
                setListener(listener)
            }.start()
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