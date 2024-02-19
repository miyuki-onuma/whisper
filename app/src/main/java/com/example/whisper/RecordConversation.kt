package com.example.whisper

import android.Manifest.permission
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.annotation.RequiresPermission
import com.example.whisper.AudioConfig.Companion.audioFormat
import java.util.LinkedList

class RecordConversation() {
  // AudioRecord用の設定値
  private val audioSource = MediaRecorder.AudioSource.MIC
  private val sampleRate = 16000
  private val channel = AudioFormat.CHANNEL_IN_MONO
  private val bufferSize = AudioRecord.getMinBufferSize(sampleRate, channel, audioFormat)

  // 発話終了までを録音する関数
  @RequiresPermission(permission.RECORD_AUDIO)
  fun capture(): ByteArray {
    // 結果保存用
    val conversationBuffer = LinkedList<Byte>()
    // 録音開始
    val audioRecord = AudioRecord(audioSource, sampleRate, channel, audioFormat, bufferSize)
    audioRecord.startRecording()
    // 一時的な音声データ
    val buffer = ByteArray(bufferSize)
    // 発話終了まで録音
    while (audioRecord.recordingState == AudioRecord.RECORDSTATE_RECORDING) {
      val readSize = audioRecord.read(buffer, 0, AudioConfig.BUFFER_SIZE) ?: continue
      if (readSize > 0) {
        conversationBuffer.addAll(buffer.toList())
        // 発話終了判定を行う
        if (someVADDetectionFunc(conversationBuffer)) {
          break
        }
      }
    }
    return conversationBuffer.toByteArray()
  }

  private fun someVADDetectionFunc(conversationBuffer: LinkedList<Byte>): Boolean {
    return true
  }

}