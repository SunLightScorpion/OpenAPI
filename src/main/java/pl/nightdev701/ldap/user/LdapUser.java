package pl.nightdev701.ldap.user;



/*

lukas - 6:46PM
4/26/25
https://github.com/NightDev701

© SunLightScorpion 2020 - 2024

*/

public class LdapUser {

    private final String displayName;
    private final String email;
    private final String dn;

    public LdapUser(String displayName, String email, String dn) {
        this.displayName = displayName;
        this.email = email;
        this.dn = dn;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public String getDn() {
        return dn;
    }

}
