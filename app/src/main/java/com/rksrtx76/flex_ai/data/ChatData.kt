package com.rksrtx76.flex_ai.data

import android.graphics.Bitmap
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.ResponseStoppedException
import com.google.ai.client.generativeai.type.content
import com.rksrtx76.flex_ai.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ChatData {
    val API_KEY = BuildConfig.API_KEY

    // we do not need retrofit as google did every thing

    // without image
    suspend fun getResponse(prompt : String) : Chat {
        val generativeModel = GenerativeModel(
            modelName = "gemini-1.5-pro",
            apiKey = API_KEY
        )
        try {
            val response = withContext(Dispatchers.IO){
                generativeModel.generateContent(prompt = prompt)
            }
            return Chat(
                prompt = response.text ?: "Error",
                bitmap = null,
                isFromUser = false
            )
        }catch (e : Exception){  // in case prompt violets the rules
            return Chat(
                prompt = e.message ?: "Error",
                bitmap = null,
                isFromUser = false
            )
        }
    }


    // with image
    suspend fun getResponseWithImage(prompt : String, bitmap : Bitmap) : Chat {
        val generativeModel = GenerativeModel(
            modelName = "gemini-1.5-pro",
            apiKey = API_KEY
        )
        try {
            val inputContent = content {
                image(bitmap)
                text(prompt)
            }

            val response = withContext(Dispatchers.IO){
                generativeModel.generateContent(inputContent)
            }
            return Chat(
                prompt = response.text ?: "Error",
                bitmap = null,
                isFromUser = false
            )
        }catch (e : Exception){  // in case prompt violets the rules
            return Chat(
                prompt = e.message ?: "Error",
                bitmap = null,
                isFromUser = false
            )
        }
    }


    // remove * symbols
    private fun removeSymbol(text : String) : String{
        return text.replace("*","")
    }
}