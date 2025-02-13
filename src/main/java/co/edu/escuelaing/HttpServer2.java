package co.edu.escuelaing;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

import anotations.GetMapping;
import anotations.RestController;

public class HttpServer2 {
    private static final int PORT = 8080;
    public static Map<String, Method> services = new HashMap<>();

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Debe proporcionar el nombre de la clase controladora.");
            return;
        }
        loadComponents(args[0]);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor HTTP en ejecución en el puerto " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleRequest(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleRequest(Socket clientSocket) {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                OutputStream out = clientSocket.getOutputStream()
        ) {
            String requestLine = in.readLine();
            if (requestLine == null) return;

            System.out.println("Solicitud: " + requestLine);
            String[] parts = requestLine.split(" ");
            if (parts.length < 2 || !parts[0].equals("GET")) {
                sendResponse(out, "400 Bad Request", "text/plain", "Bad Request".getBytes());
                return;
            }

            String requestedEndpoint = parts[1];
            String path = getPathBeforeQuery(requestedEndpoint);
            Method method = services.get(path);

            if (method != null) {
                Object instance = method.getDeclaringClass().getDeclaredConstructor().newInstance();
                String paramValue = getQueryParam(requestedEndpoint, "name");
                String response = (String) method.invoke(instance, paramValue);
                sendResponse(out, "200 OK", "text/plain", response.getBytes());
            } else {
                sendResponse(out, "404 Not Found", "text/plain", "Service Not Found".getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException ignored) {}
        }
    }

    private static void sendResponse(OutputStream out, String status, String contentType, byte[] content) throws IOException {
        PrintWriter headerWriter = new PrintWriter(out, true);
        headerWriter.println("HTTP/1.1 " + status);
        headerWriter.println("Content-Type: " + contentType);
        headerWriter.println("Content-Length: " + content.length);
        headerWriter.println();
        out.write(content);
        out.flush();
    }

    public static String getQueryParam(String req, String param) {
        if (!req.contains("?")) return "World";
        String queryString = req.split("\\?")[1];
        String[] params = queryString.split("&");
        for (String p : params) {
            String[] keyValue = p.split("=");
            if (keyValue.length == 2 && keyValue[0].equals(param)) {
                return keyValue[1];
            }
        }
        return "World";
    }

    public static String getPathBeforeQuery(String url) {
        int index = url.indexOf("?");
        return (index != -1) ? url.substring(0, index) : url;
    }

    public static void loadComponents(String className) {
        try {
            Class<?> c = Class.forName(className);
            if (!c.isAnnotationPresent(RestController.class)) {
                System.out.println("La clase no está anotada con @RestController.");
                return;
            }
            for (Method m : c.getDeclaredMethods()) {
                if (m.isAnnotationPresent(GetMapping.class)) {
                    GetMapping annotation = m.getAnnotation(GetMapping.class);
                    services.put(annotation.value(), m);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
