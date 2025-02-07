package co.edu.escuelaing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HttpServerTest {

    @Test
    public void testGetQueryParam() {
        String request = "GET /app/hello?name=Juan HTTP/1.1";
        String name = HttpServer.getQueryParam(request, "name");
        assertEquals("Juan HTTP/1.1", name);
    }

    @Test
    public void testGetQueryParamNoParam() {
        String request = "GET /app/hello HTTP/1.1";
        String name = HttpServer.getQueryParam(request, "name");
        assertEquals("Guest", name);
    }

    @Test
    public void testGetQueryParamMultipleParams() {
        String request = "GET /app/hello?name=Juan&age=30 HTTP/1.1";
        String name = HttpServer.getQueryParam(request, "name");
        assertEquals("Juan", name);
    }

    @Test
    public void testGetPathBeforeQuery() {
        String url = "/app/hello?name=Juan";
        String path = HttpServer.getPathBeforeQuery(url);
        assertEquals("/app/hello", path);
    }

    @Test
    public void testGetPathBeforeQueryNoQuery() {
        String url = "/app/hello";
        String path = HttpServer.getPathBeforeQuery(url);
        assertEquals("/app/hello", path);
    }

    @Test
    public void testGetContentType() {
        String contentType = HttpServer.getContentType("index.html");
        assertEquals("text/html", contentType);

        contentType = HttpServer.getContentType("styles.css");
        assertEquals("text/css", contentType);

        contentType = HttpServer.getContentType("script.js");
        assertEquals("application/javascript", contentType);

        contentType = HttpServer.getContentType("image.png");
        assertEquals("image/png", contentType);

        contentType = HttpServer.getContentType("photo.jpg");
        assertEquals("image/jpeg", contentType);

        contentType = HttpServer.getContentType("photo.jpeg");
        assertEquals("image/jpeg", contentType);

        contentType = HttpServer.getContentType("animation.gif");
        assertEquals("image/gif", contentType);

        contentType = HttpServer.getContentType("file.txt");
        assertEquals("text/plain", contentType);

        contentType = HttpServer.getContentType("unknown.xyz");
        assertEquals("application/octet-stream", contentType);
    }
}