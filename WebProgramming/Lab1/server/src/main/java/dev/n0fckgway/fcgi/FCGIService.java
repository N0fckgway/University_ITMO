package dev.n0fckgway.fcgi;

import com.fastcgi.FCGIInterface;
import dev.n0fckgway.fcgi.handler.Handler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class FCGIService implements Runnable {
    private final FCGIInterface fcgiInterface = new FCGIInterface();
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final Map<String, Map<String, Handler>> registrationRequest = new HashMap<>();


    {
        try {
            FileHandler fileHandler = new FileHandler("./fcgi.log", false);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);

            logger.addHandler(fileHandler);
            logger.info("Logger init!");

        } catch (SecurityException | IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

    }

    @Override
    public void run() {
       while (fcgiInterface.FCGIaccept() >= 0) {
           handle();
       }

    }

    public void registerHandler(String method, String path, Handler handler) {
        registrationRequest.computeIfAbsent(method, k -> new HashMap<>()).put(path, handler);
    }

    private void handle() {
        logger.info("I've got request from client: %s".formatted(FCGIInterface.request.params.getProperty("REMOTE_ADDR")));
        try {
            String method = FCGIInterface.request.params.getProperty("REQUEST_METHOD");
            logger.info("I've got method: %s".formatted(method));
            if (!registrationRequest.containsKey(method)) {
                logger.info("I've got 405 ERROR");
                System.out.println("""
                        HTTP/1.1 405 Method Not Allowed\r\n
                        Content-Length: 0\r\n
                        Allow: GET\r\n
                        \r\n
                        """);

                return;
            }

            String path = FCGIInterface.request.params.getProperty("REQUEST_URI");
            logger.info("I've got URI: %s".formatted(path));
            path = path.substring(
                    path.lastIndexOf('/'),
                    path.indexOf('?') == -1 ? path.length() : path.indexOf('?')
            );
            logger.info("I've got formatted path: %s".formatted(path));

            if (!registrationRequest.get(method).containsKey(path)) {
                logger.info("I've got 404 ERROR");
                System.out.println("""
                       HTTP/1.1 404 Not Found\r\n
                       Content-Length: 0\r\n
                       \r\n
                       """);
                return;
            }
            registrationRequest.get(method).get(path).process();

        } catch (Exception e) {
            logger.info("I've got bad req: " + e.getMessage());
            String headers = """
                    HTTP/1.1 400 Bad Request\r\n
                    Content-Type: application/json\r\n
                    Content-Length: %d\r\n
                    \r\n
                    %s""";

            String json = """
                    {"error": "Bad Request","message": "%s"}
                    """.formatted(e.getMessage());
            System.out.print(String.format(headers, json.getBytes(StandardCharsets.UTF_8).length, json));

        }
    }

    private Map<String, String> makeHeaders() {
        Properties param = FCGIInterface.request.params;
        Map<String, String> header = new HashMap<>();

        for (String name: param.stringPropertyNames()) {
            String http = "HTTP_";
            if (name.startsWith(http)) {
                header.put(name.substring(http.length()), param.getProperty(name));
            }
        }
        return header;
    }



}
