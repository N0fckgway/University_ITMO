package ru.suvorov.client;



import ru.suvorov.client.ui.ClientUI;



public class Client {
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 1919;

    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : DEFAULT_HOST;
        int port = args.length > 1 ? Integer.parseInt(args[1]) : DEFAULT_PORT;

        ClientConnection connection = new ClientConnection(host, port);
        ClientUI ui = new ClientUI(connection);
        ui.start();
    }
} 