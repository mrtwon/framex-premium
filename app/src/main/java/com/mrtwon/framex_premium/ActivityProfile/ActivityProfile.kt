package com.mrtwon.framex_premium.ActivityProfile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.*
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.ViewModelProvider
import coil.compose.rememberImagePainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mrtwon.framex_premium.ActivityAuth.AuthActivity
import com.mrtwon.framex_premium.Helper.DetailsError
import com.mrtwon.framex_premium.Helper.TYPE_ERROR
import com.mrtwon.framex_premium.R
import com.mrtwon.framex_premium.retrofit.framexAuth.ResponseItem

class ActivityProfile: AppCompatActivity() {
    private val vm: ProfileViewModel by lazy { ViewModelProvider(this).get(ProfileViewModel::class.java) }
    private val colorBackgroundCard = Color(0xFFD1D1D1)
    private val colorTextCard = Color(0xFF4D4D4D)
    private val newNickNameField = mutableStateOf("")
    private val newFavoriteContent = mutableStateOf("")
    private val newAboutMe = mutableStateOf("")
    private val newAccessFavorite = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        setContent {
            MainLayout()
            OpenPreDialogError(field = vm.preErrorState)
            OpenPreProgressDialog(field = vm.preLoadState)
            OpenProgressDialog(field = vm.loadLiveData)
            OpenDialogError(field = vm.errorLiveData)
            OpenEditNickName()
            OpenEditAboutMe()
            OpenEditFavorite()
            OpenEditAccessFavorite()
            OpenHelpDialog()
        }
        observeAuthStatus()
        observerConfirmEdit()
        vm.firstGiveProfile()
        super.onCreate(savedInstanceState)
    }
    fun log(s: String){
        Log.i("self-profile", s)
    }

    private fun observeAuthStatus(){
        vm.isAuth.observe(this){
            if(it){
                vm.giveProfile()
            }else{
                startActivity(Intent(this, AuthActivity::class.java))
            }
        }
    }
    private fun observerConfirmEdit(){
        vm.confirmEditLiveData.observe(this){
            if(it != null && it){
                Toast.makeText(this, "Успешно", Toast.LENGTH_LONG).show()
                vm.confirmEditLiveData.value = null
            }
        }
    }
    @Preview
    @Composable
    fun MainLayout() {
        val isRefreshing by vm.isRefreshing.collectAsState()
        val profile = remember { vm.profileLiveData }
        profile.value?.response?.get(0)?.let { responseItem ->
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
            onRefresh = { vm.refresh() }
        ) {
            Column(modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
            ) {
                Column {
                    Column(modifier = Modifier.padding(bottom = 10.dp)) {
                        TopAction()
                    }
                    Column(modifier = Modifier.padding(bottom = 50.dp)) {
                        Profile(responseItem)
                    }
                    Column(modifier = Modifier.padding(bottom = 10.dp)) {
                        FavoriteContent(responseItem)
                    }
                    Column(modifier = Modifier.padding(bottom = 20.dp)) {
                        AboutMe(responseItem)
                    }
                    Column(modifier = Modifier.padding(bottom = 10.dp)) {
                        FavoriteList(responseItem)
                        }
                    }
                }
            }
         }
    }

    @Composable
    fun Profile(responseItem: ResponseItem) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .padding(start = 20.dp, end = 20.dp)
                    .background(colorBackgroundCard, shape = RoundedCornerShape(8.dp)),
                elevation = 12.dp
            )
            {
                Row(modifier = Modifier.background(colorBackgroundCard)) {
                    Row(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(start = 20.dp), verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = rememberImagePainter(data = responseItem.photo),
                            contentDescription = "avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(84.dp)
                                .clip(CircleShape)
                                .border(2.dp, Color.Yellow, CircleShape)
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onLongPress = {
                                            startActivity(
                                                Intent(
                                                    this@ActivityProfile,
                                                    UpdatePhotoActivity::class.java
                                                )
                                            )
                                        }
                                    )
                                }
                        )
                        Column(
                            modifier = Modifier.padding(start = 10.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Column(modifier = Modifier.pointerInput(Unit) {
                                detectTapGestures(
                                    onLongPress = {
                                        vm.isOpenEditNickName.value = true
                                    }
                                )
                            }) {
                                TextWithRed(text = responseItem.nickname, textSize = 18.sp, fontWeight = FontWeight.Bold)
                            }
                            Column(modifier = Modifier.padding(top = 10.dp)) {
                                TextWithRed(text = responseItem.userType, textSize = 15.sp)
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
                        if(responseItem.isDonate) {
                            StatusDonate()
                        }
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

    @Composable
    fun FavoriteContent(responseItem: ResponseItem) {
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
                                    vm.isOpenEditFavoriteContent.value = true
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
                        TextWithRed(
                            text = "Любимый контент",
                            textSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                        val resultText = if(responseItem.favoriteContent.isEmpty()) "Нет данных." else responseItem.favoriteContent
                        Text(text = resultText, textAlign = TextAlign.Center, modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 5.dp))
                    }
                }
            }
        }

    @Composable
    fun AboutMe(responseItem: ResponseItem) {
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
                                    vm.isOpenEditAboutMe.value = true
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
                        TextWithRed(
                            text = "О себе",
                            textSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val resultText = if(responseItem.about.isEmpty()) "Нет данных." else responseItem.about
                        Text(text = resultText, textAlign = TextAlign.Center, modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 5.dp))
                    }
                }
            }
        }

    @Composable
    fun FavoriteList(responseItem: ResponseItem) {
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
                        .padding(bottom = 10.dp, top = 10.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onLongPress = {
                                    vm.isOpenEditAccessFavorite.value = true
                                }
                            )
                        }
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TextWithRed(
                            text = "Избранные пользователя",
                            textSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        NotFound(text = "🔒 Информация скрыта пользователем")
                    }
                }
            }
        }

    @Preview
    @Composable
    fun OpenEditNickName() {
        val nickname = remember { newNickNameField }
        val isOpen = remember { vm.isOpenEditNickName }
        if (isOpen.value) {
            val isValid = nickname.value.isNotEmpty() && nickname.value.isNotBlank()
            Dialog(onDismissRequest = {
                vm.isOpenEditNickName.value = false
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
                            onClick = { vm.createNickName( newNickNameField.value )},
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
    fun OpenEditFavorite(){
        val trackField = remember { newFavoriteContent }
        val isOpenField = remember { vm.isOpenEditFavoriteContent }
        var availableSymbol = 200 - trackField.value.length
        if(0 > availableSymbol ) availableSymbol = 0

        if (isOpenField.value) {
            val isValid = trackField.value.isNotEmpty() && trackField.value.isNotBlank()
            Dialog(onDismissRequest = {
                isOpenField.value = false
                trackField.value = ""
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
                        TextInformationEdit(text = "Ваш любимый контент.\nОграничение в 200 символов. Доступно $availableSymbol.")
                        FavoriteTextField(defaultValue = vm.profileLiveData.value?.response?.get(0)?.favoriteContent ?: "")
                        Button(
                            enabled = isValid,
                            onClick = {
                                vm.createFavorite(newFavoriteContent.value)
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
        }
    }

    @Composable
    fun OpenEditAboutMe(){
        val trackField = remember { newAboutMe }
        val isOpenField = remember { vm.isOpenEditAboutMe }
        var avaliableSymbol = 200 - trackField.value.length
        if(0 > avaliableSymbol) avaliableSymbol = 0
        if (isOpenField.value) {
            val isValid = trackField.value.isNotEmpty() && trackField.value.isNotBlank()
            Dialog(onDismissRequest = {
                isOpenField.value = false
                trackField.value = ""
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
                        TextInformationEdit(text = "Расскажите немного о себе\nОграничение в 200 символов. Осталось $avaliableSymbol.")
                        AboutTextField(defaultValue = vm.profileLiveData.value?.response?.get(0)?.about ?: "")
                        Button(
                            enabled = isValid,
                            onClick = {
                                      vm.createAboutMe(newAboutMe.value)
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
        }
    }

    /*@Composable
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
    }*/

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
    fun AboutTextField(defaultValue: String){
        Column {
            val trackField = remember { newAboutMe }
            val focusRequest = remember { FocusRequester() }

            LaunchedEffect(key1 = Unit, block = {
                trackField.value = defaultValue
            })
            OutlinedTextField(
                value = trackField.value,
                onValueChange = {
                    newAboutMe.value = it
                },
                label = { TextWithRed(text = "Расскажите немного о себе") },
                leadingIcon = {
                    Image(painter = painterResource(id = R.drawable.ic_account), contentDescription = "О себе")
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    cursorColor = colorResource(id = R.color.colorPrimary),
                    focusedBorderColor = colorResource(id = R.color.colorPrimary),
                    unfocusedBorderColor = colorResource(id = R.color.colorPrimary)),
                textStyle = TextStyle(fontSize = 18.sp),
                //singleLine = true,
                modifier = Modifier
                    .width(280.dp)
                    .height(200.dp)
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
    fun FavoriteTextField(defaultValue: String){
        Column {
            val trackField = remember { newFavoriteContent }
            val focusRequest = remember { FocusRequester() }
            //trackField.value = defaultValue
            LaunchedEffect(key1 = Unit, block = {
                trackField.value = defaultValue
            })
            OutlinedTextField(
                value = trackField.value,
                onValueChange = {
                    newFavoriteContent.value = it
                },
                label = { TextWithRed(text = "Любимый контент") },
                leadingIcon = {
                    Image(painter = painterResource(id = R.drawable.ic_favorite_gray), contentDescription = "Любимый контент")
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    cursorColor = colorResource(id = R.color.colorPrimary),
                    focusedBorderColor = colorResource(id = R.color.colorPrimary),
                    unfocusedBorderColor = colorResource(id = R.color.colorPrimary)),
                textStyle = TextStyle(fontSize = 18.sp),
                //singleLine = true,
                modifier = Modifier
                    .width(280.dp)
                    .height(200.dp)
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
    fun MultiTextField(field: MutableState<String>, defaultValue: String, label: String){
        Column {
            val trackField = remember { field }
            val focusRequest = remember { FocusRequester() }
            //trackField.value = defaultValue
            LaunchedEffect(key1 = Unit, block = {
                trackField.value = defaultValue
            })
            OutlinedTextField(
                value = trackField.value,
                onValueChange = {
                    field.value = it
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
                    .width(250.dp)
                    .height(150.dp)
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
            LaunchedEffect(key1 = Unit, block = {
                nickname.value = vm.profileLiveData.value?.response?.get(0)?.nickname ?: ""
            })
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
    fun OpenEditAccessFavorite() {
        log("recomposition")
        val isOpen = remember { vm.isOpenEditAccessFavorite }
        if (isOpen.value) {
            Dialog(onDismissRequest = {
                vm.isOpenEditAccessFavorite.value = false
                newAccessFavorite.value = false
               }
            ) {
                val isBlocked = remember { newAccessFavorite }
                val textStateAccess = if (isBlocked.value) "Избранные открыты" else "Избранные скрыты"
                LaunchedEffect(key1 = Unit, block = {
                    newAccessFavorite.value = vm.profileLiveData.value?.response?.get(0)?.openFavorite ?: false
                    log("launchedEffect, data: ${vm.profileLiveData.value?.response?.get(0)?.openFavorite}")
                })
                Card(
                    modifier = Modifier
                        .padding(10.dp)
                        .background(colorBackgroundCard, RoundedCornerShape(8.dp)),
                    elevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier
                            .background(colorBackgroundCard)
                            .padding(start = 25.dp, end = 25.dp, top = 10.dp, bottom = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Row {
                            Text(
                                text = textStateAccess,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = colorTextCard,
                                modifier = Modifier.padding(end = 10.dp)
                            )
                            Switch(
                                checked = isBlocked.value,
                                onCheckedChange = {
                                    newAccessFavorite.value = it
                                },
                                colors = SwitchDefaults.colors(checkedThumbColor = colorResource(id = R.color.colorPrimary))
                            )
                        }
                        Column(verticalArrangement = Arrangement.Bottom) {
                            Button(
                                onClick = {
                                    vm.createOpenFavorite(newAccessFavorite.value)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = colorResource(id = R.color.colorPrimary),
                                    contentColor = colorResource(id = R.color.colorPrimaryDark)
                                ),
                                modifier = Modifier.padding(top = 10.dp)
                            ) {
                                Text(text = "ИЗМЕНИТЬ")
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun OpenProgressDialog(field: MutableState<Boolean>){
        val isOpen = remember { field }
        if(isOpen.value){
            Dialog(
                onDismissRequest = { field.value = false },
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
    fun OpenPreProgressDialog(field: MutableState<Boolean>){
        val isOpen = remember { field }
        if(isOpen.value){
            Dialog(
                onDismissRequest = {
                    field.value = false
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
    fun OpenPreDialogError(field: MutableState<DetailsError?>){
        val mError = remember { field }
        if (mError.value != null) {
            mError.value?.let {
                AlertDialog(
                    onDismissRequest = {
                        vm.errorLiveData.value = null
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
                                vm.errorLiveData.value = null
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
    fun OpenDialogError(field: MutableState<DetailsError?>) {
        val mError = remember { field }
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
    fun OpenHelpDialog(){
        val isOpen = remember { vm.isOpenHelpDialog }
        if(isOpen.value) {
            Dialog(onDismissRequest = { isOpen.value = false }) {
                Column(modifier = Modifier
                    .width(300.dp)
                    .background(colorBackgroundCard, RoundedCornerShape(10.dp)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
                ) {
                        Text(
                            text = "Для редактирования удержите элемент",
                            fontSize = 16.sp,
                            color = colorTextCard,
                            modifier = Modifier
                                .padding(start = 10.dp, end = 10.dp, top = 10.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Button(
                            modifier = Modifier.padding(5.dp),
                            onClick = { isOpen.value = false },
                            colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(R.color.colorPrimary))
                        ) {
                            Text(text = "Понятно", fontSize = 16.sp, color = colorResource(id = R.color.colorPrimaryDark))
                        }
                    }
                }
            }
        }


    @Preview
    @Composable
    fun TopAction() {
        val profile = remember { vm.profileLiveData }
        profile.value?.let {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = { vm.isOpenHelpDialog.value = true },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0x00000000))
                ) {
                    Text(
                        text = "РЕДАКТИРОВАТЬ",
                        fontSize = 16.sp,
                        color = Color(0xFFCA5454),
                        fontWeight = FontWeight.Bold
                    )
                }
                TextButton(
                    onClick = { vm.logout() },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0x00000000))
                ) {
                    Text(
                        text = "ВЫХОД",
                        fontSize = 16.sp,
                        color = Color(0xFFCA5454),
                        fontWeight = FontWeight.Bold
                    )
                }
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
    fun TextWithRed(text: String, modifier: Modifier = Modifier, textSize: TextUnit = TextUnit.Unspecified, fontWeight: FontWeight? = null) {
        val start = text.substring(0, text.length - 1)
        val end = text[text.length - 1]
        Text(text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = colorTextCard)) {
                append(start)
            }
            withStyle(style = SpanStyle(color = Color(0xFFCA5454))) {
                append(end)
            }
        }, modifier = modifier, fontSize = textSize, fontWeight = fontWeight)
    }
}