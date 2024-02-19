package com.example.whisper

import kotlinx.serialization.Serializable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface OpenAiService {
  @Multipart
  @Headers("Authorization: Bearer ${BuildConfig.OPENAI_API_KEY}")
  @POST("v1/audio/transcriptions")
  suspend fun transcribeAudioFile(
    @Part file: MultipartBody.Part,
    @Part("model") model: RequestBody,
  ): TranscriptionResponse

  // レスポンスのモデルを定義
  @Serializable
  data class TranscriptionResponse(
    val text: String,
  )
}
//
// class OpenApiService: OpenAiService {
//   override suspend fun transcribeAudioFile(
//     file: MultipartBody.Part,
//     model: RequestBody
//   ): TranscriptionResponse {
//       val baseUrl = "https://api.openai.com/"
//
//       // APIキーとリクエストのファイルパスを設定
//       val filePath = "/path/to/audio/file.wav"
//
//       // HTTPクライアントを作成
//       val client = OkHttpClient()
//
//       // Retrofitクライアントを作成
//       val retrofit = Retrofit.Builder()
//         .baseUrl(baseUrl)
//         .client(client)
//         .addConverterFactory(MoshiConverterFactory.create())
//         .build()
//
//       // OpenAiServiceを取得
//       val openAiService = retrofit.create(OpenAiService::class.java)
//
//       // // 音声ファイルをリクエストボディとして作成
//       // val fileRequestBody = MultipartBody.Part.createFormData(
//       //   "file",
//       //   filePath,
//       //   "audio/wav".toMediaType(),
//       //   File(filePath).asRequestBody()
//       // )
//
//       // リクエストボディのモデルを作成
//       val modelRequestBody = mapOf(
//         "model" to "davinci-codex".toRequestBody()
//       )
//
//       // リクエストを送信
//       val response = openAiService.transcribeAudioFile(model, modelRequestBody)
//
//       // レスポンスを取得
//       val transcript = response.text
//
//       // レスポンスを表示
//       println(transcript)
//
//     return TranscriptionResponse(transcript)
//   }
// }
