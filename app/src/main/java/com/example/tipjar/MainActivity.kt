package com.example.tipjar

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tipjar.ui.home.HomeScreen
import com.example.tipjar.ui.home.HomeScreenViewModel
import com.example.tipjar.ui.theme.TipJarTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TipJarTheme {
                val homeViewModel: HomeScreenViewModel = hiltViewModel()
                val state by homeViewModel.state.collectAsState()
                HomeScreen(state, homeViewModel::dispatchEvent)
            }
        }
    }
}
