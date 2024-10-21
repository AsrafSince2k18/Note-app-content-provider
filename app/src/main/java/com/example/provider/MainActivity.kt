package com.example.provider

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.provider.notes.presentance.screen.homeScreen.screen.HomeScreen
import com.example.provider.notes.presentance.screen.homeScreen.viewModel.HomeViewModel
import com.example.provider.ui.theme.ProviderTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProviderTheme {
                val viewModel = hiltViewModel<HomeViewModel>()
                val state by viewModel.homeState.collectAsState()
                HomeScreen(homeState = state, homeEvent = viewModel::onEvent)
            }
        }
    }
}

