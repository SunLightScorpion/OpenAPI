package pl.nightdev701.hook;

/*

Lukas - 18:43
26.09.2023
https://github.com/NightDev701

© SunLightScorpion 2020 - 2023

*/

public interface BaseHookLoader {

    /**
     * init api
     */
    void hookAPI();

    /**
     * init settings
     */
    void initSettings();

    /**
     * get module name
     */
    String getModule();

}
