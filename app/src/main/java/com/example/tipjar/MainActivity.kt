package com.example.tipjar

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.tipjar.ui.home.CaptureImageFromCamera
import com.example.tipjar.ui.home.HomeScreen
import com.example.tipjar.ui.theme.TipJarTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TipJarTheme {
                CaptureImageFromCamera{
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewHomeScreen() {
    TipJarTheme {
        HomeScreen()
    }
}
