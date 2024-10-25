package com.rksrtx76.flex_ai.data

import android.graphics.Bitmap

data class ChatState(
    val chatList : MutableList<Chat> = mutableListOf(),
    val prompt : String = "",
    val bitmap : Bitmap? = null,
)
