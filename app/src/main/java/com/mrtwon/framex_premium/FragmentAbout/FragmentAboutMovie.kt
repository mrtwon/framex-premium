package com.mrtwon.framex_premium.FragmentAbout

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.constraintlayout.solver.widgets.Helper
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import com.google.android.material.appbar.MaterialToolbar
import com.mrtwon.framex_premium.ActivityWebView.ActivityWebView
import com.mrtwon.framex_premium.Content.ContentTypeEnum
import com.mrtwon.framex_premium.ContentResponse.ContentResponse
import com.mrtwon.framex_premium.Helper.HelperFunction
import com.mrtwon.framex_premium.MainActivity
import com.mrtwon.framex_premium.MyApplication
import com.mrtwon.framex_premium.R
import com.mrtwon.framex_premium.databinding.FragmentAboutMovieBinding
import com.mrtwon.framex_premium.room.MovieDataBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.BlurTransformation
import kotlinx.android.synthetic.main.layout_error_load.view.*
import kotlinx.coroutines.*
import pl.droidsonroids.gif.GifImageView
import java.lang.Exception

class FragmentAboutMovie: Fragment(), View.OnClickListener, Toolbar.OnMenuItemClickListener {
    val aboutVM: AboutMovieViewModel by lazy { ViewModelProvider(this).get(AboutMovieViewModel::class.java) }
    lateinit var view: FragmentAboutMovieBinding
    lateinit var DRAWABLE_ON: Drawable
    lateinit var DRAWABLE_OFF: Drawable

    lateinit var tool_bar: MaterialToolbar
    lateinit var frame_layout: FrameLayout
    lateinit var frame_error: FrameLayout
    lateinit var not_found: View
    lateinit var connect_error: View
    lateinit var load: GifImageView
    var id: Int? = null
    var contentResponse: ContentResponse? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        id = requireArguments().getInt("id")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        (activity as MainActivity).hiddenBottomBar()
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        view = DataBindingUtil.inflate(inflater, R.layout.fragment_about_movie, container, false)
        frame_layout = view.frameLayout
        tool_bar = view.toolBar
        frame_error = view.frameError
        not_found = view.notFound
        connect_error = view.errorLoad
        load = view.gifLoad
        connect_error.reload.setOnClickListener(this)

        DRAWABLE_ON = ResourcesCompat.getDrawable(resources, R.drawable.test_favorite_on, requireActivity().theme)!!
        DRAWABLE_OFF = ResourcesCompat.getDrawable(resources, R.drawable.test_favorite_off, requireActivity().theme)!!

        view.box.background = ResourcesCompat.getDrawable(resources, R.drawable.cornet_view_about, requireActivity().theme)

        //tool_bar.setNavigationIcon(R.drawable.ic_back)
        tool_bar.setNavigationOnClickListener(this)
        tool_bar.setOnMenuItemClickListener { onMenuItemClick(it) }
        view.look.setOnClickListener{ checkBlockAndStartActivity() }
        return view.root
    }

    override fun onDetach() {
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        (activity as MainActivity).showBottomBar()
        super.onDetach()
    }


    @DelicateCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observer()
        id?.let {
            observerIsFavorite(it)
            aboutVM.getAbout(it)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun observerIsFavorite(id: Int){
        aboutVM.getFavoriteLiveData(id).observe(viewLifecycleOwner, Observer {
            val favoriteIconElement = tool_bar.menu.findItem(R.id.favorite)
            if(it != null) {
                favoriteIconElement.icon = DRAWABLE_ON
            }else {
                favoriteIconElement.icon = DRAWABLE_OFF
            }
        })
    }

    private fun loadBackgroundPoster(url: String?){
       val imageBuff = ImageView(requireContext())
        MyApplication.getInstance.picasso
            .load(url)
            .transform(BlurTransformation(requireActivity(), 25, 1))
            .into(imageBuff, object: Callback{
                override fun onSuccess() {
                    view.frameLayout.background = imageBuff.drawable
                }

                override fun onError(e: Exception?) { }

            })
    }

    private fun loadPoster(url: String?){
        MyApplication.getInstance.picasso
            .load(url)
            .fit()
            .into(view.poster, object: Callback{
                override fun onSuccess() {

                }
                override fun onError(e: Exception?) {
                    if(e == null){
                        Log.i("self-about", "exception is null")
                    }
                    e?.printStackTrace()
                    view.poster.setImageResource(R.drawable.connect_error)
                }
            })
    }

    private fun setPosterAndBackground(url: String?) {
        loadPoster(url)
        loadBackgroundPoster(url)
    }


    private fun observer(){
        aboutVM.contentData.observe(viewLifecycleOwner) {

            if (it == null) aboutVM.notFoundLiveData.postValue(true)
            else {
                clearVisibility()
                frame_layout.visibility = View.VISIBLE

                // data binding
                contentResponse = it
                view.movie = MovieDataBinding(it)

                //load poster
                setPosterAndBackground(it.poster_preview)
            }

        }
        aboutVM.connectErrorLiveData.observe(viewLifecycleOwner){
            clearVisibility()
            frame_error.visibility = View.VISIBLE
            connect_error.visibility = View.VISIBLE
        }
        aboutVM.notFoundLiveData.observe(viewLifecycleOwner){
            clearVisibility()
            frame_error.visibility = View.VISIBLE
            not_found.visibility = View.VISIBLE
        }
        aboutVM.loadLiveData.observe(viewLifecycleOwner){
            clearVisibility()
            frame_error.visibility = View.VISIBLE
            load.visibility = View.VISIBLE
        }
    }

    private fun clearVisibility(){
        frame_layout.visibility = View.GONE
        frame_error.visibility = View.GONE
        not_found.visibility = View.GONE
        load.visibility = View.GONE
        connect_error.visibility = View.GONE
    }

    private fun checkBlockAndStartActivity() {
        val intent = Intent(requireContext(), ActivityWebView::class.java)
        intent.putExtra("id", id)
        intent.putExtra("content_type", "movie")
        startActivity(intent)
    }

    private fun sharedContent(){
        id?.let {
            val deepLinkShared = HelperFunction.buildDeepLink(ContentTypeEnum.MOVIE, it)
            val intent = Intent(Intent.ACTION_SEND)
            intent.setType("text/plain")
            intent.putExtra(Intent.EXTRA_TEXT, deepLinkShared)
            startActivity(Intent(intent))
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.reload -> {
                if(id != null) aboutVM.getAbout(id!!)
            }
            else ->{
                (activity as MainActivity).navController.popBackStack()
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.favorite -> {
                contentResponse?.let {aboutVM.favoriteAction(it)}
            }
            R.id.shared -> {
                sharedContent()
            }
        }
        return true
    }
}