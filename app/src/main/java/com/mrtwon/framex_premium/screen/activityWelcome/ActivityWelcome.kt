package com.mrtwon.framex_premium.screen.activityWelcome

import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.mrtwon.framex_premium.screen.mainActivity.MainActivity
import com.mrtwon.framex_premium.R

class ActivityWelcome: AppCompatActivity(), View.OnClickListener {
    //lateinit var welcome_text: TextView
    private lateinit var webView: WebView
    private lateinit var checkBox: CheckBox
    private lateinit var start: MaterialButton
    private lateinit var image: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_welcome)
        start = findViewById(R.id.start)
        image = findViewById(R.id.action_to_check_box)
        checkBox = findViewById(R.id.check_box)
        webView = findViewById(R.id.web_view)
        start.setOnClickListener(this)
        observerCheckBox()
        setHtmlText()
        super.onCreate(savedInstanceState)
    }
    private fun setHtmlText(){
        webView.settings.javaScriptEnabled = true
        webView.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        val html = """ 
<html>
<head>
<style>
   body{
	background: #D1D1D1;
	color:#000
   }
   mark {
    background: #CA5454;
    padding: 0 3px;
    border: 1px dashed #333;
   }
  p, span { display: inline; color:#ddd; }
  h2 { display: inline; }
  </style>
</head>
<div style="width:100%">
<h2>Frame<h2 style="color:#CA5454">X</h2></h2>
<ul>
<li>У нас <mark><p>нет рекламы</p></mark></li>
<li>Мы не монетизируем приложения</li>
<li>В базе более <mark><p>60 000 контента</p></mark></li>
<li>Уникальный поиск по описанию</li>
<li>Ежедневное добавление <mark><p>нового контента</p></mark></li>
</ul>
</html>
        """
        val encodedHtml: String = Base64.encodeToString(html.toByteArray(), Base64.NO_PADDING)
        webView.loadData(encodedHtml, "text/html", "base64")
    }
    private fun observerCheckBox(){
        checkBox.setOnClickListener{
            if(checkBox.isChecked){
                image.setImageResource(R.drawable.sherlok_welcome)
            }else{
                image.setImageResource(R.drawable.welcome_sherlok_02)
            }
        }
    }

    override fun onClick(v: View?) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}