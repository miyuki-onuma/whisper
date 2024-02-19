package com.example.whisper

import androidx.lifecycle.ViewModel

class MainViewModel(
  private val speechToText: SpeechToText
): ViewModel() {

  suspend fun speechToText(): String {
    return speechToText.speechToText("/path/to/audio/file.wav")
  }
}