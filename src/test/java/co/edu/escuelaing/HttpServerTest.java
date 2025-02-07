package co.edu.escuelaing;

import org.junit.jupiter.api.*;
import java.io.*;
import java.net.*;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;

public class HttpServerTest {

    private static final int PORT = 8080;
    private static Thread serverThread;
/*
    // Iniciar el servidor en un hilo separado antes de cada prueba
    @BeforeAll
    static void startServer() {
        serverThread = new Thread(() -> {
            String[] args = {};  // Los args pueden ser vacíos
            HttpServer.start(args);
        });
        serverThread.start();

        // Esperamos un poco para asegurarnos de que el servidor esté en funcionamiento
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Detener el servidor después de cada prueba
    @AfterAll
    static void stopServer() {
        // Lógica para detener el servidor si fuera necesario, aunque en este caso solo cerramos el hilo
        serverThread.interrupt();
    }

    // Prueba para el endpoint /
    @Test
    void testRootEndpoint() throws IOException {
        // Realizamos una solicitud GET al endpoint "/"
        HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:" + PORT + "/").openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();

        // Verificamos que la respuesta sea 200 OK
        assertEquals(200, responseCode, "La respuesta del servidor no es 200 OK");
    }

    // Prueba para el endpoint /imagen
    @Test
    void testImagenEndpoint() throws IOException {
        // Realizamos una solicitud GET al endpoint "/imagen"
        HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:" + PORT + "/imagen").openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();

        // Verificamos que la respuesta sea 200 OK
        assertEquals(200, responseCode, "La respuesta del servidor no es 200 OK");
    }

    // Prueba para el endpoint /html
    @Test
    void testHtmlEndpoint() throws IOException {
        // Realizamos una solicitud GET al endpoint "/html"
        HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:" + PORT + "/html").openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();

        // Verificamos que la respuesta sea 200 OK
        assertEquals(200, responseCode, "La respuesta del servidor no es 200 OK");
    }

    // Prueba para el endpoint /css
    @Test
    void testCssEndpoint() throws IOException {
        // Realizamos una solicitud GET al endpoint "/css"
        HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:" + PORT + "/css").openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();

        // Verificamos que la respuesta sea 200 OK
        assertEquals(200, responseCode, "La respuesta del servidor no es 200 OK");
    }

    // Prueba para el endpoint /js
    @Test
    void testJsEndpoint() throws IOException {
        // Realizamos una solicitud GET al endpoint "/js"
        HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:" + PORT + "/js").openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();

        // Verificamos que la respuesta sea 200 OK
        assertEquals(200, responseCode, "La respuesta del servidor no es 200 OK");
    }*/
}
