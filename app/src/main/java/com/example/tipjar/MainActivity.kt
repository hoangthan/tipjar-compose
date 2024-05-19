package com.example.tipjar

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tipjar.ui.history.HistoryScreen
import com.example.tipjar.ui.history.HistoryViewModel
import com.example.tipjar.ui.home.HomeScreen
import com.example.tipjar.ui.home.HomeViewModel
import com.example.tipjar.ui.theme.TipJarTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TipJarTheme {
                /*val homeViewModel: HomeViewModel = hiltViewModel()
                val state by homeViewModel.state.collectAsState()
                HomeScreen(state, homeViewModel::dispatchEvent)*/

                val historyViewModel: HistoryViewModel = hiltViewModel()
                val state by historyViewModel.viewState.collectAsState()
                HistoryScreen(state, historyViewModel::dispatchViewEvent)
            }
        }
    }
}
