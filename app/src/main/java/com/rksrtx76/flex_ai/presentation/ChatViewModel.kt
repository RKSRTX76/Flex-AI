package com.rksrtx76.flex_ai.presentation

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rksrtx76.flex_ai.ChatUiEvents
import com.rksrtx76.flex_ai.data.Chat
import com.rksrtx76.flex_ai.data.ChatData
import com.rksrtx76.flex_ai.data.ChatState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {

    private val _chatState =  MutableStateFlow(ChatState())
    val chatState = _chatState.asStateFlow()

    val uriState = MutableStateFlow("")



    // Trigger some event
    fun onEvent(event : ChatUiEvents){
        when(event){
            is ChatUiEvents.SendPrompt -> {
                if(event.prompt.isNotEmpty()){
                    // add prompt to chat/ events
                    addPrompt(event.prompt, event.bitmap)
                    // get response from api
                    if(event.bitmap != null){
                        getResponseWithImage(event.prompt, event.bitmap)
                    }else{
                        getResponse(event.prompt)
                    }
                }
            }
            is ChatUiEvents.UpdatePrompt -> {
                _chatState.update {
                    it.copy(
                        prompt = event.newPrompt
                    )
                }
            }
        }
    }

    fun updateImageUri(uri : String){
        uriState.update {
            uri
        }
    }

    private fun addPrompt(prompt :String, bitmap:Bitmap?){
        // update chat state
        _chatState.update {
            it.copy(
                chatList = it.chatList.toMutableList().apply {
                    add(0, Chat(prompt, bitmap, true))
                },
                prompt = "",  // clear prompt to display textField empty, do not worry we stored that prompt in events
                bitmap = null  // clear bitmap to display image empty
            )
        }
    }

    private fun getResponse(prompt : String){
        viewModelScope.launch {
            val chat = ChatData.getResponse(prompt)
             _chatState.update {
                 it.copy(
                     chatList = it.chatList.toMutableList().apply {
                         add(0, chat)
                     }
                 )
             }
        }
    }

    private fun getResponseWithImage(prompt : String, bitmap : Bitmap){
        viewModelScope.launch {
            val chat = ChatData.getResponseWithImage(prompt, bitmap)
            _chatState.update {
                it.copy(
                    chatList = it.chatList.toMutableList().apply {
                        add(0, chat)
                    }
                )
            }
        }
    }


}