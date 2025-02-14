package controllers;

import java.io.IOException;
import java.io.OutputStream;
import java.net.http.HttpClient;
import java.util.concurrent.atomic.AtomicLong;
import anotations.*;
import co.edu.escuelaing.HttpServer;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/app/hello")
    public static  String greeting(@RequestParam(value = "name", defaultValue = "World") String name, OutputStream out) throws IOException {
        String contentType = HttpServer.getContentType("txt");
        HttpServer.sendResponse(out, "200", contentType, name.getBytes());
        return "OK";
    }
    @GetMapping("/app")
    public static String numbers(String name, OutputStream out) throws IOException {
        String variable = name.equals("pi")? Double.toString(Math.PI): Double.toString(Math.E);
        String contentType = HttpServer.getContentType("txt");
        HttpServer.sendResponse(out, "200", contentType, variable.getBytes());
        return "OK";
    }


    @GetFile("/webroot")
    public static String app(@RequestParam(value = "file", defaultValue = "proof.html") String name, OutputStream out) {
        try{
            HttpServer.handleStaticFileRequest(out, "/webroot/"+name);
            return "File found";
        }
        catch (Exception e){
            e.printStackTrace();
            return "error";
        }

    }
}