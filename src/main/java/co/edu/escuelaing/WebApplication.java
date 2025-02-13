package co.edu.escuelaing;

import anotations.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;

import static co.edu.escuelaing.HttpServer.*;

public class WebApplication {
    public static void main(String[] args) throws IOException, URISyntaxException {
        /** Configurar archivos estáticos
        staticfiles("/webroot");

        // Rutas del servidor
        get("/app/pi", (req, res) -> String.valueOf(Math.PI));
        get("/app/e", (req, res) -> String.valueOf(Math.E));
        get("/app/hello", (req, res) -> "Hello " + getQueryParam(req, "name"));**/

        // Iniciar el servidor
        HttpServer.start(args);
    }



}
