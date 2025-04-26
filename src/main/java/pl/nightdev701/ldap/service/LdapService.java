package pl.nightdev701.ldap.service;



/*

lukas - 6:46PM
4/26/25
https://github.com/NightDev701

© SunLightScorpion 2020 - 2024

*/

import com.unboundid.ldap.sdk.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.nightdev701.ldap.user.LdapUser;
import pl.nightdev701.logger.AbstractLogger;

import java.util.logging.Level;

public class LdapService {

    private final String domain;
    private final String ldapHost;
    private final int ldapPort;
    private final String baseDN;
    private final AbstractLogger logger;

    public LdapService(String domain, String ldapHost, int ldapPort, String baseDN, AbstractLogger logger) {
        this.domain = domain;
        this.ldapHost = ldapHost;
        this.ldapPort = ldapPort;
        this.baseDN = baseDN;
        this.logger = logger;
    }

    public LdapUser authenticate(String username, String password) {
        LDAPConnection connection = null;
        try {
            connection = new LDAPConnection(ldapHost, ldapPort);

            String userPrincipal = username.contains("@") ? username : username + "@" + domain;
            BindResult bindResult = connection.bind(userPrincipal, password);

            if (bindResult.getResultCode() != ResultCode.SUCCESS) {
                logger.log(Level.WARNING, "Bind failed for user: " + username);
                return null;
            }

            SearchResult searchResult = connection.search(
                    baseDN,
                    SearchScope.SUB,
                    "(sAMAccountName=" + username + ")",
                    "dn", "cn", "mail"
            );

            if (searchResult.getEntryCount() == 0) {
                logger.log(Level.WARNING, "User not found after bind: " + username);
                return null;
            }

            SearchResultEntry userEntry = searchResult.getSearchEntries().get(0);
            String userDN = userEntry.getDN();

            return new LdapUser(
                    userEntry.getAttributeValue("cn"),
                    userEntry.getAttributeValue("mail"),
                    userDN
            );

        } catch (LDAPException ex) {
            logger.log(Level.WARNING, "Can't authenticate \"" + username + "\", error: " + ex.getMessage());
            return null;
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

}
