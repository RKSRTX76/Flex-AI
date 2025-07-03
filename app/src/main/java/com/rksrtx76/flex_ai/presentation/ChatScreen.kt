package com.rksrtx76.flex_ai.presentation

import android.Manifest
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.rounded.AttachFile
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.rksrtx76.flex_ai.ChatUiEvents
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.time.delay
import kotlin.contracts.contract


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    modifier : Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    paddingValues : PaddingValues
) {
    val chatViewModel = hiltViewModel<ChatViewModel>()
    val chatState = chatViewModel.chatState.collectAsState().value
    val voiceViewModel = hiltViewModel<VoiceToTextViewModel>()
    val voiceState = voiceViewModel.state.collectAsState().value

    var canRecord by remember { mutableStateOf(false) }

    // permission request
    val recordAudioLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted->
            canRecord = isGranted
        }
    )

    LaunchedEffect(recordAudioLauncher) {
        recordAudioLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }

    // Sync spoken text into prompt
    LaunchedEffect(voiceState.spokenText) {
        if(voiceState.spokenText.isNotEmpty()){
            chatViewModel.onEvent(
                ChatUiEvents.UpdatePrompt(
                    voiceState.spokenText
                )
            )
        }
    }


    val bitmap = getBitmap(chatViewModel)

    // Image Picker
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            chatViewModel.updateImageUri(uri.toString())
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = paddingValues.calculateBottomPadding())
            .background(Color(0xFF1A1A5A))
    ) {
        // Background and blur effect
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
//                            Color(0xFF0E0E34),
                            Color(0xFF1A1A5A),
                            Color(0xFF2B2B8F),
                            Color(0xFF4A4ABF),
//                            Color(0xFF6A6AEF),
                            Color(0xFF2B2B8F),
                            Color(0xFF1A1A5A)
                        ),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
                .blur(28.dp)
        )

        if (chatState.chatList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularBubblePattern()
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 12.dp, end = 12.dp,bottom = 30.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .nestedScroll(scrollBehavior.nestedScrollConnection), // Apply scroll connection
                reverseLayout = true
            ) {
                itemsIndexed(chatState.chatList) { index, chat ->
                    if (chat.isFromUser) {
                        UserChat(
                            prompt = chat.prompt,
                            bitmap = chat.bitmap
                        )
                    } else {
                        if(chat.isLoading){
                            ModelChatLoading()
                        }else{
                            ModelChat(response = chat.prompt)
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White.copy(alpha = 0.10f),
                        shape = RoundedCornerShape(50.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = Icons.Rounded.AttachFile,
                    contentDescription = "Attach",
                    modifier = Modifier
                        .size(40.dp)
                        .padding(8.dp)
                        .clickable {
                            imagePicker.launch(
                                PickVisualMediaRequest
                                    .Builder()
                                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    .build()
                            )
                        },
                    tint = Color.White.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    bitmap?.let {
                        Image(
                            modifier = Modifier
                                .size(45.dp)
                                .padding(bottom = 2.dp)
                                .clip(RoundedCornerShape(6.dp)),
                            contentDescription = "Picked image",
                            contentScale = ContentScale.Crop,
                            bitmap = bitmap.asImageBitmap()
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                BasicTextField(
                    modifier = Modifier.weight(1f).padding(top = 4.dp),
                    value = chatState.prompt,
                    textStyle = TextStyle(fontSize = 16.sp, color = Color.White),
                    cursorBrush = SolidColor(Color.White),
                    onValueChange = {
                        chatViewModel.onEvent(ChatUiEvents.UpdatePrompt(it))
                    },
                    decorationBox = { innerTextField ->
                        if (chatState.prompt.isEmpty()) {
                            Text(
                                text = "Ask me anything",
                                fontSize = 16.sp,
                                style = TextStyle(color = Color.Gray)
                            )
                        }
                        innerTextField()
                    }
                )

                Spacer(modifier = Modifier.width(8.dp))

                // add mic option
                IconButton(
                    onClick = {
                        if(canRecord){
                            if(!voiceState.isSpeaking){
                                voiceViewModel.startListening("en-US")
                            }else{
                                voiceViewModel.stopListening()
                            }
                        }
                    }
                ) {
                    AnimatedContent(targetState = voiceState.isSpeaking) { isSpeaking ->
                        if(isSpeaking){
                            Icon(
                                imageVector = Icons.Rounded.Stop,
                                contentDescription = null
                            )
                        }else{
                            Icon(
                                imageVector = Icons.Rounded.Mic,
                                contentDescription = null
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .size(35.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color.White.copy(alpha = 0.8f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier
                            .size(25.dp)
                            .clickable {
                                chatViewModel.onEvent(ChatUiEvents.SendPrompt(chatState.prompt, bitmap))
                                chatViewModel.uriState.update { "" }
                            },
                        imageVector = Icons.Default.ArrowUpward,
                        contentDescription = "Send prompt",
                        tint = Color.Black.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}


@Composable
fun getBitmap(
    chatViewModel: ChatViewModel
) : Bitmap?{

    val uri = chatViewModel.uriState.collectAsState().value

    // convert to image
    val imageState : AsyncImagePainter.State = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(uri)
            .size(Size.ORIGINAL)
            .build()
    ).state

    if(imageState is AsyncImagePainter.State.Success){
        return imageState.result.drawable.toBitmap()
    }

    return null

}

@Composable
fun UserChat(prompt: String, bitmap: Bitmap?) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd
    ) {
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth(0.75f)
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 6.dp, end = 4.dp, top = 8.dp, bottom = 8.dp),
                horizontalAlignment = Alignment.End
            ) {
                bitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "User Image",
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.LightGray)
                            .aspectRatio(it.width.toFloat() / it.height)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                if(prompt.isNotBlank()){
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.White.copy(alpha = 0.8f))
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = prompt,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ModelChat(response: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .padding(4.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = Color.White.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(12.dp)
        ) {
            Text(
                text = response,
                color = Color.White,
            )
        }
    }
}

@Composable
fun ModelChatLoading() {
    Row(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .padding(4.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = Color.White.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) { index ->
                    BouncingDot(delayMillis = index * 100)
                }
            }
        }
    }
}

@Composable
fun BouncingDot(delayMillis: Int) {
    val offsetY = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        delay(delayMillis.toLong())
        while (true) {
            offsetY.animateTo(
                targetValue = -8f,
                animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
            )
            offsetY.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 300, easing = FastOutLinearInEasing)
            )
        }
    }

    Box(
        modifier = Modifier
            .size(7.dp)
            .offset(y = offsetY.value.dp)
            .background(Color.White, shape = CircleShape)
    )
}





//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun PreviewChatScreen() {
//    MaterialTheme {
//        ChatScreen()
//    }
//}