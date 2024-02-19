package com.example.whisper

import android.media.AudioFormat

class AudioConfig {
  companion object {
    const val BUFFER_SIZE = 456000
    const val MIME_TYPE = "audio/mpeg"
    const val audioFormat = AudioFormat.ENCODING_PCM_16BIT
  }
}