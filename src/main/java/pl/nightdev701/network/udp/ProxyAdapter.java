package pl.nightdev701.network.udp;

import pl.nightdev701.logger.AbstractLogger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
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
            DatagramSocket socket = new DatagramSocket(localPort);

            logger.log(Level.INFO, "Proxy-Server listen on port " + localPort);

            while (true) {
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);

                new Thread(() -> handleClient(receivePacket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.WARNING, "Connection error: " + e.getMessage());
        }
    }

    /**
     * handle client
     */
    private void handleClient(DatagramPacket receivePacket) {
        try {
            DatagramSocket remoteSocket = new DatagramSocket();
            InetAddress clientAddress = receivePacket.getAddress();
            int clientPort = receivePacket.getPort();

            byte[] clientData = receivePacket.getData();
            DatagramPacket sendPacket = new DatagramPacket(clientData, clientData.length, InetAddress.getByName(remoteHost), remotePort);
            remoteSocket.send(sendPacket);

            // Handling incoming data from remote server
            byte[] receiveData = new byte[1024];
            DatagramPacket receiveFromServerPacket = new DatagramPacket(receiveData, receiveData.length);
            remoteSocket.receive(receiveFromServerPacket);

            // Send data back to the client
            DatagramPacket sendToClientPacket = new DatagramPacket(receiveFromServerPacket.getData(), receiveFromServerPacket.getLength(), clientAddress, clientPort);
            new DatagramSocket().send(sendToClientPacket);

            remoteSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.WARNING, "Connection error: " + e.getMessage());
        }
    }
}
