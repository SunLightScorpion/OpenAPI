package org.sunlightdev.base;

/*

Lukas - 15:33
26.08.2023
https://github.com/NightDev701

© SunLightScorpion 2020 - 2023

*/

public interface BaseKey {

    Object baseValue();

    void save(String path);

    Object getData(String path);

}