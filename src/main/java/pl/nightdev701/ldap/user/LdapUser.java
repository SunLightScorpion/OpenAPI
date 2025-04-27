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
    private final String loginId;

    public LdapUser(String displayName, String email, String dn, String loginId) {
        this.displayName = displayName;
        this.email = email;
        this.dn = dn;
        this.loginId = loginId;
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

    public String getLoginId() {
        return loginId;
    }

}
