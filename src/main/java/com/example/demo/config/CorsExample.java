package com.example.demo.config;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class CorsExample {
    public static void main(String[] args) throws Exception {
        int port = 8080; // APIサーバーのポート番号

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new MyHandler());
        server.setExecutor(null); // デフォルトの Executor を使用

        server.start();
        System.out.println("APIサーバーが起動しました。ポート: " + port);
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // CORSポリシーを設定する
            Headers headers = exchange.getResponseHeaders();
            headers.set("Access-Control-Allow-Origin", "https://*"); // HTTPSを許可
            headers.set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS"); // 許可するHTTPメソッド
            headers.set("Access-Control-Allow-Headers", "Content-Type, Authorization"); // 許可するヘッダー

            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                // プリフライトリクエストの場合、適切なレスポンスを返す
                headers.set("Access-Control-Allow-Origin", "https://*");
                headers.set("Access-Control-Max-Age", "3600");
                headers.set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                headers.set("Access-Control-Allow-Headers", "Content-Type, Authorization");
                exchange.sendResponseHeaders(204, -1);
            } else {
                // 通常のリクエストの場合、レスポンスのボディを返す
                String response = "APIのレスポンス";
                exchange.sendResponseHeaders(200, response.getBytes().length);
                exchange.getResponseBody().write(response.getBytes());
            }
            exchange.close();
        }
    }
}
