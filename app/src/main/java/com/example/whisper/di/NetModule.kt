package com.example.whisper.di

import android.content.Context
import com.example.whisper.BuildConfig
import com.example.whisper.OpenAiService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Authenticator
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object NetModule {
  const val NAME_CACHE_INTERCEPTOR = "cache"
  const val NAME_HEADER_INTERCEPTOR = "header"
  const val NAME_CHUCK_INTERCEPTOR = "chuck"
  const val NAME_AUTHENTICATE_INTERCEPTOR = "authentication"
}

fun createOkHttpClient(
  context: Context,
  cacheInterceptor: Interceptor,
  headerInterceptor: Interceptor? = null,
  authenticationInterceptor: Authenticator,
  chuckInterceptor: Interceptor
): OkHttpClient {
  val timeout = 60L
  val cache = Cache(File(context.cacheDir, "http-cache"), 10 * 1024 * 1024) // 10M cache
  val okHttpBuilder = OkHttpClient.Builder()
    .cache(cache)
    .addInterceptor(cacheInterceptor)
    .addInterceptor(chuckInterceptor)
    .callTimeout(timeout, TimeUnit.SECONDS)
    .connectTimeout(timeout, TimeUnit.SECONDS)
    .readTimeout(timeout, TimeUnit.SECONDS)
    .writeTimeout(timeout, TimeUnit.SECONDS)
    .retryOnConnectionFailure(true)
  headerInterceptor?.let { okHttpBuilder.addInterceptor(it) }

  return okHttpBuilder.build()
}

val netModule = module {
  single {
    createOkHttpClient(
      get(),
      get(named(NetModule.NAME_CACHE_INTERCEPTOR)),
      get(named(NetModule.NAME_HEADER_INTERCEPTOR)),
      get(named(NetModule.NAME_AUTHENTICATE_INTERCEPTOR)),
      get(named(NetModule.NAME_CHUCK_INTERCEPTOR))
    )
  }

  single<OpenAiService> {
    Retrofit.Builder()
      .baseUrl(BuildConfig.OPENAI_API_KEY)
      .client(get())
      .addConverterFactory(MoshiConverterFactory.create(get()))
      .addCallAdapterFactory(CoroutineCallAdapterFactory())
      .build().create(OpenAiService::class.java)
  }
}