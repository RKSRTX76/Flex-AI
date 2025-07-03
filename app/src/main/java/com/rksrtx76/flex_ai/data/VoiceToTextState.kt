package com.rksrtx76.flex_ai.data

data class VoiceToTextState(
    val isSpeaking : Boolean = false,
    val spokenText : String = "",
    val error : String? = null
)
