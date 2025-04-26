package pl.nightdev701.ldap;


/*

lukas - 6:57PM
4/26/25
https://github.com/NightDev701

© SunLightScorpion 2020 - 2024

*/

import pl.nightdev701.ldap.service.LdapService;
import pl.nightdev701.ldap.user.LdapUser;

public class LdapAuthController {

    private final LdapService service;

    public LdapAuthController(LdapService service) {
        this.service = service;
    }

    public LdapUser login(String user, String password) {
        return service.authenticate(user, password);
    }

}
