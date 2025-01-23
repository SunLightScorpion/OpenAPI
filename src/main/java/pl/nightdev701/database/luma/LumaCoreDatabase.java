package pl.nightdev701.database.luma;


/*

lukas - 11:15 PM, 1/21/25
https://github.com/NightDev701

© SunLightScorpion 2020 - 2024

*/

import pl.nightdev701.OpenAPI;
import pl.nightdev701.json.JsonManager;
import pl.nightdev701.network.HttpRequestHandler;

import java.util.HashMap;
import java.util.Map;

public class LumaCoreDatabase {

    private final String address;

    public LumaCoreDatabase(String address) {
        this.address = address;
    }

    public void insertData(String database, HashMap<String, String> rec) {
        HttpRequestHandler http = OpenAPI.getRequestHandler(address + "/" + database);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        JsonManager parser = new JsonManager(rec);

        http.makeApiRequest("POST", headers, parser.toJson());
    }

}
