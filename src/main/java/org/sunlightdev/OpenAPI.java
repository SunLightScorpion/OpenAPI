package org.sunlightdev;

/*

Lukas - 18:36
25.08.2023
https://github.com/NightDev701

© SunLightScorpion 2020 - 2023

*/

import org.sunlightdev.crypto.CryptoManager;

public class OpenAPI {

    public static CryptoManager getCryptoManager(String key) {
        return new CryptoManager(key);
    }

}
