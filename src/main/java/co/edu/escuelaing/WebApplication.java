package co.edu.escuelaing;

import anotations.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;

import static co.edu.escuelaing.HttpServer.*;

public class WebApplication {
    public static void main(String[] args) throws IOException, URISyntaxException {
        // Iniciar el servidor
        HttpServer.start(args);
    }



}
