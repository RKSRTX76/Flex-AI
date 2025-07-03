package com.rksrtx76.flex_ai.data

import android.graphics.Bitmap

data class Chat(
    val prompt : String,
    val bitmap: Bitmap?,
    val isFromUser : Boolean,
    val isLoading : Boolean = false
)
