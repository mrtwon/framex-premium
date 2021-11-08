package com.mrtwon.framex_premium.ActivityProfile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.ImagePainter
import coil.compose.LocalImageLoader
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.google.android.material.color.MaterialColors
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.mrtwon.framex_premium.Helper.DetailsError
import com.mrtwon.framex_premium.Helper.TYPE_ERROR
import com.mrtwon.framex_premium.MainActivity
import com.mrtwon.framex_premium.R
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.io.File
import java.io.FileInputStream
import java.lang.Exception
import java.net.URI
import kotlin.reflect.KProperty

class UpdatePhotoActivity : AppCompatActivity() {
    private val vm: UpdatePhotoVM by lazy { ViewModelProvider(this).get(UpdatePhotoVM::class.java) }

    data class Rules(val listRules: List<String>)
    sealed class Warning(val indexWarning: Int) {
        class WARNING_FORMAT(val index: Int = 0) : Warning(index)
        class WARNING_WEIGHT(val index: Int = 1) : Warning(index)
    }

    private val greenColor = Color(0xff519c40)
    private val theme by MaterialTheme
    private val defaultRules = Rules(arrayListOf("Формат PNG/JPEG", "Вес не более 1 Мб"))


    private val warningState = mutableStateOf<ArrayList<Warning>?>(null)
    private val isSuccessful = mutableStateOf<Boolean?>(null)
    private val mStartForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                log("OK")
                result.data?.data?.let {
                    vm.uriState.value = it
                    startCheckFile(it)
                    log(it.toString())
                }
            } else log("NOT OK")
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colors = lightColors(
                    secondary = Color(0xFFCA5454),
                    background = Color(0xFFD1D1D1),
                    primaryVariant = Color(0xFFD1D1D1),
                    primary = Color(0xFFD1D1D1),
                    error = Color(0xFFCA5454)
                )
            ) {
              Column(
                  modifier = Modifier.fillMaxSize(),
                  verticalArrangement = Arrangement.Center,
                  horizontalAlignment = Alignment.CenterHorizontally
              ) {
                  Column(modifier = Modifier.padding(bottom = 10.dp)) {
                      RowImage()
                  }
                  Column() {
                      RowActButton()
                  }
              }
                OpenDialogError()
                OpenPreDialogError()
                OpenProgressDialog()
                OpenPreProgressDialog()
            }
        }
        observerConfirm()
        vm.giveMyPhoto()
    }


    private fun observerConfirm(){
        vm.confirmLiveData.observe(this){
            if(it){
                Toast.makeText(this, "Успешно", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }
    private fun startCheckFile(uri: Uri): Boolean {
        var result = true

        val warningList = arrayListOf<Warning>()
        if (checkFile(uri)) {
            result = false
            warningList.add(Warning.WARNING_WEIGHT())
        }
        if (checkMimeType(uri)) {
            result = false
            warningList.add(Warning.WARNING_FORMAT())
        }

        warningState.value = warningList
        isSuccessful.value = result

        return result
    }

    private fun checkMimeType(uri: Uri): Boolean {
        val contentResolver = this.contentResolver
        val mime = MimeTypeMap.getSingleton()
        val resultMime = mime.getExtensionFromMimeType(contentResolver.getType(uri))
        log("mime type - $resultMime")
        return (resultMime != "jpg" && resultMime != "jpeg" && resultMime != "png")
    }

    private fun checkFile(uri: Uri): Boolean {
        var isNotSuccessful = false
        val inputStream = contentResolver.openInputStream(uri)
        val fileSize = inputStream!!.available()
        log("fileSize $fileSize")
        if(fileSize > 1048576){
            isNotSuccessful = true
        }
        inputStream.close()
        return isNotSuccessful
    }


    @Composable
    fun RowImage(){
        Row {
            Column {
                ImageLoad()
            }
            Column(modifier = Modifier.padding(start = 15.dp)) {
                TextRuleLoad()
            }
        }
    }

    @Composable
    fun RowActButton(){
        val rememberSuccessful = remember { isSuccessful }

        val color = when(rememberSuccessful.value){
            true -> Color.Green
            else -> theme.colors.secondary
        }
        val enabled = when(rememberSuccessful.value){
            false, null -> false
            else -> true
        }
        //val color = if(successful) greenColor else theme.colors.secondary
        Row {
            Column(modifier = Modifier.padding(end = 10.dp)) {
                ActButton(
                    text = "ЗАГРУЗИТЬ",
                    drawableId = R.drawable.upload_image,
                    enabled = enabled,
                    colorStroke = color
                ) {
                    if(rememberSuccessful.value == true){
                        vm.uriState.value?.let {
                            vm.createPhoto(it)
                        }
                    }
                }
            }
            Column() {
                ActButton(
                    text = "ВЫБРАТЬ",
                    drawableId = R.drawable.pick_image
                ) { runLaunch() }
            }
        }
    }

    @Preview
    @Composable
    fun ButtonActionPhoto(){
        Row(){
           OutlinedButton(onClick = {}, border = BorderStroke(1.dp, theme.colors.secondary)) {
                Image(
                    painter = painterResource(id = R.drawable.pick_image),
                    contentDescription = "PickImage",
                    modifier = Modifier
                        .width(25.dp)
                        .height(25.dp)
                )
                Text(text = "ВЫБРАТЬ",style = theme.typography.button, color = Color.Gray, modifier = Modifier.padding(start = 5.dp))
           }
        }
    }
    @Composable
    fun TextRuleLoad(){
        val rememberWarning = remember { warningState }
        val warning = rememberWarning.value
        Text(text = "Ограничения:\n", fontWeight = FontWeight.Medium, fontSize = 16.sp)
        Text(text = buildAnnotatedString {
            //append("Ограничения:\n")
            if(warning == null){
                for(rule in defaultRules.listRules){
                    append(AnnotatedString(text = rule + "\n", spanStyle = SpanStyle(color = Color.Black)))
                }
            }else {
                for (ruleIndex in defaultRules.listRules.indices) {
                    val text = defaultRules.listRules[ruleIndex] + "\n"
                    var isNotWarning = true
                    for (warningElement in warning) {
                        if (ruleIndex == warningElement.indexWarning) {
                            append(
                                AnnotatedString(
                                    text = text,
                                    spanStyle = SpanStyle(color = theme.colors.secondary, fontWeight = FontWeight.Medium)
                                )
                            )
                            isNotWarning = false
                        }
                    }
                    if (isNotWarning)
                        append(
                            AnnotatedString(
                                text = text,
                                spanStyle = SpanStyle(color = greenColor, fontWeight = FontWeight.Light)
                            )
                        )
                }
            }
        })
    }

    @Composable
    fun ImageLoad(){
        val rememberSuccessful = remember { isSuccessful }
        val isLoadPhoto = remember { vm.loadPhotoState }
        val uri = remember { vm.uriState }
        val alpha = when(rememberSuccessful.value){
            true, null -> 1f
            else -> 0.7f
        }
        if(isLoadPhoto.value){
            CircularProgressIndicator(
                modifier = Modifier.size(100.dp),
                color = theme.colors.secondary
            )
        }
        uri.value?.let {
            Image(
                alpha = alpha,
                painter = rememberImagePainter(data = it.toString()),
                contentScale = ContentScale.Crop,
                contentDescription = "Image",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(width = 1.dp, color = Color.Gray, shape = CircleShape)
            )
        }
    }

    @Composable
    fun ActButton(text: String, @DrawableRes drawableId: Int, modifier: Modifier = Modifier,enabled: Boolean = true, colorStroke: Color = theme.colors.secondary, actOnCLick: () -> Unit){
        OutlinedButton(onClick = actOnCLick, border = BorderStroke(1.dp, colorStroke), modifier = modifier, enabled = enabled) {
            Image(
                painter = painterResource(id = drawableId),
                contentDescription = "PickImage",
                modifier = Modifier
                    .width(25.dp)
                    .height(25.dp)
            )
            Text(text = text, style = theme.typography.button, color = Color.Gray, modifier = Modifier.padding(start = 5.dp))
        }
    }

    @Composable
    fun OpenPreProgressDialog(){
        val isOpen = remember { vm.preLoadState }
        if(isOpen.value){
            Dialog(
                onDismissRequest = {
                    vm.preLoadState.value = false
                    finish()
                },
                DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false)
            ) {
                Box(contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color.White, shape = RoundedCornerShape(8.dp))
                ) {
                    CircularProgressIndicator(color = colorResource(id = R.color.colorPrimary))
                }
            }
        }
    }

    @Composable
    fun OpenProgressDialog(){
        val isOpen = remember { vm.loadState }
        if(isOpen.value){
            Dialog(
                onDismissRequest = {
                    vm.loadState.value = false
               },
                DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false)
            ) {
                Box(contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color.White, shape = RoundedCornerShape(8.dp))
                ) {
                    CircularProgressIndicator(color = colorResource(id = R.color.colorPrimary))
                }
            }
        }
    }

    @Composable
    fun OpenPreDialogError() {
        val mError = remember { vm.preErrorState }
        if (mError.value != null) {
            mError.value?.let {
                AlertDialog(
                    onDismissRequest = {
                        vm.preErrorState.value = null
                        finish()
                     },
                    title = {
                        Text(text = it.title)
                    },
                    text = {
                        Text(text = it.message)
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                vm.preErrorState.value = null
                                finish()
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = colorResource(id = R.color.colorPrimary),
                                contentColor = Color.White
                            )
                        ) {
                            Text(text = "Закрыть")
                        }
                    }
                )
            }
        }
    }

    @Composable
    fun OpenDialogError() {
        val mError = remember { vm.errorState }
        if (mError.value != null) {
            mError.value?.let {
                AlertDialog(
                    onDismissRequest = {
                        if (it.type == TYPE_ERROR.FALIED) {
                            finish()
                        }
                        vm.errorState.value = null
                    },
                    title = {
                        Text(text = it.title)
                    },
                    text = {
                        Text(text = it.message)
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                if (it.type == TYPE_ERROR.FALIED) {
                                    finish()
                                }
                                vm.errorState.value = null
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = colorResource(id = R.color.colorPrimary),
                                contentColor = Color.White
                            )
                        ) {
                            Text(text = "Закрыть")
                        }
                    }
                )
            }
        }
    }

    private fun runLaunch(){
        mStartForResult.launch(Intent(Intent.ACTION_GET_CONTENT).apply { this.type = "image/*" })
    }

    private fun log(s: String) {
        Log.i("self-main", s)
    }
}

private operator fun MaterialTheme.getValue(updatePhotoActivity: UpdatePhotoActivity, property: KProperty<*>): MaterialTheme {
    return this
}
