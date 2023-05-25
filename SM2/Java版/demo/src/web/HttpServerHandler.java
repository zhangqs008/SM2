package src.web;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;

import src.web.HttpServerHandler;

/**
 * 自定义简单Http服务器
 */
public class HttpServerHandler implements HttpHandler {

    // 测试调用代码
    public static void Main() {
        try {
            // HttpServer：HttpServer主要是通过带参的create方法来创建，
            // 第一个参数InetSocketAddress表示绑定的ip地址和端口号。
            // 第二个参数为int类型，表示允许排队的最大TCP连接数，如果该值小于或等于零，则使用系统默认值。
            int port = 8000;
            String path = "/hello";
            HttpServer httpServer = HttpServer.create(new InetSocketAddress(port), 0);
            // createContext：可以调用多次，表示将指定的url路径绑定到指定的HttpHandler处理器对象上，服务器接收到的所有路径请求都将通过调用给定的处理程序对象来处理。
            httpServer.createContext(path, new HttpServerHandler());
            // setExecutor：设置服务器的线程池对象，不设置或者设为null则表示使用start方法创建的线程。
            httpServer.setExecutor(Executors.newFixedThreadPool(10));
            // 启动服务器
            httpServer.start();
            System.out.println("服务器已启动，侦听端口：" + port + "，测试地址：http://localhost:8000" + path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(HttpExchange httpExchange) {
        try {
            StringBuilder responseText = new StringBuilder();
            responseText.append("请求方法：").append(httpExchange.getRequestMethod()).append("<br/>");
            responseText.append("请求参数：").append(getRequestParam(httpExchange)).append("<br/>");
            responseText.append("请求头：<br/>").append(getRequestHeader(httpExchange));
            handleResponse(httpExchange, responseText.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 获取请求头
     * 
     * @param httpExchange
     * @return
     */
    private String getRequestHeader(HttpExchange httpExchange) {
        Headers headers = httpExchange.getRequestHeaders();
        return headers.entrySet().stream()
                .map((Map.Entry<String, List<String>> entry) -> entry.getKey() + ":" + entry.getValue().toString())
                .collect(Collectors.joining("<br/>"));
    }

    /**
     * 获取请求参数
     * 
     * @param httpExchange
     * @return
     * @throws Exception
     */
    private String getRequestParam(HttpExchange httpExchange) throws Exception {
        String paramStr = "";
        if (httpExchange.getRequestMethod().equals("GET")) {
            // GET请求读queryString
            paramStr = httpExchange.getRequestURI().getQuery();
        } else {
            // 非GET请求读请求体
            BufferedReader buffer = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody(), "utf-8"));
            StringBuilder content = new StringBuilder();
            String line = null;
            while ((line = buffer.readLine()) != null) {
                content.append(line);
            }
            paramStr = content.toString();
        }
        return paramStr;
    }

    /**
     * 处理响应
     * 
     * @param httpExchange
     * @param responsetext
     * @throws Exception
     */
    private void handleResponse(HttpExchange httpExchange, String responsetext) throws Exception {
        // 生成html
        StringBuilder responseContent = new StringBuilder();
        responseContent.append("<html>").append("<body>").append(responsetext).append("</body>").append("</html>");
        String responseContentStr = responseContent.toString();
        byte[] responseContentByte = responseContentStr.getBytes("utf-8");

        // 设置响应头，必须在sendResponseHeaders方法之前设置！
        httpExchange.getResponseHeaders().add("Content-Type:", "text/html;charset=utf-8");

        // 设置响应码和响应体长度，必须在getResponseBody方法之前调用！
        httpExchange.sendResponseHeaders(200, responseContentByte.length);

        OutputStream out = httpExchange.getResponseBody();
        out.write(responseContentByte);
        out.flush();
        out.close();
    }
}
