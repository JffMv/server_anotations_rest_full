package controllers;

import java.util.concurrent.atomic.AtomicLong;
import anotations.*;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/greeting")
    public static  String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hola " + name;
    }
    @GetMapping("/pi")
    public static String pi(String name) {
        return Double.toString(Math.PI);
    }
}