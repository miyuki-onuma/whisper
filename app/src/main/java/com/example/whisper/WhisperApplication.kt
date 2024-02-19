package com.example.whisper

import android.app.Application
import com.example.whisper.di.appModule
import com.example.whisper.di.dataModule
import com.example.whisper.di.netModule
import com.example.whisper.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class WhisperApplication: Application() {

  override fun onCreate() {
    super.onCreate()

    startKoin {
      printLogger()
      androidContext(this@WhisperApplication)
      modules(listOf(appModule, viewModelModule, dataModule, netModule))
    }
  }
}