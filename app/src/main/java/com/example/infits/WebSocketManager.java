package com.example.infits;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import Constants.WebSocketConstants;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class WebSocketManager {
    private OkHttpClient okHttpclient;
    private WebSocket webSocket;

    public WebSocketManager() {
        this.okHttpclient = new OkHttpClient();
    }

    public void connectWebSocket() {
        Request request = new Request.Builder()
                .url(WebSocketConstants.WS_URL)
                .build();

        WebSocketListener webSocketListener = new WebSocketListener() {
            @Override
            public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
                super.onClosed(webSocket, code, reason);
            }

            @Override
            public void onClosing(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
                super.onClosing(webSocket, code, reason);
            }

            @Override
            public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, @Nullable Response response) {
                super.onFailure(webSocket, t, response);
            }

            @Override
            public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
                super.onMessage(webSocket, text);
            }

            @Override
            public void onMessage(@NonNull WebSocket webSocket, @NonNull ByteString bytes) {
                super.onMessage(webSocket, bytes);
            }

            @Override
            public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
                super.onOpen(webSocket, response);
            }
        };

        webSocket = okHttpclient.newWebSocket(request, webSocketListener);
    }

    public void sendMessage(String message) {
        if (webSocket != null) {
            webSocket.send(message);

        }
    }
        public void closeWebSocket(){
            if(webSocket!=null){
                webSocket.close(1000,"user intitated");
            }
        }


}

