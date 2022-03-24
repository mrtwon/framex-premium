package com.mrtwon.framex_premium.screen.activityWebView

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.work.ListenableWorker
import com.mrtwon.framex_premium.R
import com.mrtwon.framex_premium.app.appComponent
import com.mrtwon.framex_premium.domain.entity.enum.ContentEnum
import com.mrtwon.framex_premium.domain.exception.Failure
import java.io.ByteArrayInputStream
import javax.inject.Inject


class ActivityWebView: AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: WebViewVM.Factory
    private lateinit var webView: WebView
    private var id: Int? = null
    private val vm: WebViewVM by lazy { ViewModelProvider(this, viewModelFactory).get(WebViewVM::class.java) }
    private var currentUriToContent: Uri? = null
    private lateinit var contentEnum: ContentEnum
    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_webview)
        appComponent
            .createWebViewComponent()
            .inject(this)
        webView = findViewById(R.id.web_view)
        initWebView(webView)
        id = intent.getIntExtra("id", 0)
        contentEnum = ContentEnum.valueOf(intent.getStringExtra("contentEnum")!!)
        if(id != 0 && contentEnum != ContentEnum.Undefined) {
            observerContent()
            observerFailure()
            vm.getVideoLink(id!!, contentEnum)
        }else{
            Toast.makeText(this, "Не возможно найти контент", Toast.LENGTH_LONG).show()
        }
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("SetJavaScriptEnabled")
    fun initWebView(wv: WebView){
        wv.apply {
            webViewClient = BlockOtherVideo()
            webChromeClient = ChromeClient()
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.mediaPlaybackRequiresUserGesture = false
        }
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
    }

    private fun observerContent(){
        vm.contentLD.observe(this){ content ->
            content?.iframeSrc?.let {  iframeNotNull ->
                webView.loadUrl("http:$iframeNotNull")
            }
        }
    }

    private fun observerFailure(){
        vm.errorLD.observe(this){ failure ->
            val textError = when(failure){
                Failure.ServerError -> "Ошибка со стороны сервера"
                Failure.ClientError -> "Ошибка со стороны приложения"
                Failure.NetworkConnection -> "Ошибка запроса"
            }
            Toast.makeText(this, textError, Toast.LENGTH_LONG).show()
        }
    }

    private inner class ChromeClient : WebChromeClient(){
        override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
            if(consoleMessage != null && consoleMessage.message().contains("download")){
                if(currentUriToContent == null) toastErrorOpenBrowser()
                else openBrowser()
            }
            //log("CONSOLE: "+consoleMessage?.message())
            return super.onConsoleMessage(consoleMessage)
        }
    }

    private inner class BlockOtherVideo internal constructor() : WebViewClient() {

        override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
            actionContentLink(request)
            if(isBlockedUrl(request)){
                log("BLOCKED host: ${request?.url?.host} | path ${request?.url?.path}")
                return createEmptyResource()
            }else{
                log("accept url: host: ${request?.url?.host} | path ${request?.url?.path}")
                return null
            }
        }

        fun createEmptyResource(): WebResourceResponse {
            return WebResourceResponse(
                "text/plain",
                "utf-8",
                ByteArrayInputStream("".toByteArray())
            )
        }

        fun isBlockedUrl(webResource: WebResourceRequest?): Boolean{
            if(webResource == null) return false
            val accessHost = "cdnland.in"
            val currentHost = webResource.url.host
            val currentPath = webResource.url.path
            log("host: $currentHost | path $currentPath")
            if(currentHost == null || currentPath == null){
                return false
            }
            if(".mp4" in currentPath){
                return accessHost !in currentHost
            }
            return false
        }

    }

    fun actionContentLink(webResource: WebResourceRequest?) {
        if (webResource == null) return
        val targetHost = "cloud.cdnland.in"
        val fullAddress = webResource.url
        val currentHost = webResource.url.host ?: return
        if (currentHost.contains(targetHost, ignoreCase = true)) {
            log("add to currentUri")
            currentUriToContent = fullAddress
        }
    }

    fun openBrowser(){
        currentUriToContent?.let { link ->
            log("openBrowser")
            startActivity(Intent(Intent.ACTION_VIEW, link))
        }
    }

    fun toastErrorOpenBrowser(){
        Toast.makeText(this, "Ошибка. Подождите загрузку контента", Toast.LENGTH_LONG).show()
    }

    fun log(s: String){
        Log.i("self-webview-debug",s)
    }

}