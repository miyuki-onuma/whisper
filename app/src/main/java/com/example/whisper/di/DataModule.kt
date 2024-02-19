package com.example.whisper.di

import com.example.whisper.SpeechToText
import org.koin.dsl.module

val dataModule = module {
  factory { SpeechToText(get()) }
}