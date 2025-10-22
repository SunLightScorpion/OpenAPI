package pl.nightdev701.ldap.service;



/*

lukas - 6:46PM
4/26/25
https://github.com/NightDev701

© SunLightScorpion 2020 - 2024

*/

import com.unboundid.ldap.sdk.*;
import com.unboundid.util.ssl.SSLUtil;
import pl.nightdev701.ldap.user.LdapUser;
import pl.nightdev701.logger.AbstractLogger;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.logging.Level;

public class LdapService {

    private final String domain;
    private final String ldapHost;
    private final int ldapPort;
    private final String baseDN;
    private final AbstractLogger logger;
    private LDAPConnection connection;

    public LdapService(String domain, String ldapHost, int ldapPort, String baseDN, AbstractLogger logger) {
        this.domain = domain;
        this.ldapHost = ldapHost;
        this.ldapPort = ldapPort;
        this.baseDN = baseDN;
        this.logger = logger;
    }

    private void connectLdap() {
        try {
            if (connection == null || !connection.isConnected()) {
                connection = new LDAPConnection(ldapHost, ldapPort);
            }
        } catch (LDAPException e) {
            logger.log(Level.SEVERE, "Connection failed: " + e.getMessage());
        }
    }

    private void connectLdaps(String trustStorePath) {
        try {
            if (connection == null || !connection.isConnected()) {
                logger.log(Level.INFO, "Trying LDAPS connect to " + ldapHost + ":" + ldapPort);

                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStore.load(null, null);

                try (InputStream fis = new FileInputStream(trustStorePath)) {
                    CertificateFactory cf = CertificateFactory.getInstance("X.509");
                    Certificate cert = cf.generateCertificate(fis);
                    trustStore.setCertificateEntry("custom-ca", cert);
                }

                TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                tmf.init(trustStore);

                SSLUtil sslUtil = new SSLUtil(tmf.getTrustManagers());
                SSLSocketFactory sslSocketFactory = sslUtil.createSSLSocketFactory();

                connection = new LDAPConnection(sslSocketFactory, ldapHost, ldapPort);
                logger.log(Level.INFO, "LDAPS connection established using custom CA path.");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Connection failed: " + e.getMessage());
        }
    }

    public LdapUser authenticate(String username, String password, String trust) {
        try {

            if (connection == null || !connection.isConnected()) {
                if (ldapPort == 636) {
                    connectLdaps(trust);
                } else {
                    connectLdap();
                }
            }

            String userPrincipal = username.contains("@") ? username : username + "@" + domain;
            BindResult bindResult = connection.bind(userPrincipal, password);

            logger.log(Level.INFO, "Try to authenticate " + userPrincipal);

            if (bindResult.getResultCode() != ResultCode.SUCCESS) {
                logger.log(Level.WARNING, "Bind failed for user: " + username);
                return null;
            }

            SearchResult searchResult = connection.search(
                    baseDN,
                    SearchScope.SUB,
                    "(sAMAccountName=" + username + ")",
                    "dn", "cn", "mail", "sAMAccountName"
            );

            if (searchResult.getEntryCount() == 0) {
                logger.log(Level.INFO, "User not found after bind: " + username);
                return null;
            }

            SearchResultEntry userEntry = searchResult.getSearchEntries().get(0);
            String userDN = userEntry.getDN();

            logger.log(Level.INFO, "Authenticated: " + username);

            return new LdapUser(
                    userEntry.getAttributeValue("cn"),
                    userEntry.getAttributeValue("mail"),
                    userDN,
                    userEntry.getAttributeValue("sAMAccountName")
            );

        } catch (LDAPException ex) {
            logger.log(Level.WARNING, "Can't authenticate " + username + ", error: " + ex.getMessage());
            return null;
        }
    }

    public boolean isAuthenticated(LdapUser user) {
        return user != null;
    }

    public LDAPConnection getConnection() {
        return connection;
    }

    public void closeConnection() {
        if (connection != null && connection.isConnected()) {
            connection.close();
        }
    }

}
