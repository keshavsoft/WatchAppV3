package com.example.watchappv3.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.example.watchappv3.presentation.ws.WatchWebSocketClient

import androidx.wear.compose.foundation.lazy.AutoCenteringParams
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.DisposableEffect
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController

class MainActivity : ComponentActivity() {
    fun onCreate1(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberSwipeDismissableNavController()

            SwipeDismissableNavHost(
                navController = navController,
                startDestination = "main"
            ) {
                composable("main") {
                    WatchApp()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNav()
        }
    }
}

@Composable
fun WatchHomeScreen() {

    // 🔌 WebSocket lifecycle tied to UI
    DisposableEffect(Unit) {
        WatchWebSocketClient.connect()

        onDispose {
            WatchWebSocketClient.disconnect()
        }
    }

    MessagesUI()
}

@Composable
fun AppNav() {
    val navController = rememberSwipeDismissableNavController()

    SwipeDismissableNavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            WatchHomeScreen()
        }
    }
}

@Composable
fun WatchApp() {

    DisposableEffect(Unit) {
        // UI appeared → connect
        WatchWebSocketClient.connect()

        onDispose {
            // UI dismissed (swipe) → disconnect
            WatchWebSocketClient.disconnect()
        }
    }

    MessagesUI()
}

@Composable
fun MessagesUI() {

    val messages = WatchWebSocketClient
        .messages
        .collectAsState(initial = emptyList())
        .value

    MaterialTheme {
        ScalingLazyColumn(
            modifier = Modifier.fillMaxSize(),
            autoCentering = AutoCenteringParams(itemIndex = 0),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(messages) { msg ->
                MessageBubble(msg)
            }
        }
    }
}

@Composable
fun WatchApp1() {

    val messages = WatchWebSocketClient
        .messages
        .collectAsState(initial = emptyList())
        .value

    MaterialTheme {
        ScalingLazyColumn(
            modifier = Modifier.fillMaxSize(),
            autoCentering = AutoCenteringParams(itemIndex = 0),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(messages) { msg ->
                MessageBubble(msg)
            }
        }
    }
}


@Composable
fun MessageBubble(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f)        // round-safe width
            .padding(vertical = 6.dp)
            .background(
                color = Color(0xFF1F1F1F), // dark neutral
                shape = RoundedCornerShape(14.dp)
            )
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            maxLines = 3
        )
    }
}

