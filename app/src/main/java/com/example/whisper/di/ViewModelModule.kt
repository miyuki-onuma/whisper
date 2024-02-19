package com.example.whisper.di

import com.example.whisper.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
  viewModel {
    MainViewModel(get())
  }
}