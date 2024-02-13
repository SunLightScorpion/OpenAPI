package pl.nightdev701.security.honeypot;

import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import pl.nightdev701.logger.AbstractLogger;

import java.io.IOException;
import java.util.logging.Level;

public class SSHHoneypot {

    int port;
    AbstractLogger logger;
    boolean logIp;

    public SSHHoneypot(int port, AbstractLogger logger) {
        this.port = port;
        this.logger = logger;
        this.logIp = false;
    }

    public void setLogIp(boolean logIp) {
        this.logIp = logIp;
    }

    public void listen() {
        try {
            SshServer sshd = SshServer.setUpDefaultServer();
            sshd.setPort(port);

            sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider());

            sshd.setPasswordAuthenticator((username, password, session) -> {
                if (logIp) {
                    logger.log(Level.WARNING, "HONEYPOT: An attacker has tried to log in with the username " + username + " and the password " + password + ", IP: " + session.getClientAddress() + ".");
                } else {
                    logger.log(Level.WARNING, "HONEYPOT: An attacker has tried to log in with the username " + username + " and the password " + password + ".");
                }
                return false;
            });

            sshd.start();
            logger.log(Level.INFO, "SSH honeypot runs on port " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
