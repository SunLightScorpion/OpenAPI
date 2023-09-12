package org.sunlightdev.database;

/*

Lukas - 18:15
03.09.2023
https://github.com/NightDev701

© SunLightScorpion 2020 - 2023

*/

public class DatabaseConnector {

    String ip;
    String user;
    String database;
    String table;

    public DatabaseConnector(String ip, String user, String database, String table){
        this.ip = ip;
        this.user = user;
        this.database = database;
        this.table = table;
    }

}
