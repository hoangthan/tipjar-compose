package com.example.tipjar

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tipjar.ui.TipJarScreen
import com.example.tipjar.ui.history.HistoryScreen
import com.example.tipjar.ui.history.HistoryViewModel
import com.example.tipjar.ui.home.CaptureImageFromCamera
import com.example.tipjar.ui.home.HomeScreen
import com.example.tipjar.ui.home.HomeViewModel
import com.example.tipjar.ui.home.HomeViewModel.HomeViewEvent
import com.example.tipjar.ui.theme.TipJarTheme
import com.example.tipjar.ui.utils.CollectDataResult
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TipJarTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = TipJarScreen.Home) {

                    composable<TipJarScreen.Home> {
                        val viewModel: HomeViewModel = hiltViewModel()
                        val state by viewModel.state.collectAsStateWithLifecycle()
                        val viewEffect by viewModel.viewEffect.collectAsStateWithLifecycle(null)

                        navController.CollectDataResult<String>(TipJarScreen.Camera.KEY_IMAGE_PATH) {
                            viewModel.dispatchEvent(HomeViewEvent.SaveBillImage(it))
                        }

                        LaunchedEffect(viewEffect) {
                            when (viewEffect) {
                                HomeViewModel.HomeViewEffect.LaunchCamera -> {
                                    navController.navigate(TipJarScreen.Camera)
                                }

                                HomeViewModel.HomeViewEffect.NavigateToHistory -> {
                                    navController.navigate(TipJarScreen.History)
                                }

                                else -> {
                                    // Do nothing
                                }
                            }
                        }

                        HomeScreen(state, viewModel::dispatchEvent)
                    }

                    composable<TipJarScreen.Camera> {
                        CaptureImageFromCamera(
                            onFinish = {
                                navController.previousBackStackEntry?.savedStateHandle?.set(
                                    TipJarScreen.Camera.KEY_IMAGE_PATH, it
                                )
                                navController.popBackStack()
                            }
                        )
                    }

                    composable<TipJarScreen.History> {
                        val historyViewModel: HistoryViewModel = hiltViewModel()
                        val state by historyViewModel.viewState.collectAsState()

                        HistoryScreen(state, historyViewModel::dispatchViewEvent) {
                            navController.popBackStack()
                        }
                    }
                }
            }
        }
    }
}
