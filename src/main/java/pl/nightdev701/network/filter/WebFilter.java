package pl.nightdev701.network.filter;

/*

Lukas - 14:19
02.06.2024
https://github.com/NightDev701

© SunLightScorpion 2020 - 2024

*/

import pl.nightdev701.logger.AbstractLogger;
import pl.nightdev701.network.task.FilterThread;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;

public class WebFilter {

    ServerSocket server;
    int port;
    List<String> blockedSite;
    AbstractLogger logger;

    public WebFilter(int port, List<String> blockedSite, AbstractLogger logger) {
        this.port = port;
        this.blockedSite = blockedSite;
        this.logger = logger;
    }

    public void start() {
        try {
            server = new ServerSocket(port);
            logger.log(Level.INFO, "Proxy Web-Filter started on port: " + port);

            while (true) {
                Socket socket = server.accept();
                logger.log(Level.INFO, "Proxy Web-Filter accepted from: " + socket.getInetAddress().getHostAddress());
                new Thread(new FilterThread(socket, blockedSite, logger)).start();
            }

        } catch (Exception ex) {
            logger.log(Level.WARNING, ex.getMessage());
            ex.printStackTrace();
        }
    }

}
