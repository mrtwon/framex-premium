package com.mrtwon.framex_premium.FragmentTop

import android.animation.Animator
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.ViewPropertyAnimatorListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mrtwon.framex_premium.Content.CollectionContentEnum
import com.mrtwon.framex_premium.Content.ContentTypeEnum
import com.mrtwon.framex_premium.Content.GenresEnum
import com.mrtwon.framex_premium.Content.ParcelableEnum
import com.mrtwon.framex_premium.ContentResponse.ContentResponse
import com.mrtwon.framex_premium.Helper.HelperFunction.Companion.roundRating
import com.mrtwon.framex_premium.MainActivity
import com.mrtwon.framex_premium.MyApplication
import com.mrtwon.framex_premium.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import jp.wasabeef.recyclerview.animators.FadeInAnimator
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import jp.wasabeef.recyclerview.animators.holder.AnimateViewHolder
import kotlinx.android.synthetic.main.recyclerview_top_element.view.*
import kotlinx.coroutines.DelicateCoroutinesApi
import pl.droidsonroids.gif.GifImageView
import java.lang.Exception

class FragmentTopContent: Fragment(), View.OnClickListener {
    val vm: TopViewModel by lazy { ViewModelProvider(this).get(TopViewModel::class.java) }
    val controller by lazy { (requireActivity() as MainActivity).navController }

    lateinit var gif_load: GifImageView
    lateinit var contentType: ContentTypeEnum
    lateinit var rv: RecyclerView
    lateinit var not_found: LinearLayout
    lateinit var connect_error: LinearLayout
    lateinit var reload: Button

    val listContent = arrayListOf<ContentResponse>()
    var genres: GenresEnum? = null
    var collection: CollectionContentEnum? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("self-top","onCreate()")
        // init content data of arg
        contentType = requireArguments().getParcelable<ParcelableEnum>("content_enum")!!.contentTypeEnum!!
        genres = arguments?.getParcelable<ParcelableEnum>("genres_enum")?.genresEnum
        collection = arguments?.getParcelable<ParcelableEnum>("collection_enum")?.collectionEnum
        Log.i("self-top","contentType = $contentType | genres = $genres")
        super.onCreate(savedInstanceState)
    }

    override fun onDestroyView() {
        log("onDestroyView()")
        super.onDestroyView()
    }
    @DelicateCoroutinesApi
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        log("onCreateView")
        val view = inflater.inflate(R.layout.recyclerview_top_element, container, false)
        rv = view.recycler_view
        rv.adapter = ScaleInAnimationAdapter(AlphaInAnimationAdapter(Adapter(listContent))).apply {
            setDuration(700)
            setFirstOnly(false)
        }
        gif_load = view.findViewById(R.id.gif_load)
        connect_error = view.findViewById(R.id.error_load)
        not_found = view.findViewById(R.id.not_found)
        reload = view.findViewById(R.id.reload)
        reload.setOnClickListener(this)
        rv.layoutManager = GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
        rv.itemAnimator = FadeInAnimator()
        observer()
        if(genres != null){
            vm.getContentByGenresEnum(genres!!, contentType)
        }
        else if(collection != null){
            vm.getContentByCollectionEnum(collection!!, contentType)
        }
        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        log("saveInstanceState")
        rv.layoutManager?.onSaveInstanceState()?.let {
            outState.putParcelable("layout_manager",it)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        savedInstanceState?.getParcelable<Parcelable>("layout_manager")?.let {
        rv.layoutManager?.onRestoreInstanceState(it)
        }
        super.onActivityCreated(savedInstanceState)
    }
    fun observer(){
        vm.listLiveData.observe(viewLifecycleOwner) {
            log("observer is running")
            if (listContent.isEmpty() && it.isEmpty()) {
                vm.notFoundLiveData.postValue(true)
            } else {
                clearVisibility()
                for(elem in it){
                    listContent.add(listContent.lastIndex+1, elem)
                    rv.adapter?.notifyItemInserted(listContent.lastIndex)
                }
                rv.visibility = View.VISIBLE
            }
        }
        vm.connectErrorLiveData.observe(viewLifecycleOwner){
            if(it) {
                clearVisibility()
                connect_error.visibility = View.VISIBLE
                vm.connectErrorLiveData.postValue(false)
            }
        }
        vm.notFoundLiveData.observe(viewLifecycleOwner) {
            if (it) {
                clearVisibility()
                not_found.visibility = View.VISIBLE
                vm.notFoundLiveData.postValue(false)
            }
        }
        vm.loadLiveData.observe(viewLifecycleOwner){
            if(it){
                clearVisibility()
                gif_load.visibility = View.VISIBLE
            }else{
                clearVisibility()
                gif_load.visibility = View.GONE
            }
        }
    }
    fun clearVisibility(){
        rv.visibility = View.GONE
        not_found.visibility = View.GONE
        connect_error.visibility = View.GONE
        gif_load.visibility = View.GONE
    }
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), AnimateViewHolder{

        val content_layout = itemView.findViewById<LinearLayout>(R.id.content_layout)
        val poster = itemView.findViewById<ImageView>(R.id.poster)
        val rating_kp = itemView.findViewById<TextView>(R.id.kp_rating)
        val rating_imdb = itemView.findViewById<TextView>(R.id.imdb_rating)
        val title = itemView.findViewById<TextView>(R.id.title)
        fun build(content: ContentResponse){
            //init
            //build
            if(content.poster != null) {
                MyApplication.getInstance.picasso
                    .load(content.poster_preview)
                    .fit()
                    .into(poster, object: Callback{
                    override fun onSuccess() {}
                    override fun onError(e: Exception?) {
                        Log.i("self-top-content","error image load")
                        poster.setImageResource(R.drawable.connect_error)
                    }
                })
            }

            rating_kp.text = roundRating(content.kinopoisk_raintg?.toDouble())
            rating_imdb.text = roundRating(content.imdb_rating?.toDouble())
            if(content.ru_title != null){
                title.text = content.ru_title
            }
            content_layout.setOnClickListener {
                val bundle = Bundle().apply {
                    putInt("id", content.id)
                }
                when(contentType){
                    ContentTypeEnum.MOVIE -> { controller.navigate(R.id.action_fragmentTop_to_fragmentAboutMovie, bundle) }
                    ContentTypeEnum.SERIAL -> { controller.navigate(R.id.action_fragmentTop_to_fragmentAboutSerial, bundle)}
                }
            }

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
    inner class Adapter(val contentList: List<ContentResponse>): RecyclerView.Adapter<ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            log("[recyclerview] onCreateViewHolder()")
            val view = layoutInflater.inflate(R.layout.one_top_element, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            log("[recyclerview] onBindViewHolder position ${position}")
            if(position == contentList.size-1){
                if(genres != null) vm.giveNextPageGenres(genres!!, contentType)
                else if(collection != null) vm.giveNextPageCollection(collection!!, contentType)
            }
            holder.build(contentList[position])
        }

        override fun getItemCount(): Int {
            return contentList.size
        }
    }

    companion object{
        fun instance(contentType: ContentTypeEnum, bundle: Bundle): Fragment{
            return FragmentTopContent().apply {
                arguments = bundle.apply {
                    putParcelable("content_enum", ParcelableEnum(contentType))
                }
            }
        }
    }

    /*override fun onPause() {
        Log.i("self-top","onPause()")
        super.onPause()
    }
    override fun onStop() {
        Log.i("self-top","onStop()")
        super.onStop()
    }
    override fun onResume() {
        Log.i("self-top","onResume()")
        super.onResume()
    }
    override fun onDestroy() {
        Log.i("self-top","Destroy()")
        super.onDestroy()
    }*/
    private fun log(s: String){
        Log.i("self-top-content",s)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.reload -> {
                if(genres != null){
                    if(listContent.isEmpty()){
                        vm.getContentByGenresEnum(genres!!, contentType)
                    }else{
                        vm.giveNextPageGenres(genres!!, contentType)
                    }
                }
                else if(collection != null){
                    if(listContent.isEmpty()){
                        vm.getContentByCollectionEnum(collection!!, contentType)
                    }else{
                        vm.giveNextPageCollection(collection!!, contentType)
                    }
                }

            }
        }
    }

}