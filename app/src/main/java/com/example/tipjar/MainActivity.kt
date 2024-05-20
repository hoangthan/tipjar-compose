package com.example.tipjar

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tipjar.ui.home.CaptureImageFromCamera
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
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "home") {
                    composable(route = "home") {
                        val viewModel: HomeViewModel = hiltViewModel()
                        val state by viewModel.state.collectAsStateWithLifecycle()
                        val viewEffect by viewModel.viewEffect.collectAsStateWithLifecycle(
                            initialValue = null
                        )

                        val imgPath = navController.currentBackStackEntry?.savedStateHandle?.get<String>("imgPath")
                        viewModel.dispatchEvent(HomeViewModel.HomeViewEvent.SetBillImage(imgPath))

                        LaunchedEffect(viewEffect) {
                            if (viewEffect is HomeViewModel.HomeViewEffect.LaunchCamera) {
                                navController.navigate("camera")
                            }
                        }

                        HomeScreen(state, viewModel::dispatchEvent)
                    }

                    composable(route = "camera") {
                        CaptureImageFromCamera(
                            onBack = {
                                navController.popBackStack()
                            },
                            onDone = {
                                navController.previousBackStackEntry?.savedStateHandle?.set(
                                    "imgPath",
                                    it
                                )
                                navController.popBackStack()
                            }
                        )
                    }
                }

                /*val historyViewModel: HistoryViewModel = hiltViewModel()
                val state by historyViewModel.viewState.collectAsState()
                HistoryScreen(state, historyViewModel::dispatchViewEvent)*/
            }
        }
    }
}
