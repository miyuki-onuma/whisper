package com.example.whisper

import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaFormat
import android.media.MediaMuxer
import com.example.whisper.AudioConfig.Companion.audioFormat
import java.lang.StrictMath.min
import java.nio.file.Path
import kotlin.io.path.createTempFile
import kotlin.io.path.pathString

class AudioFileConverter() {
  // MediaCodec用の設定値
  private val mimeType = MediaFormat.MIMETYPE_AUDIO_AAC
  private val sampleRate = 16000
  private val bitRate = 16000
  private val channelCount = 1 // mono = 1, stereo = 2
  private val waitBufferTimeout = 5000L // encode処理のタイムアウト時間
  private val mediaFormat: MediaFormat = MediaFormat.createAudioFormat(mimeType, sampleRate, channelCount)
    .apply {
      setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC)
      setInteger(MediaFormat.KEY_BIT_RATE, bitRate)
    }
  // MediaCodecのインスタンスを生成
  private val mediaCodec: MediaCodec = MediaCodec.createEncoderByType(AudioConfig.MIME_TYPE)
    .apply {
      configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
      start()
    }
  // MediaCodecの保有するbufferステータス確認用
  private val bufferInfo: MediaCodec.BufferInfo = MediaCodec.BufferInfo()
  // MediaMuxer用の設定値
  private val outputFilePath = createTempFile(suffix=".mp4")
  private val mediaOutputFormat = mediaCodec.outputFormat
  // MediaMuxerのインスタンスを生成
  private val mediaMuxer =  MediaMuxer(outputFilePath.pathString, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)
    .apply {
      addTrack(mediaOutputFormat)
      start()
    }

  // 録音データからmp4ファイルにする関数
  fun convertToMP4(conversationBuffer: ByteArray): Path {
    var inputIndex = 0
    // すべての音声データの圧縮・保存を行う
    while (inputIndex < conversationBuffer.size) {
      // MediaCodecを使ってAACフォーマットで圧縮
      inputIndex += feedDataToEncoder(conversationBuffer, inputIndex)
      // MediaMuxerでmp4コンテナに詰める
      writeEncodedDataToContainer()
    }
    return outputFilePath
  }

  private fun feedDataToEncoder(inputAudioData: ByteArray, inputIndex: Int): Int {
    // 空いてるCodecのbufferのindexを取得
    val inputBufferIndex = mediaCodec.dequeueInputBuffer(waitBufferTimeout)
    // bufferのindexが正常だった場合に処理を行う
    if (inputBufferIndex >= 0) {
      // buffer自体を取得
      val inputBuffer = mediaCodec.getInputBuffer(inputBufferIndex) ?: return 0
      // bufferの限界サイズまで音声データを詰める
      val remainDataSize = inputAudioData.size - inputIndex
      val feededDataSize = min(inputBuffer.limit(), remainDataSize)
      // MediaCodecのbufferに音声データを入力できる場合に処理を行う
      if (feededDataSize > 0) {
        // MediaCodecのbufferに音声データを入力する
        inputBuffer.put(inputAudioData, inputIndex, feededDataSize)
        // データ全体での再生時の時刻を計算
        val presentationTimeUs = (1000000.0 * (inputIndex / audioFormat) / sampleRate).toLong()
        // encode処理を行う
        mediaCodec.queueInputBuffer(inputBufferIndex, 0, feededDataSize, presentationTimeUs, 0)
      }
      return feededDataSize
    }
    return 0
  }

  private fun writeEncodedDataToContainer() {
    // Codecからencode済みのbufferのindexを取得
    var outputBufferIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, waitBufferTimeout)
    // encode済みのデータがある限りMediaMuxerを使ってコンテナに詰める
    while (outputBufferIndex != MediaCodec.INFO_TRY_AGAIN_LATER) {
      // encode済みのデータを取得
      val encodedData = mediaCodec.getOutputBuffer(outputBufferIndex) ?: break
      // MediaMuxerにencode済みデータを渡してコンテナに詰めてもらう
      encodedData.position(bufferInfo.offset)
      encodedData.limit(bufferInfo.offset + bufferInfo.size)
      mediaMuxer.writeSampleData(0, encodedData, bufferInfo)
      // コンテナへ詰め終わったbufferを廃棄
      mediaCodec.releaseOutputBuffer(outputBufferIndex, false)
      // 次にencode済みbufferのindexを取得
      outputBufferIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, waitBufferTimeout)
    }
  }
}