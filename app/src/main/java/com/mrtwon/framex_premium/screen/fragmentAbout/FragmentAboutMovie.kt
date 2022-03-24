package com.mrtwon.framex_premium.screen.fragmentAbout

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.MaterialToolbar
import com.mrtwon.framex_premium.screen.activityWebView.ActivityWebView
import com.mrtwon.framex_premium.Helper.HelperFunction
import com.mrtwon.framex_premium.screen.mainActivity.MainActivity
import com.mrtwon.framex_premium.app.MyApplication
import com.mrtwon.framex_premium.R
import com.mrtwon.framex_premium.app.appComponent
import com.mrtwon.framex_premium.databinding.FragmentAboutMovieBinding
import com.mrtwon.framex_premium.domain.entity.Content
import com.mrtwon.framex_premium.domain.entity.enum.ContentEnum
import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.domain.functional.SubscriptionInterface
import com.mrtwon.framex_premium.screen.mainActivity.MainActivityCallback
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.BlurTransformation
import kotlinx.android.synthetic.main.layout_error_load.view.*
import pl.droidsonroids.gif.GifImageView
import java.lang.Exception
import javax.inject.Inject

class FragmentAboutMovie: Fragment(), View.OnClickListener, Toolbar.OnMenuItemClickListener {
    @Inject
    lateinit var picasso: Picasso
    @Inject
    lateinit var viewModelFactory: AboutMovieViewModel.Factory
    private val aboutVM: AboutMovieViewModel by lazy { ViewModelProvider(this, viewModelFactory).get(AboutMovieViewModel::class.java) }
    private lateinit var view: FragmentAboutMovieBinding
    private lateinit var drawableOn: Drawable
    private lateinit var drawableOff: Drawable

    private lateinit var toolBar: MaterialToolbar
    private lateinit var frameLayout: FrameLayout
    private lateinit var frameLayoutError: FrameLayout
    private lateinit var notFound: View
    private lateinit var connectError: View
    private lateinit var load: GifImageView
    private var id: Int? = null
    private var currentContent: Content? = null

    override fun onAttach(context: Context) {
        context
            .appComponent
            .createContentAboutComponent()
            .inject(this)
        super.onAttach(context)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        id = requireArguments().getInt("id")
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        (activity as MainActivityCallback).hiddenBottomBar()
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        view = DataBindingUtil.inflate(inflater, R.layout.fragment_about_movie, container, false)
        frameLayout = view.frameLayout
        toolBar = view.toolBar
        frameLayoutError = view.frameError
        notFound = view.notFound
        connectError = view.errorLoad
        load = view.gifLoad
        connectError.reload.setOnClickListener(this)

        drawableOn = ResourcesCompat.getDrawable(resources, R.drawable.test_favorite_on, requireActivity().theme)!!
        drawableOff = ResourcesCompat.getDrawable(resources, R.drawable.test_favorite_off, requireActivity().theme)!!

        view.box.background = ResourcesCompat.getDrawable(resources, R.drawable.cornet_view_about, requireActivity().theme)

        toolBar.setNavigationOnClickListener(this)
        toolBar.setOnMenuItemClickListener { onMenuItemClick(it) }
        view.look.setOnClickListener{ checkBlockAndStartActivity() }

        return view.root
    }

    override fun onDetach() {
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        (activity as MainActivityCallback).showBottomBar()
        super.onDetach()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        id?.let { idNotNull ->
            aboutVM.initContent(idNotNull)
            observerLoad()
            observerFailure()
            observerExistFavorite()
            observerContent()
        }
        super.onViewCreated(view, savedInstanceState)
    }



    private fun observerContent(){
        aboutVM.contentLiveData.observe(viewLifecycleOwner){ content ->
            currentContent = content
            content?.let { contentNotNull ->
                clearVisibility()
                frameLayout.visibility = View.VISIBLE
                view.movie = ContentDataBinding(contentNotNull)
                setPosterAndBackground(contentNotNull.posterPreview)
            }
            if(content == null){
                clearVisibility()
                frameLayoutError.visibility = View.VISIBLE
                notFound.visibility = View.VISIBLE
            }
        }
    }
    private fun observerFailure(){
        aboutVM.errorLiveData.observe(viewLifecycleOwner){ failure ->
            when(failure){
                Failure.NetworkConnection -> {
                    clearVisibility()
                    frameLayoutError.visibility = View.VISIBLE
                    connectError.visibility = View.VISIBLE
                }
                else -> {}
            }
        }
    }
    private fun observerExistFavorite(){
        val favoriteIconElement = toolBar.menu.findItem(R.id.favorite)
        aboutVM.favoriteExistLiveData.observe(viewLifecycleOwner){ liveData ->
            Log.i("self-favorite","on observer change favorite state")
            liveData.setLifecycleOwner(viewLifecycleOwner)
            liveData.observe(object: SubscriptionInterface.Observer<Boolean>{
                override fun onChange(t: Boolean) {
                    Log.i("self-favorite","change favorite $t")
                    when(t){
                        true -> {
                            favoriteIconElement.icon = drawableOn
                        }
                        false -> {
                            favoriteIconElement.icon = drawableOff
                        }
                    }
                }

            })
        }
    }
    private fun observerLoad(){
        aboutVM.loadLiveData.observe(viewLifecycleOwner){
            clearVisibility()
            frameLayoutError.visibility = View.VISIBLE
            load.visibility = View.VISIBLE
        }
    }


    private fun loadBackgroundPoster(url: String?){
       val imageBuff = ImageView(requireContext())
        picasso
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
        picasso
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

    private fun clearVisibility(){
        frameLayout.visibility = View.GONE
        frameLayoutError.visibility = View.GONE
        notFound.visibility = View.GONE
        load.visibility = View.GONE
        connectError.visibility = View.GONE
    }

    private fun checkBlockAndStartActivity() {
        val intent = Intent(requireContext(), ActivityWebView::class.java)
        intent.putExtra("id", id)
        intent.putExtra("contentEnum", ContentEnum.Movie.toString())
        startActivity(intent)
    }

    private fun sharedContent(){
        id?.let {
            val deepLinkShared = HelperFunction.buildDeepLink(ContentEnum.Movie, it)
            val intent = Intent(Intent.ACTION_SEND)
            intent.setType("text/plain")
            intent.putExtra(Intent.EXTRA_TEXT, deepLinkShared)
            startActivity(Intent(intent))
        }
    }

    override fun onClick(v: View?) {
        (activity as MainActivityCallback).getNavController().popBackStack()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.favorite -> {
                currentContent?.let { aboutVM.actionFavorite(it.id, it.contentType) }
            }
            R.id.shared -> {
                sharedContent()
            }
        }
        return true
    }
}