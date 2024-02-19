package com.example.whisper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.whisper.ui.theme.WhisperTheme

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      val navController = rememberNavController()
      WhisperTheme {
        Scaffold(
          modifier = Modifier.fillMaxSize()
        ) { padding ->
          Column {
            SpeechToTextScreen(navController, "Android")
          }
          WhisperNavHost(
            navController = navController,
            modifier = Modifier.padding(padding),
          )
        }
      }
    }
  }
}

@Composable
fun SpeechToTextScreen(navController: NavHostController, name: String, modifier: Modifier = Modifier) {
  Text(
    text = "Hello $name!",
    modifier = modifier
  )

  Button(onClick = { }) {

  }
}

@Preview(showBackground = true)
@Composable
fun SpeechToTextPreview() {
  val navController = rememberNavController()
  WhisperTheme {
    SpeechToTextScreen(navController ,"Android")
  }
}