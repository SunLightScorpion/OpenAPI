package pl.nightdev701.network.task;

/*

Lukas - 14:27
02.06.2024
https://github.com/NightDev701

© SunLightScorpion 2020 - 2024

*/

import pl.nightdev701.logger.AbstractLogger;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;

public class FilterThread implements Runnable {

    Socket client;
    List<String> blockedSite;
    AbstractLogger logger;

    public FilterThread(Socket client, List<String> blockedSite, AbstractLogger logger) {
        this.client = client;
        this.blockedSite = blockedSite;
        this.logger = logger;
    }

    @Override
    public void run() {
        try {
            InputStream clientInput = client.getInputStream();
            OutputStream clientOutput = client.getOutputStream();

            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientInput));
            DataOutputStream outToClient = new DataOutputStream(clientOutput);

            String requestLine = inFromClient.readLine();
            logger.log(Level.INFO,"Request Line: " + requestLine);

            if (requestLine != null) {
                String[] parts = requestLine.split(" ");
                if (parts.length == 3) {
                    String method = parts[0];
                    String url = parts[1];
                    String version = parts[2];

                    URL targetURL = new URL(url);
                    String host = targetURL.getHost();

                    if (blockedSite.contains(host)) {
                        outToClient.writeBytes("HTTP/1.1 403 Forbidden\r\n");
                        outToClient.writeBytes("Content-Type: text/html\r\n");
                        outToClient.writeBytes("\r\n");
                        outToClient.writeBytes("<html><body><h1>403 Forbidden</h1><p>Access to this site is blocked.</p></body></html>");
                        outToClient.flush();
                    } else {
                        if (method.equals("CONNECT")) {
                            handleHttpsRequest(parts, clientInput, clientOutput);
                        } else {
                            handleHttpRequest(parts, inFromClient, outToClient);
                        }
                    }
                }
            }
            client.close();
        } catch (Exception ex){
            logger.log(Level.WARNING, ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void handleHttpRequest(String[] parts, BufferedReader inFromClient, DataOutputStream outToClient) {
        try {
            String method = parts[0];
            String urlString = parts[1];
            String version = parts[2];

            URL url = new URL(urlString);
            String host = url.getHost();
            int port = (url.getPort() == -1) ? 80 : url.getPort();

            Socket targetSocket = new Socket(host, port);
            OutputStream targetOutput = targetSocket.getOutputStream();
            InputStream targetInput = targetSocket.getInputStream();

            DataOutputStream outToTarget = new DataOutputStream(targetOutput);
            outToTarget.writeBytes(method + " " + url.getFile() + " " + version + "\r\n");

            String headerLine;
            while (!(headerLine = inFromClient.readLine()).isEmpty()) {
                outToTarget.writeBytes(headerLine + "\r\n");
            }
            outToTarget.writeBytes("\r\n");

            BufferedReader inFromTarget = new BufferedReader(new InputStreamReader(targetInput));
            String responseLine;
            while ((responseLine = inFromTarget.readLine()) != null) {
                outToClient.writeBytes(responseLine + "\r\n");
            }

            targetSocket.close();
        } catch (IOException ex) {
            logger.log(Level.WARNING, ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void handleHttpsRequest(String[] parts, InputStream clientInput, OutputStream clientOutput) {
        try {
            String hostPort = parts[1];
            String[] hostPortSplit = hostPort.split(":");
            String host = hostPortSplit[0];
            int port = (hostPortSplit.length == 2) ? Integer.parseInt(hostPortSplit[1]) : 443;

            Socket targetSocket = new Socket(host, port);

            DataOutputStream outToClient = new DataOutputStream(clientOutput);
            outToClient.writeBytes("HTTP/1.1 200 Connection Established\r\n");
            outToClient.writeBytes("Proxy-Agent: JavaProxy/1.0\r\n");
            outToClient.writeBytes("\r\n");
            outToClient.flush();

            InputStream targetInput = targetSocket.getInputStream();
            OutputStream targetOutput = targetSocket.getOutputStream();

            Thread clientToServer = new Thread(() -> {
                try {
                    byte[] buffer = new byte[4096];
                    int read;
                    while ((read = clientInput.read(buffer)) != -1) {
                        targetOutput.write(buffer, 0, read);
                        targetOutput.flush();
                    }
                } catch (IOException ex) {
                    logger.log(Level.WARNING, ex.getMessage());
                    ex.printStackTrace();
                }
            });

            Thread serverToClient = new Thread(() -> {
                try {
                    byte[] buffer = new byte[4096];
                    int read;
                    while ((read = targetInput.read(buffer)) != -1) {
                        clientOutput.write(buffer, 0, read);
                        clientOutput.flush();
                    }
                } catch (IOException ex) {
                    logger.log(Level.WARNING, ex.getMessage());
                    ex.printStackTrace();
                }
            });

            clientToServer.start();
            serverToClient.start();

            clientToServer.join();
            serverToClient.join();

            targetSocket.close();
        } catch (IOException | InterruptedException ex) {
            logger.log(Level.WARNING, ex.getMessage());
            ex.printStackTrace();
        }
    }

}
