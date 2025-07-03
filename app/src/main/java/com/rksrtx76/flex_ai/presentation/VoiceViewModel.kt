package com.rksrtx76.flex_ai.presentation

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.rksrtx76.flex_ai.data.VoiceToTextState
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class VoiceToTextViewModel(application: Application) : AndroidViewModel(application),
    RecognitionListener {

    // Needed to create SpeechRecognizer
    private val context = application

    private val _state = MutableStateFlow(VoiceToTextState())
    val state: StateFlow<VoiceToTextState> = _state.asStateFlow()

    private val recognizer = SpeechRecognizer.createSpeechRecognizer(context)

    fun startListening(languageCode: String = "en-US") {
        // Reset previous state
        _state.value = VoiceToTextState()

        // If not available, show error
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            _state.value = _state.value.copy(error = "Speech recognition not available")
            return
        }

        // Prepare speech recognition intent
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageCode)
        }

        // Set the listener and start listening
        recognizer.setRecognitionListener(this)
        recognizer.startListening(intent)

        // Update state to show "Speaking"
        _state.value = _state.value.copy(isSpeaking = true)
    }

    // Stop listening when user is done
    fun stopListening() {
        recognizer.stopListening()
        _state.value = _state.value.copy(isSpeaking = false)
    }

    // --- RecognitionListener functions ---

    override fun onReadyForSpeech(params: Bundle?) {
        _state.value = _state.value.copy(error = null) // Clear previous error
    }

    override fun onEndOfSpeech() {
        _state.value = _state.value.copy(isSpeaking = false) // Done speaking
    }

    override fun onError(error: Int) {
        if (error != SpeechRecognizer.ERROR_CLIENT) {
            _state.value = _state.value.copy(error = "Error code: $error")
        }
    }

    override fun onResults(results: Bundle?) {
        val spokenText = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull()
        if (spokenText != null) {
            _state.value = _state.value.copy(spokenText = spokenText)
        }
    }

    // The rest can be left empty
    override fun onPartialResults(partialResults: Bundle?) {}
    override fun onEvent(eventType: Int, params: Bundle?) {}
    override fun onBeginningOfSpeech() {}
    override fun onRmsChanged(rmsdB: Float) {}
    override fun onBufferReceived(buffer: ByteArray?) {}

    override fun onCleared() {
        super.onCleared()
        recognizer.destroy() // Clean up
    }
}