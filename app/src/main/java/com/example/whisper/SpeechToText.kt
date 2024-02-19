package com.example.whisper

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class SpeechToText constructor(
  private val openAiService: OpenAiService,
) {
  suspend fun speechToText(audioFilePath: String): String {
    val audioFile = File(audioFilePath)
    val mediaType = "mp4".toMediaTypeOrNull()
    val requestBody = audioFile.asRequestBody(mediaType)
    val part = MultipartBody.Part.createFormData("file", audioFile.name, requestBody)
    val model = "whisper-1".toRequestBody("text/plain".toMediaTypeOrNull())

    return openAiService.transcribeAudioFile(part, model).text
  }
}