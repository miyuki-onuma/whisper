package com.example.whisper

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.koin.compose.koinInject

object SpeechToTextScreen : Destination {
  override val route = "SpeechToText"
}

interface Destination {
  val route: String
}

@Composable
fun WhisperNavHost(
  navController: NavHostController,
  modifier: Modifier = Modifier,
) {
  val viewModel: MainViewModel = koinInject()
  NavHost(
    navController = navController,
    startDestination = SpeechToTextScreen.route,
    modifier = modifier
  ) {
    composable(route = SpeechToTextScreen.route) {
      SpeechToTextScreen(navController = navController, name = "Android")
    }
  }
}