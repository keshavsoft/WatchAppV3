package com.example.watchappv3.presentation.ws

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object WatchWebSocketClient {

    private const val TAG = "WatchWS"
    private const val WS_URL = "wss://keshavsoft.com/"

    private val client = OkHttpClient()
    private var socket: WebSocket? = null

    private val _messages = MutableStateFlow<List<String>>(emptyList())
    val messages: StateFlow<List<String>> = _messages

    fun connect() {
        if (socket != null) return

        val request = Request.Builder().url(WS_URL).build()
        socket = client.newWebSocket(request, object : WebSocketListener() {

            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d(TAG, "CONNECTED")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d(TAG, "MSG: $text")
                _messages.value = _messages.value + text
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e(TAG, "ERROR", t)
                socket = null
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Log.d(TAG, "CLOSED")
                socket = null
            }
        })
    }

    fun disconnect() {
        socket?.close(1000, "bye")
        socket = null
    }
}
