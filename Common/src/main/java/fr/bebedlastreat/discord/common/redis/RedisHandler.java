package fr.bebedlastreat.discord.common.redis;

import lombok.Data;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

@Data
public class RedisHandler {

    private final String host;
    private final int port;
    private final String password;

    private JedisPool jedisPool;

    public RedisHandler(String host, int port, String password) {
        this.host = host;
        this.port = port;
        this.password = password;
    }

    public void init() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(40);
        jedisPoolConfig.setMaxIdle(20);
        jedisPoolConfig.setMinIdle(1);
        if (password == null || password.isEmpty()) {
            jedisPool = new JedisPool(jedisPoolConfig, host, port, 5000);
        } else {
            jedisPool = new JedisPool(jedisPoolConfig, host, port, 5000, password);
        }
    }

    public void close() {
        jedisPool.close();
    }

    public Jedis getResource() {
        return jedisPool.getResource();
    }

    public void send(String channel, String message) {
        try (Jedis jedis = getResource()) {
            jedis.publish(channel, message);
        }
    }

    public void subscribe(String channel, JedisPubSub subscriber) {
        try (Jedis jedis = getResource()) {
            jedis.subscribe(subscriber, channel);
        }
    }
}
