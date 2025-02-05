package pl.nightdev701.json;


/*

lukas - 9:08 PM, 1/23/25
https://github.com/NightDev701

© SunLightScorpion 2020 - 2024

*/

import com.google.gson.Gson;

import java.util.HashMap;

public class JsonManager {

    private final HashMap<String, String> data;

    public JsonManager(HashMap<String, String> data) {
        this.data = data;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(data);
    }

}
