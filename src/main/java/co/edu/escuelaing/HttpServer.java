package co.edu.escuelaing;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

public class HttpServer {
    private static final int PORT = 8080;
    private static final String RESOURCES_DIR = "public"; // Carpeta dentro de resources

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor HTTP en ejecución en el puerto " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleRequest(clientSocket)).start(); // Manejo de múltiples clientes
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
            // Leer la primera línea de la petición (ejemplo: "GET /index.html HTTP/1.1")
            String requestLine = in.readLine();
            if (requestLine == null) return;

            System.out.println("Solicitud: " + requestLine);
            String[] parts = requestLine.split(" ");
            if (parts.length < 2 || !parts[0].equals("GET")) {
                if (!parts[0].equals("POST")) {
                sendResponse(out, "400 Bad Request", "text/plain", "Bad Request".getBytes());
                return;}
            }

            // Verificar si el endpoint es /conection
            String requestedEndpoint = parts[1];
            if (requestedEndpoint.equals("/")) {
                handleStaticFileRequest(out, "src/main/resources/public/index.html");
            } else if (requestedEndpoint.equals("/imagen")) {
                handleStaticFileRequest(out, "proof.png");
            } else if (requestedEndpoint.equals("/html")) {
                handleStaticFileRequest(out, "proof.html");
            } else if (requestedEndpoint.equals("/css")) {
                handleStaticFileRequest(out, "proof.css");
            } else if (requestedEndpoint.equals("/js")) {
                handleStaticFileRequest(out, "proof.js");
            }else {
                handleStaticFileRequest(out, requestedEndpoint.substring(1)); // Eliminar el '/' inicial
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException ignored) {}
        }
    }

    private static void handleStaticFileRequest(OutputStream out, String requestedFile) throws IOException {
        // Obtener la ruta del archivo dentro de resources/public
        String resourcePath = (requestedFile.contains("src/main/resources/public/index.html")) ? requestedFile :RESOURCES_DIR + "/" + requestedFile ;
        File file = new File(resourcePath);

        if (!file.exists()) {
            sendResponse(out, "404 Not Found", "text/plain", "File Not Found".getBytes());
            return;
        }

        // Leer el contenido del archivo
        byte[] fileContent = Files.readAllBytes(file.toPath());

        // Obtener el tipo de contenido
        String contentType = getContentType(requestedFile);

        // Enviar respuesta con el contenido del archivo
        sendResponse(out, "200 OK", contentType, fileContent);
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

    private static String getContentType(String fileName) {
        Map<String, String> contentTypes = new HashMap<>();
        contentTypes.put("html", "text/html");
        contentTypes.put("css", "text/css");
        contentTypes.put("js", "application/javascript");
        contentTypes.put("png", "image/png");
        contentTypes.put("jpg", "image/jpeg");
        contentTypes.put("jpeg", "image/jpeg");
        contentTypes.put("gif", "image/gif");
        contentTypes.put("txt", "text/plain");

        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        return contentTypes.getOrDefault(extension, "application/octet-stream");
    }
}