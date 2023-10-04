package pl.nightdev701.key;

/*

Lukas - 15:34
26.08.2023
https://github.com/NightDev701

© SunLightScorpion 2020 - 2023

*/

import pl.nightdev701.base.BaseKey;

/**
 * string key
 */
public class ValueKey<T extends String> implements BaseKey {

    T key;

    private ValueKey(T key) {
        this.key = key;
    }

    /**
     * create key
     *
     * @return key value
     */
    public static ValueKey<String> getKey(Object key) {
        return new ValueKey<>(key.toString());
    }

    /**
     * read value
     *
     * @return key value
     */
    @Override
    public Object baseValue() {
        return key;
    }

    @Override
    public void save(String path) {
    }

    @Override
    public Object getData(String path) {
        return null;
    }

}
