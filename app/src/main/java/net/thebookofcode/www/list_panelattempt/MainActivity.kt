package net.thebookofcode.www.list_panelattempt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import dagger.hilt.android.AndroidEntryPoint
import net.thebookofcode.www.list_panelattempt.model.TheBOCViewModel
import net.thebookofcode.www.list_panelattempt.ui.screens.TheBOCApp
import net.thebookofcode.www.list_panelattempt.ui.screens.home.HomeViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: TheBOCViewModel by viewModels()
        val homeViewModel : HomeViewModel by viewModels()
        setContent {
            val widthSizeClass = calculateWindowSizeClass(this).widthSizeClass
            TheBOCApp(
                widthSizeClass = widthSizeClass,
                viewModel = viewModel,
                homeViewModel = homeViewModel
            )
        }
    }
}

