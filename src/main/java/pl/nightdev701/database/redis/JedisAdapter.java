package pl.nightdev701.database.redis;

import pl.nightdev701.logger.AbstractLogger;
import redis.clients.jedis.Jedis;

import java.util.logging.Level;

public class JedisAdapter {

    Jedis jedis;
    AbstractLogger logger;

    public JedisAdapter(String host, int port, AbstractLogger logger) {
        this.jedis = new Jedis(host, port);
        this.logger = logger;

        if (jedis.isConnected()) {
            logger.log(Level.INFO, "Redis Database is connected to " + host + ":" + port);
        }
    }

    public Jedis getJedis() {
        return jedis;
    }

}
