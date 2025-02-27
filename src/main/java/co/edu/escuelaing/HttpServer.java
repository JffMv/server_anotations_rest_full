package co.edu.escuelaing;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import anotations.GetFile;
import anotations.GetMapping;
import anotations.RequestParam;
import anotations.RestController;
import spark.Request;
import spark.Response;

public class HttpServer {
    private static final int PORT = Integer.parseInt(System.getenv("PORT") != null ? System.getenv("PORT") : "8080");
    private static boolean onDocker = true;
    private static String routeComplementaryDocker = "/usrapp/bin/";



    private static final String RESOURCES_DIR = onDocker? routeComplementaryDocker + "classes":"target/classes" ; // Carpeta dentro de resources
    public static Map<String, Method> services = new HashMap<>();
    private static String routePath = "";

    private static String routeIndex= onDocker? routeComplementaryDocker + "classes/resources/public/index.html":"classes/resources/public/index.html";

    public static void start(String[] args) {
        if (args.length == 0) {
            System.out.println("Debe proporcionar el nombre de la clase controladora.");
            return;
        }

        loadComponents(args); // Cargar los métodos antes de iniciar el servidor

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
            // Leer la primera línea de la petición (ejemplo: "GET /index.html HTTP/1.1")
            String requestLine = in.readLine();
            if (requestLine == null) return;


            System.out.println("Solicitud: " + requestLine);
            String[] parts = requestLine.split(" ");
            if (parts.length < 2 || !parts[0].equals("GET")) {
                //if (!parts[0].equals("POST")) {
                sendResponse(out, "400 Bad Request", "text/plain", "Bad Request".getBytes());
                return;//}
            }

            String requestedEndpoint = parts[1];

            if (requestedEndpoint.equals("/")) {
                handleStaticFileRequest(out, routeIndex);
            }
            else
            {
                String nameParam = (requestedEndpoint.contains("file")) ? HttpServer.getQueryParam(requestedEndpoint, "file") : HttpServer.getQueryParam(requestedEndpoint, "name");
                Method method = services.get(HttpServer.getPathBeforeQuery(requestedEndpoint));

                if (method == null) {
                    sendResponse(out, "404 Not Found Method is null", "text/plain", "Service Not Found".getBytes());
                    return;
                }

                // Obtener el valor por defecto del parámetro si es necesario
                RequestParam paramAnnotation = method.getParameters()[0].getAnnotation(RequestParam.class);
                String defaultValue = (paramAnnotation != null) ? paramAnnotation.defaultValue() : "";
                String finalValue = nameParam.equals("Guest") ? defaultValue : nameParam;

                method.invoke(null, finalValue, out);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param out es la salida del server
     * @param requestedFile debe traer el nombre del archivo
     * @throws IOException
     */
    public static void handleStaticFileRequest(OutputStream out, String requestedFile) throws IOException {
        // Obtener la ruta del archivo dentro de resources/public
        String resourcePath = (requestedFile.contains(routeIndex)) ? requestedFile :RESOURCES_DIR + "/" + requestedFile;
        File file = new File(resourcePath);

        if (!file.exists() && !resourcePath.contains("/"+routePath)) {
            if(!resourcePath.contains("/app") && !requestedFile.contains(routeIndex)){
                sendResponse(out, "404 Not Found", "text/plain", "File or Service Not Found".getBytes());
                return;
            }
        }

        if (resourcePath.contains("/"+ routePath) || requestedFile.contains(routeIndex)){

            // Leer el contenido del archivo
            byte[] fileContent = Files.readAllBytes(file.toPath());
            // Obtener el tipo de contenido
            String contentType = getContentType(requestedFile);
            // Enviar respuesta con el contenido del archivo
            sendResponse(out, "200 OK", contentType, fileContent);
        } else {
            System.out.println(requestedFile + " es la respuesta" + services.keySet());
            String key ="";
            if(requestedFile.contains("/app/hello?")){
                key = getPathBeforeQuery(requestedFile);
            }
            key = key.equals("") ? requestedFile : key;
            String  response = "";//(services.get(key));
            System.out.println(response + " es la respuesta");
            String contentType = getContentType("txt");
            byte[] fileContent = response.getBytes();
            sendResponse(out, "200 OK", contentType, fileContent);
        }

    }

    public static void sendResponse(OutputStream out, String status, String contentType, byte[] content) throws IOException {
        PrintWriter headerWriter = new PrintWriter(out, true);
        headerWriter.println("HTTP/1.1 " + status);
        headerWriter.println("Content-Type: " + contentType);
        headerWriter.println("Content-Length: " + content.length);
        headerWriter.println();
        out.write(content);
        out.flush();
    }


    public static String getContentType(String fileName) {
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
        //System.out.println(extension + " esta es la extension");
        return contentTypes.getOrDefault(extension, "application/octet-stream");
    }

    // Extraer las queries
    public static String getQueryParam(String req, String param) {
        if (!req.contains("?")) return "Guest"; // Si no hay parámetros, devuelve "Guest"

        String queryString = req.split("\\?")[1]; // Extrae "name=Juan"
        String[] params = queryString.split("&");

        for (String p : params) {
            String[] keyValue = p.split("=");
            if (keyValue.length == 2 && keyValue[0].equals(param)) {
                return keyValue[1]; // Retorna el valor del parámetro
            }
        }
        return "Guest"; // Si el parámetro no existe, retorna "Guest"
    }

    public static String getPathBeforeQuery(String url) {
        int index = url.indexOf("?");
        return (index != -1) ? url.substring(0, index) : url;
    }


    public static void loadComponents(String[] args) {
        if (args.length == 0) {
            System.out.println("Debe proporcionar el nombre de la clase controladora.");
            return;
        }
        try {
            Class<?> c = Class.forName(args[0]);
            System.out.println("Cargando clase: " + args[0]);

            if (!c.isAnnotationPresent(RestController.class)) {
                System.out.println("La clase no tiene la anotación @RestController.");
                return;
            }

            for (Method m : c.getDeclaredMethods()) {
                if (m.isAnnotationPresent(GetMapping.class) ) {
                    GetMapping annotation = m.getAnnotation(GetMapping.class);
                    services.put(annotation.value(), m);
                    System.out.println("Método registrado: " + annotation.value());
                }
                if(m.isAnnotationPresent(GetFile.class)){
                    GetFile annotation = m.getAnnotation(GetFile.class);
                    services.put(annotation.value(), m);
                    System.out.println("Método registrado: " + annotation.value());
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println("No se encontró la clase: " + args[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}