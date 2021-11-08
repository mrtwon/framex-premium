package com.mrtwon.framex_premium.ActivityAuth

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.mrtwon.framex_premium.Helper.DetailsError
import com.mrtwon.framex_premium.Helper.TYPE_ERROR
import com.mrtwon.framex_premium.MainActivity
import com.mrtwon.framex_premium.R
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class AuthActivity: AppCompatActivity() {
    private val vm: AuthViewModel by lazy { ViewModelProvider(this).get(AuthViewModel::class.java) }
    private val colorAccount: Color = Color(0xFF626262)
    private val colorRed: Color = Color(0xFFFC6565)
    private val colorGreen: Color = Color(0xFF58BF56)
    private var email = mutableStateOf("")
    private var password = mutableStateOf("")
    private val emailForReset = mutableStateOf("")
    private val isOpenResetPassword = mutableStateOf(false)
    override fun onCreate(savedInstanceState: Bundle?) {

        setContent {
                MainLayout()
                OpenDialog()
                OpenProgressDialog()
                OpenResetPasswordDialog()
        }
        observeConfirm()
        super.onCreate(savedInstanceState)
    }

    private fun observeConfirm(){
        vm.confirmLiveData.observe(this){
            if(it){
                Toast.makeText(this, "Вы авторизированны", Toast.LENGTH_LONG).show()
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
                LoginColumn()
            }
        }
    }

    @Preview
    @Composable
    fun PasswordField() {
        Column {
            val mPassword = remember { password }
            OutlinedTextField(
                value = mPassword.value,
                onValueChange = { it ->
                    password.value = it
                },
                label = { TextFieldLabel(text = "Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                leadingIcon = {
                    Image(painter = painterResource(id = R.drawable.ic_password), contentDescription = "Phone")
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
            val mEmail = remember { email }
            OutlinedTextField(
                value = mEmail.value,
                onValueChange = {
                    email.value = it
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
    fun LoginColumn() {
            Card(
                modifier = Modifier
                    .width(320.dp)
                    .height(320.dp)
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
                            ButtonLogin()
                        ActionLayout()
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
                text = "Авторизация", fontSize = 22.sp,
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
    fun ActionLayout(){
        Row(modifier = Modifier.padding(top = 1.dp)) {
           TextButton(onClick = { isOpenResetPassword.value = true }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(0x00000000), contentColor = colorRed)) {
               Text(
                   text = "Забыл Пароль",
                   fontFamily = FontFamily.SansSerif, fontSize = 14.sp,
                   fontWeight = FontWeight.Bold
               )
           }
           TextButton(onClick = { startActivity(Intent(this@AuthActivity, RegisterActivity::class.java)) }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(0x00000000), contentColor = colorGreen)) {
                Text(
                    text = "Регестрация",
                    fontFamily = FontFamily.SansSerif, fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }


    @Composable
    fun ButtonLogin(){
        val mEmail = remember { email }
        val mPassword = remember { password }
        val isValid = (
                    mEmail.value.isNotBlank() && mEmail.value.isNotEmpty()
                    && mPassword.value.isNotEmpty() && mPassword.value.isNotBlank()
                )
        Button(
            enabled = isValid,
            onClick ={
                vm.loginWithEmailPassword(email.value, password.value)
            },
            modifier = Modifier
                .width(260.dp)
                .padding(top = 15.dp), colors = ButtonDefaults.buttonColors(backgroundColor = colorRed, contentColor = Color.White)) {
            Text(
                text = "Войти", fontSize = 16.sp,
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

    @Composable
    fun TextRestPassword(){
        Text(text = "На Email будет отправлено письмо с инструкцией", fontSize = 15.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(bottom = 10.dp).fillMaxWidth(), color = colorAccount, textAlign = TextAlign.Center)
    }
    @Composable
    fun ButtonResetPassword(){
        val mEmail = remember { emailForReset }
        val isValid = mEmail.value.isNotBlank() && mEmail.value.isNotEmpty()
        Button(
            enabled = isValid,
            onClick = {
            if(correctEmail(emailForReset.value)){
                isOpenResetPassword.value = false
                Toast.makeText(this, "На почту было отправлено письмо с инструкцией", Toast.LENGTH_LONG).show()
            }
        }, colors = ButtonDefaults.buttonColors(backgroundColor = colorRed, contentColor = Color.White), modifier = Modifier.padding(top = 15.dp)) {
            Text(text = "Сбросить пароль", fontSize = 15.sp, fontWeight = FontWeight.Medium)
        }
    }

    @Preview
    @Composable
    fun OpenResetPasswordDialog() {
        val isOpen = remember { isOpenResetPassword }
        if (isOpen.value) {
            Dialog(
                onDismissRequest = { isOpenResetPassword.value = false},
                DialogProperties(dismissOnClickOutside = false)
            ) {
                Column(modifier = Modifier
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                    .width(300.dp)
                    .height(200.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val mEmail = remember { emailForReset }
                    TextRestPassword()
                    OutlinedTextField(
                        value = mEmail.value,
                        onValueChange = {
                            emailForReset.value = it
                        },
                        label = { TextFieldLabel(text = "Email") },
                        leadingIcon = {
                            Image(
                                painter = painterResource(id = R.drawable.ic_email),
                                contentDescription = "Phone"
                            )
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            cursorColor = myRed(),
                            focusedBorderColor = myRed(),
                            unfocusedBorderColor = myRed(),
                            disabledTextColor = Color.Gray
                        ),
                        textStyle = TextStyle(fontSize = 18.sp),
                        singleLine = true,
                        modifier = Modifier.width(260.dp)
                    )
                    ButtonResetPassword()
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

    private fun correctEmail(email: String): Boolean{
        if(!Pattern.matches(".+[@].+[.].+",email)){
            vm.errorLiveData.value = DetailsError(TYPE_ERROR.ERROR, "Ошибка в поле Email", "Введён не правильный почтой адрес.\nПример: test@emaple.com")
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

    fun log (s: String) = Log.i("self-main", s)
}