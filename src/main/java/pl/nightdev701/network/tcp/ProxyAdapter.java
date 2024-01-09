package pl.nightdev701.network.tcp;

import pl.nightdev701.logger.AbstractLogger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;

public class ProxyAdapter {

    AbstractLogger logger;
    int localPort;
    String remoteHost;
    int remotePort;

    public ProxyAdapter(int localPort, String remoteHost, int remotePort, AbstractLogger logger) {
        this.localPort = localPort;
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
        this.logger = logger;
    }

    /**
     * run socket proxy
     */
    public void runServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(localPort);

            logger.log(Level.INFO, "Proxy-Server listen on port " + localPort);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket, remoteHost, remotePort)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.WARNING, "Connection error: " + e.getMessage());
        }
    }

    /**
     * handle client
     */
    private void handleClient(Socket clientSocket, String remoteHost, int remotePort) {
        try {
            Socket remoteSocket = new Socket(remoteHost, remotePort);
            InputStream clientInput = clientSocket.getInputStream();
            OutputStream clientOutput = clientSocket.getOutputStream();
            InputStream remoteInput = remoteSocket.getInputStream();
            OutputStream remoteOutput = remoteSocket.getOutputStream();

            new Thread(() -> {
                byte[] buffer = new byte[1024];
                int bytesRead;
                try {
                    while ((bytesRead = clientInput.read(buffer)) != -1) {
                        remoteOutput.write(buffer, 0, bytesRead);
                        remoteOutput.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.log(Level.WARNING, "Connection error: " + e.getMessage());
                }
            }).start();

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = remoteInput.read(buffer)) != -1) {
                clientOutput.write(buffer, 0, bytesRead);
                clientOutput.flush();
            }

            clientSocket.close();
            remoteSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.WARNING, "Connection error: " + e.getMessage());
        }
    }

}
