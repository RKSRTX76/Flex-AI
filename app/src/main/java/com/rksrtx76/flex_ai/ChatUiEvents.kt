package com.rksrtx76.flex_ai

import android.graphics.Bitmap

sealed class ChatUiEvents {
    data class UpdatePrompt(val newPrompt : String) : ChatUiEvents()
    data class SendPrompt(val prompt : String, val bitmap: Bitmap?) : ChatUiEvents()
}