package com.mrtwon.framex_premium.screen.fragmentAbout

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.MaterialToolbar
import com.mrtwon.framex_premium.Helper.HelperFunction
import com.mrtwon.framex_premium.screen.mainActivity.MainActivity
import com.mrtwon.framex_premium.app.MyApplication
import com.mrtwon.framex_premium.R
import com.mrtwon.framex_premium.app.appComponent
import com.mrtwon.framex_premium.databinding.FragmentAboutSerialBinding
import com.mrtwon.framex_premium.domain.entity.Content
import com.mrtwon.framex_premium.domain.entity.enum.ContentEnum
import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.domain.functional.SubscriptionInterface
import com.mrtwon.framex_premium.screen.activityWebView.ActivityWebView
import com.mrtwon.framex_premium.screen.mainActivity.MainActivityCallback
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.BlurTransformation
import kotlinx.android.synthetic.main.layout_error_load.view.*
import pl.droidsonroids.gif.GifImageView
import java.lang.Exception
import javax.inject.Inject

class FragmentAboutSerial: Fragment(), View.OnClickListener, Toolbar.OnMenuItemClickListener {
    @Inject
    lateinit var picasso: Picasso
    @Inject
    lateinit var viewModelFactory: AboutSerialViewModel.Factory
    private val aboutVM: AboutSerialViewModel by lazy { ViewModelProvider(this, viewModelFactory).get(AboutSerialViewModel::class.java) }
    private var id: Int? = null
    private var currentContent: Content? = null

    private lateinit var btnSubscription: Button
    private lateinit var drawableOn: Drawable
    private lateinit var drawableOff: Drawable
    private lateinit var view: FragmentAboutSerialBinding

    private lateinit var toolBar: MaterialToolbar
    private lateinit var frameLayout: FrameLayout
    private lateinit var frameLayoutError: FrameLayout
    private lateinit var notFound: View
    private lateinit var connectError: View
    private lateinit var load: GifImageView

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

        view = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_about_serial,
            container,
            false
        )
        frameLayout = view.frameLayout
        frameLayoutError = view.frameError
        notFound= view.notFound
        connectError = view.errorLoad
        load = view.gifLoad
        connectError.reload.setOnClickListener(this)

        toolBar = view.toolBar
        btnSubscription = view.subscription
        drawableOn = ResourcesCompat.getDrawable(
            resources,
            R.drawable.test_favorite_on,
            requireActivity().theme
        )!!
        drawableOff = ResourcesCompat.getDrawable(
            resources,
            R.drawable.test_favorite_off,
            requireActivity().theme
        )!!

        view.box.background = ResourcesCompat.getDrawable(
            resources,
            R.drawable.cornet_view_about,
            requireActivity().theme
        )

        toolBar.setNavigationIcon(R.drawable.ic_back)
        toolBar.setNavigationOnClickListener(this)
        view.look.setOnClickListener{ checkBlockAndStartActivity() }
        toolBar.setOnMenuItemClickListener { onMenuItemClick(it) }
        btnSubscription.setOnClickListener(this)
        return view.root
    }


        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            id?.let {
                observerContent()
                observerExistFavorite()
                observerExistSubscription()
                observerFailure()
                observerLoad()
                aboutVM.initContent(it)
            }
            super.onViewCreated(view, savedInstanceState)
        }

        override fun onDetach() {
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            (activity as MainActivityCallback).showBottomBar()
            super.onDetach()
        }

    private fun observerContent(){
        aboutVM.contentLiveData.observe(viewLifecycleOwner){ content ->
            currentContent = content
            content?.let { contentNotNull ->
                clearVisibility()
                frameLayout.visibility = View.VISIBLE
                view.serial = ContentDataBinding(contentNotNull)
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
            liveData.setLifecycleOwner(viewLifecycleOwner)
            liveData.observe(object: SubscriptionInterface.Observer<Boolean>{
                override fun onChange(t: Boolean) {
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
            //frame_error.visibility = View.VISIBLE
            load.visibility = View.VISIBLE
        }
    }
    private fun observerExistSubscription(){
        aboutVM.subscriptionExistLiveData.observe(viewLifecycleOwner){ liveData ->
            liveData.setLifecycleOwner(viewLifecycleOwner)
            liveData.observe(object: SubscriptionInterface.Observer<Boolean>{
                override fun onChange(t: Boolean) {
                    btnSubscription.text = when(t){
                        true -> {
                            resources.getString(R.string.text_button_unsubscribe)
                        }
                        false -> {
                            resources.getString(R.string.text_button_subscription)
                        }
                    }
                }

            })
        }
    }


    private fun clearVisibility(){
        frameLayout.visibility = View.GONE
        frameLayoutError.visibility = View.GONE
        notFound.visibility = View.GONE
        load.visibility = View.GONE
        connectError.visibility = View.GONE
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
                        view.poster.setImageResource(R.drawable.connect_error)
                    }
                })
        }

    private fun setPosterAndBackground(url: String?) {
            loadPoster(url)
            loadBackgroundPoster(url)
        }

    private fun checkBlockAndStartActivity() {
        val intent = Intent(requireContext(), ActivityWebView::class.java)
        intent.putExtra("id", id)
        intent.putExtra("contentEnum", ContentEnum.Serial.toString())
        startActivity(intent)
    }



    private fun sharedContent(){
        id?.let {
            val deepLinkShared = HelperFunction.buildDeepLink(ContentEnum.Serial, it)
            val intent = Intent(Intent.ACTION_SEND)
            intent.setType("text/plain")
            intent.putExtra(Intent.EXTRA_TEXT, deepLinkShared)
            startActivity(Intent(intent))
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.subscription -> {
                currentContent?.let { contentNotNull ->
                    aboutVM.actionSubscription(contentNotNull.id)
                }
            }
            else -> {
                (activity as MainActivityCallback).getNavController().popBackStack()
            }
        }
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

/* private fun checkBlockAndStartActivity() {
         lifecycle.coroutineScope.launch(Dispatchers.IO + CoroutineExceptionHandler { context, error ->
             Log.i("self-about","error")
             error.printStackTrace()
         }) {
             Log.i("self-about","lifecycle scope to started")
             id?.let { _id ->
                 val isBlocked = aboutVM.model.checkedBlockSync(_id, "tv_series")
                 if (!isBlocked) {
                     Log.i("self-about","not block")
                     val intent = Intent(requireContext(), ActivityWebView::class.java)
                     intent.putExtra("id", _id)
                     intent.putExtra("content_type", "tv_series")
                     startActivity(intent)
                 } else {
                     Log.i("self-about","block")
                     launch(Dispatchers.Main){ Toast.makeText(requireContext(), "Контент не доступен", Toast.LENGTH_LONG).show() }
                 }
             }
         }
     }*/