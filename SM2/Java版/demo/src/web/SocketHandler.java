package src.web;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketHandler implements Runnable {

    final static String CRLF = "\r\n"; // 1

    private final Socket clientSocket;

    public SocketHandler(final Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    // 测试调用代码
    public static void Main() {
        final int port = 8000;
        final ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("启动服务，绑定端口： " + port);
            final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(30);
            // 线程池
            // 这个循环不停监听socket连接，使用SocketHandler处理连入的socket，而这个处理是放在线程池中的。
            while (true) {
                final Socket clientSocket = serverSocket.accept();
                System.out.println("新的连接" + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
                try {
                    fixedThreadPool.execute(new SocketHandler(clientSocket));
                } catch (final Exception e) {
                    System.out.println(e);
                }
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public void handleSocket(final Socket clientSocket) throws IOException {

        final BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        final PrintWriter out = new PrintWriter(
                new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())), true);

        String requestHeader = "";
        String s;
        while ((s = in.readLine()) != null) {
            s += CRLF; // 2 很重要，默认情况下in.readLine的结果中`\r\n`被去掉了
            requestHeader = requestHeader + s;
            if (s.equals(CRLF)) { // 3 此处HTTP请求头我们都得到了；如果从请求头中判断有请求正文，则还需要继续获取数据
                break;
            }
        }
        // 获得GET参数
        if (requestHeader.startsWith("GET")) {
            final int begin = requestHeader.indexOf("/?") + 2;
            final int end = requestHeader.indexOf("HTTP/");
            if (requestHeader.length() > end) {
                final String condition = requestHeader.substring(begin, end);
                System.out.println("GET参数是：" + condition);
            }
        }
        // 获得POST参数
        if (requestHeader.startsWith("Content-Length")) {
            final int begin = requestHeader.indexOf("Content-Lengh:") + "Content-Length:".length();
            final String postParamterLength = requestHeader.substring(begin).trim();
            final int contentLength = Integer.parseInt(postParamterLength);
            System.out.println("POST参数长度是：" + contentLength + postParamterLength);
        }

        System.out.println("客户端请求头：");
        System.out.println(requestHeader);
        String responseBody = "客户端的请求头是：\n" + requestHeader;

        // 4 问题来了：1、浏览器如何探测编码 2、浏览器受到content-length后会按照什么方式判断？汉字的个数？字节数？

        responseBody = "Hello";

        String responseHeader = "HTTP/1.0 200 OK\r\n";
        responseHeader += "Content-Type: text/plain; charset=UTF-8\r\n";
        responseHeader += "Content-Length: " + responseBody.getBytes().length + "\r\n" + "\r\n";
        System.out.println("响应头：");
        System.out.println(responseHeader);

        out.write(responseHeader);
        out.write(responseBody);
        out.flush();

        out.close();
        in.close();
        clientSocket.close();

    }

    @Override
    public void run() {
        try {
            handleSocket(clientSocket);
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

}