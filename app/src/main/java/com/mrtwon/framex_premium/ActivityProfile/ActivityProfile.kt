package com.mrtwon.framex_premium.ActivityProfile

import android.graphics.Paint
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.res.ResourcesCompat
import com.mrtwon.framex_premium.R
import kotlinx.android.synthetic.main.activity_welcome.*

class ActivityProfile: AppCompatActivity() {
    private val colorBackgroundCard = Color(0xFFD1D1D1)
    private val colorTextCard = Color(0xFF4D4D4D)
    private val newNickNameField = mutableStateOf("")
    private val isOpenEditNickName = mutableStateOf(false)
    private val isOpenEditFavoriteContent = mutableStateOf(false)
    private val isOpenEditAboutMe = mutableStateOf(false)
    private val newFavoriteContent = mutableStateOf("")
    private val newAboutMe = mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        setContent {
            MainLayout()
            OpenEditNickName()
            OpenMultiEditDialog(
                field = newFavoriteContent, isOpen = isOpenEditFavoriteContent,
                titleEdit = "Введите ваш любимый контент", label = "Избранный контент") {

            }
            OpenMultiEditDialog(
                field = newAboutMe, isOpen = isOpenEditAboutMe ,
                titleEdit = "Расскажите немного о себе", label = "О себе"
            ) {

            }
        }
        super.onCreate(savedInstanceState)
    }

    @Preview
    @Composable
    fun MainLayout(){
        Column(modifier = Modifier.fillMaxSize()) {
            Column {
                Column(modifier = Modifier.padding(bottom = 10.dp)) {
                    TopAction()
                }
                Column(modifier = Modifier.padding(bottom = 50.dp)) {
                    Profile()
                }
                Column(modifier = Modifier.padding(bottom = 10.dp)) {
                    FavoriteContent()
                }
                Column(modifier = Modifier.padding(bottom = 20.dp)) {
                    AboutMe()
                }
                Column(modifier = Modifier.padding(bottom = 10.dp)) {
                    FavoriteList()
                }
            }
        }
    }

    @Preview
    @Composable
    fun Profile() {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .padding(start = 20.dp, end = 20.dp)
                .background(colorBackgroundCard, shape = RoundedCornerShape(8.dp)),
             elevation = 12.dp)
         {
            Row(modifier = Modifier.background(colorBackgroundCard)) {
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 20.dp), verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.test_poster),
                        contentDescription = "avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(84.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.Yellow, CircleShape)
                    )
                    Column(
                        modifier = Modifier.padding(start = 10.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Column(modifier = Modifier.pointerInput(Unit) {
                            detectTapGestures(
                                onLongPress = {
                                    isOpenEditNickName.value = true
                                }
                            )
                        }) {
                            TextWithRed(text = "Mrtwon", textSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                        Column(modifier = Modifier.padding(top = 10.dp)) {
                            TextWithRed(text = "Пользователь", textSize = 15.sp)
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(end = 10.dp, bottom = 5.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.Bottom
                ) {
                    StatusDonate()
                }
            }
        }
    }

    @Preview
    @Composable
    fun StatusDonate(){
        Column {
            Image(
                painter = painterResource(id = R.drawable.ic_corone), contentDescription = "donate",
                modifier = Modifier.size(60.dp)
            )
            Text(
                text = "Donate", fontSize = 18.sp, fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold, color = Color.Yellow, modifier = Modifier.padding(top = 10.dp)
            )
        }
    }

    @Preview
    @Composable
    fun FavoriteContent() {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp)
                .background(colorBackgroundCard, shape = RoundedCornerShape(8.dp)),
            elevation = 8.dp
        ) {
                Column(
                    modifier = Modifier
                        .background(colorBackgroundCard)
                        .fillMaxWidth()
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onLongPress = {
                                    isOpenEditFavoriteContent.value = true
                                }
                            )
                        }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TextWithRed(text = "Любимый контент", textSize = 18.sp, fontWeight = FontWeight.Medium)
                    }
                    TextProfileCard(text = "Теория большого взрыва, во все тяжкие, как я встретил вашу маму")
                }
            }
        }

    @Preview
    @Composable
    fun AboutMe() {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp)
                .background(colorBackgroundCard, shape = RoundedCornerShape(8.dp)),
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .background(colorBackgroundCard)
                    .fillMaxWidth()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = {
                                isOpenEditAboutMe.value = true
                            }
                        )
                    }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextWithRed(text = "О себе", textSize = 18.sp, fontWeight = FontWeight.Medium)
                }
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    TextProfileCard(text = "Создатель FrameX, люблю смотреть ситкомы и детективы")
                }
            }
        }
    }

    @Composable
    fun FavoriteList(){
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp)
                .background(colorBackgroundCard, shape = RoundedCornerShape(8.dp)), elevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .background(colorBackgroundCard)
                    .fillMaxWidth()
                    .padding(bottom = 10.dp, top = 10.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextWithRed(text = "Избранные пользователя", textSize = 18.sp, fontWeight = FontWeight.Medium)
                }
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    NotFound(text = "🔒 Информация скрыта пользователем")
                }
            }
        }
    }

    @Preview
    @Composable
    fun OpenEditNickName() {
        val nickname = remember { newNickNameField }
        val isOpen = remember { isOpenEditNickName }
        if (isOpen.value) {
            val isValid = nickname.value.isNotEmpty() && nickname.value.isNotBlank()
            Dialog(onDismissRequest = {
                isOpenEditNickName.value = false
                newNickNameField.value = ""
            }) {
                Card(
                    modifier = Modifier
                        .padding(10.dp)
                        .background(colorBackgroundCard, RoundedCornerShape(8.dp)),
                    elevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier
                            .background(colorBackgroundCard)
                            .padding(25.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        TextInformationEdit(text = "Введите новый NickName")
                        NicknameField()
                        Button(
                            enabled = isValid,
                            onClick = {},
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = colorResource(id = R.color.colorPrimary),
                                contentColor = colorResource(id = R.color.colorPrimaryDark)
                            ),
                            modifier = Modifier.padding(bottom = 10.dp)
                        ) {
                            Text(text = "ИЗМЕНИТЬ")
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun OpenMultiEditDialog(field: MutableState<String>, isOpen: MutableState<Boolean>, titleEdit: String,  defaultValue: String = "", label: String, onClick: () -> Unit){
        val trackField = remember { field }
        val isOpenField = remember { isOpen }
        if (isOpen.value) {
            val isValid = trackField.value.isNotEmpty() && trackField.value.isNotBlank()
            Dialog(onDismissRequest = {
                isOpenField.value = false
                field.value = ""
            }) {
                Card(
                    modifier = Modifier
                        .padding(10.dp)
                        .background(colorBackgroundCard, RoundedCornerShape(8.dp))
                        .wrapContentSize(Alignment.Center),
                    elevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier
                            .background(colorBackgroundCard)
                            .padding(25.dp)
                            .wrapContentSize(Alignment.Center)
                        ,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        TextInformationEdit(text = titleEdit)
                        MultiTextField(field = field, defaultValue = defaultValue, label = label)
                        Button(
                            enabled = isValid,
                            onClick = onClick,
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = colorResource(id = R.color.colorPrimary),
                                contentColor = colorResource(id = R.color.colorPrimaryDark)
                            ),
                            modifier = Modifier.padding(bottom = 10.dp)
                        ) {
                            Text(text = "ИЗМЕНИТЬ")
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun TextInformationEdit(text: String){
        Text(
            text = text, fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            color = colorTextCard
        )
    }

    @Composable
    fun MultiTextField(field: MutableState<String>, defaultValue: String, label: String){
        Column {
            val trackField = remember { field }
            val focusRequest = remember { FocusRequester() }
            //trackField.value = defaultValue
            OutlinedTextField(
                value = trackField.value,
                onValueChange = {
                    if(16 >= it.length) {
                        val replaceIt = it.replace(" ","")
                        field.value = replaceIt
                    }
                },
                label = { TextWithRed(text = label) },
                leadingIcon = {
                    Image(painter = painterResource(id = R.drawable.ic_account), contentDescription = label)
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    cursorColor = colorResource(id = R.color.colorPrimary),
                    focusedBorderColor = colorResource(id = R.color.colorPrimary),
                    unfocusedBorderColor = colorResource(id = R.color.colorPrimary)),
                textStyle = TextStyle(fontSize = 18.sp),
                //singleLine = true,
                modifier = Modifier
                    .width(260.dp)
                    .padding(bottom = 10.dp)
                    .focusRequester(focusRequest)
            )
            DisposableEffect(Unit){
                focusRequest.requestFocus()
                onDispose {}
            }
        }
    }

    @Preview
    @Composable
    fun NicknameField(){
        Column {
            val nickname = remember { newNickNameField }
            val focusRequest = remember { FocusRequester() }
            OutlinedTextField(
                value = nickname.value,
                onValueChange = {
                    if(16 >= it.length) {
                        val replaceIt = it.replace(" ","")
                        nickname.value = replaceIt
                    }
                },
                label = { TextWithRed(text = "Nickname") },
                leadingIcon = {
                    Image(painter = painterResource(id = R.drawable.ic_account), contentDescription = "Nickname")
                },
                trailingIcon = {
                    Text(text = "${nickname.value.length}/16")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    cursorColor = colorResource(id = R.color.colorPrimary),
                    focusedBorderColor = colorResource(id = R.color.colorPrimary),
                    unfocusedBorderColor = colorResource(id = R.color.colorPrimary)),
                textStyle = TextStyle(fontSize = 18.sp),
                singleLine = true,
                modifier = Modifier
                    .width(260.dp)
                    .padding(bottom = 10.dp)
                    .focusRequester(focusRequest)
            )
            DisposableEffect(Unit){
                focusRequest.requestFocus()
                onDispose {}
            }
        }
    }

    @Composable
    fun OpenEditAccessFavorite(defaultValue: Boolean){
        val isBlocked = remember { mutableStateOf(defaultValue) }
        val textStateAccess = if(isBlocked.value) "Избранные скрыты" else "Избранные открыты"
        Dialog(onDismissRequest = {  }) {
        }

        Card(
                modifier = Modifier
                    .padding(10.dp)
                    .background(colorBackgroundCard, RoundedCornerShape(8.dp)),
                elevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .background(colorBackgroundCard)
                        .padding(25.dp)
                ){
                    Row() {
                        Text(
                            text = textStateAccess, fontSize = 18.sp, fontWeight = FontWeight.Medium,
                            color = colorTextCard
                        )
                        Switch(checked = isBlocked.value, onCheckedChange = {
                            isBlocked.value = it
                        }, colors = SwitchDefaults.colors(checkedThumbColor = colorResource(id = R.color.colorPrimary)))
                    }
                    Button(
                        onClick = {

                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = colorResource(id = R.color.colorPrimary),
                            contentColor = colorResource(id = R.color.colorPrimaryDark)
                        ),
                        modifier = Modifier.padding(bottom = 10.dp)
                    ) {
                        Text(text = "ИЗМЕНИТЬ")
                    }
                }
        }
    }

    @Preview
    @Composable
    fun TopAction(){
        Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = { /*TODO*/ }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(0x00000000))) {
                    Text(text = "РЕДАКТИРОВАТЬ", fontSize = 16.sp, color = Color(0xFFCA5454), fontWeight = FontWeight.Bold)
                }
                TextButton(onClick = {}, colors = ButtonDefaults.buttonColors(backgroundColor = Color(0x00000000))) {
                    Text(text = "ВЫХОД", fontSize = 16.sp, color = Color(0xFFCA5454), fontWeight = FontWeight.Bold)
                }
            }
        }

    @Composable
    fun NotFound(text: String = "Нет данных"){
        Text(text = text, textAlign = TextAlign.Center)
    }
    @Composable
    fun TextProfileCard(text: String){
        Text(text = text, textAlign = TextAlign.Center)
    }

    @Composable
    fun TextWithRed(text: String, modifier: Modifier = Modifier, textSize: TextUnit = TextUnit.Unspecified, fontWeight: FontWeight? = null){
        val start = text.substring(0, text.length-1)
        val end = text[text.length-1]
        Text(text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = colorTextCard)) {
                append(start)
            }
            withStyle(style = SpanStyle(color = Color(0xFFCA5454))){
                append(end)
            }
        }, modifier = modifier, fontSize = textSize, fontWeight = fontWeight)
    }
}