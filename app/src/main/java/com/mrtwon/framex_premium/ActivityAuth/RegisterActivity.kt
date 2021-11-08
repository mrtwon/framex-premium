package com.mrtwon.framex_premium.ActivityAuth

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.mrtwon.framex_premium.Helper.DetailsError
import com.mrtwon.framex_premium.Helper.TYPE_ERROR
import com.mrtwon.framex_premium.R
import java.util.regex.Pattern

class RegisterActivity: AppCompatActivity() {
    private val vm: RegistrationViewModel by lazy { ViewModelProvider(this).get(RegistrationViewModel::class.java) }
    private val colorAccount: Color = Color(0xFF626262)
    private val colorRed: Color = Color(0xFFFC6565)
    private val colorGreen: Color = Color(0xFF58BF56)
    private var email = mutableStateOf("")
    private var password = mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        setContent {
                MainLayout()
                OpenDialog()
                OpenProgressDialog()
        }
        observeConfirm()
        super.onCreate(savedInstanceState)
    }

    private fun observeConfirm(){
        vm.confirmLiveData.observe(this){
            if(it){
                startActivity(Intent(this, CreateNickNameActivity::class.java))
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
    fun PasswordField() {
        Column {
            val text = remember { password }
            OutlinedTextField(
                value = text.value,
                onValueChange = {
                    if(16 >= it.length) {
                        password.value = it
                    }
                },
                label = { TextFieldLabel(text = "Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                leadingIcon = {
                    Image(painter = painterResource(id = R.drawable.ic_password), contentDescription = "Phone")
                },
                trailingIcon = {
                    Text(text = "${text.value.length}/16")
                },
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

    @Preview
    @Composable
    fun EmailField() {
        Column {
            val text = remember { email }
            OutlinedTextField(
                value = text.value,
                onValueChange = {
                    val replaceIt = it.replace(" ","")
                    email.value = replaceIt
                },
                label = { TextFieldLabel(text = "Email") },
                leadingIcon = {
                    Image(painter = painterResource(id = R.drawable.ic_email), contentDescription = "Phone")
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    cursorColor = myRed(),
                    focusedBorderColor = myRed(),
                    unfocusedBorderColor = myRed(),
                    disabledTextColor = Color.Gray),
                textStyle = TextStyle(fontSize = 18.sp),
                singleLine = true,
                modifier = Modifier.width(260.dp)
            )
        }
    }

    @Preview
    @Composable
    fun RegColumn() {
        Card(
            modifier = Modifier
                .width(320.dp)
                .height(360.dp)
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
                    EmailField()
                    PasswordField()
                    ButtonReg()
                }
            }
        }
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
    fun TextLogin(){
        Text(
            text = "Регестрация", fontSize = 22.sp,
            fontWeight = FontWeight.Medium, color = colorAccount,
            fontFamily = FontFamily.Serif,
            textAlign = TextAlign.Center
        )
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


    @Composable
    fun ButtonReg(){
        val mEmail = remember { email }
        val mPassword = remember { password }
        val isCorrect = mEmail.value.isEmpty() || mEmail.value.isBlank() ||
                        mPassword.value.isEmpty() || mPassword.value.isBlank()
        val alphaNumber = if(isCorrect) 1f else 0.7f
        val enabled = !isCorrect
        Button(
            onClick = {
            if(correctData()){
                vm.sendCreateUserRequest(email.value, password.value)
            }
        },
            enabled = enabled,
            modifier = Modifier
                .alpha(alphaNumber)
                .width(260.dp)
                .padding(top = 15.dp), colors = ButtonDefaults.buttonColors(backgroundColor = colorRed, contentColor = Color.White)) {
            Text(
                text = "Зарегестрироваться", fontSize = 16.sp,
                fontWeight = FontWeight.Bold, fontFamily = FontFamily.SansSerif
            )
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

    @Preview
    @Composable
    fun OpenDialog() {
        val error = remember { vm.errorLiveData }
        if (error.value != null) {
            error.value?.let {
                AlertDialog(
                    onDismissRequest = {
                        if(vm.errorLiveData.value!!.type == TYPE_ERROR.FALIED){
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
                                if(vm.errorLiveData.value!!.type == TYPE_ERROR.FALIED){
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

    private fun correctData(): Boolean{
        if(8 > password.value.length){
            vm.errorLiveData.value = DetailsError(TYPE_ERROR.ERROR, "Ошибка в поле Password", "Password меньше 8 символов")
            return false
        }
        if(!Pattern.matches(".+[@].+[.].+",email.value)){
            vm.errorLiveData.value = DetailsError(TYPE_ERROR.FALIED, "Ошибка в поле Email", "Введён не правильный почтой адрес.\nПример: test@emaple.com")
            return false
        }
        return true
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
    private fun log(s: String){
        Log.i("self-registration", s)
    }
}