package com.example.whisper.di

import com.example.whisper.WhisperApplication
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {
  single { androidApplication() as WhisperApplication }
}