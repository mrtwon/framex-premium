package com.mrtwon.framex_premium.ActivityAuth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.mrtwon.framex_premium.Helper.DetailsError
import com.mrtwon.framex_premium.Helper.TYPE_ERROR
import com.mrtwon.framex_premium.MainActivity
import com.mrtwon.framex_premium.R

class CreateNickNameActivity: AppCompatActivity() {
    private val vm: CreateNickNameVM by lazy { ViewModelProvider(this).get(CreateNickNameVM::class.java) }
    private var nickname = mutableStateOf("")
    private val colorAccount: Color = Color(0xFF626262)
    private val colorRed: Color = Color(0xFFFC6565)
    private val colorGreen: Color = Color(0xFF58BF56)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
                MainLayout()
                OpenDialog()
                OpenProgressDialog()
        }
        observeConfirm()
        observeProfile()
        vm.giveMeProfile()
    }


    private fun observeProfile(){
        vm.profileLiveData.observe(this){ userProfile ->
            nickname.value = userProfile.response[0].nickname
        }
    }

    private fun observeConfirm(){
        vm.confirmLiveData.observe(this){
            if(it){
                Toast.makeText(this, "Никнейм изменён", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, MainActivity::class.java).apply {
                    this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK))
                })
            }
        }
    }

    @Preview
    @Composable
    fun MainLayout(){
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WelcomeColumn()
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(4f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                RegColumn()
            }
        }
    }


    @Preview
    @Composable
    fun RegColumn() {
        Card(
            modifier = Modifier
                .width(320.dp)
                .height(260.dp)
                .background(color = Color.Gray),
            elevation = 15.dp
        ) {
            Column(modifier = Modifier.fillMaxSize()){
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center) {
                    TextLogin()
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(4f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    NicknameField()
                    ButtonConfirm()
                }
            }
        }
    }

    @Composable
    fun ButtonConfirm(){
        val nickName = remember{ nickname }
        val isValid = nickName.value.isNotEmpty() && nickName.value.isNotBlank()
        val alphaNumber = if(isValid) 1f else 0.7f
        Button(
            enabled = alphaNumber == 1f,
            onClick = {
                      if(isValid){
                          vm.createNickName(nickname.value)
                      }
            },
            modifier = Modifier
                .width(260.dp)
                .padding(top = 15.dp)
                .alpha(alphaNumber), colors = ButtonDefaults.buttonColors(backgroundColor = colorRed, contentColor = Color.White)){
            Text(
                text = "Готово", fontSize = 16.sp,
                fontWeight = FontWeight.Bold, fontFamily = FontFamily.SansSerif
            )
        }
    }

    @Composable
    fun TextLogin(){
        Text(
            text = "Введите Nickname", fontSize = 22.sp,
            fontWeight = FontWeight.Medium, color = colorAccount,
            fontFamily = FontFamily.Serif,
            textAlign = TextAlign.Center
        )
    }

    @Preview
    @Composable
    fun WelcomeColumn(){
        Card(elevation = 10.dp, modifier = Modifier
            .width(250.dp)
            .height(80.dp)) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally){
                Text(text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Black)){
                        append("Frame")
                    }
                    withStyle(style = SpanStyle(color = colorRed)){
                        append("X")
                    }
                    withStyle(style = SpanStyle(color = colorAccount)){
                        append(" Account")
                    }
                }, fontSize = 23.sp, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold)
            }
        }
    }

    @Composable
    fun LabelApp(){
        Text(
            buildAnnotatedString {
                append("Frame")
                withStyle(style = SpanStyle(color = Color.Red)){
                    append("X")
                }
            },
            fontSize = 20.sp,
        )
    }

    @Composable
    fun NicknameField(){
        Column {
            val text = remember { nickname }
            OutlinedTextField(
                value = text.value,
                onValueChange = {
                    if(16 >= it.length) {
                        val replaceIt = it.replace(" ","")
                        nickname.value = replaceIt
                        //text.value = replaceIt
                    }
                },
                label = { TextFieldLabel(text = "Nickname") },
                leadingIcon = {
                    Image(painter = painterResource(id = R.drawable.ic_account), contentDescription = "Nickname")
                },
                trailingIcon = {
                    Text(text = "${text.value.length}/16")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    cursorColor = myRed(),
                    focusedBorderColor = myRed(),
                    unfocusedBorderColor = myRed()),
                textStyle = TextStyle(fontSize = 18.sp),
                singleLine = true,
                modifier = Modifier.width(260.dp)
            )
        }
    }

    @Composable
    fun TextFieldLabel(text: String){
        val start = text.substring(0, text.length-1)
        val end = text[text.length-1]
        Text(buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color.Gray)) {
                append(start)
            }
            withStyle(style = SpanStyle(color = Color.Red)){
                append(end)
            }
        })
    }


    @Preview
    @Composable
    fun OpenProgressDialog(){
        val isOpen = remember { vm.loadLiveData }
        if(isOpen.value){
            Dialog(
                onDismissRequest = { vm.loadLiveData.value = false },
                DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
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
    
    @Preview
    @Composable
    fun OpenDialog() {
        val mError = remember { vm.errorLiveData }
        if (mError.value != null) {
            mError.value?.let {
                AlertDialog(
                    onDismissRequest = {
                        if (it.type == TYPE_ERROR.FALIED) {
                            finish()
                        }
                        vm.errorLiveData.value = null
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
                                vm.errorLiveData.value = null
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = colorRed,
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

    private fun myRed(): Color{
        return Color(0xFFCA5454)
    }

    private fun myGrey(): Color{
        return Color(0xFFEFEFEF)
    }

    private fun colorAccount(): Color{
        return Color(0x626262)
    }
}