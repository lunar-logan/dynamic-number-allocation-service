package com.anurag.allocator.common.impl;

import com.anurag.allocator.common.StoreProvider;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisStoreProviderImpl implements StoreProvider<Object> {
    private static final String COUNTER_KEY = "counter-key";
    private static final String ALLOCATED_SET_KEY = "allocated-set-key";

    private final JedisPool jedisPool;
    private final String counterKey;
    private final String allocatedSetKey;

    public RedisStoreProviderImpl(JedisPool jedisPool, String counterKey, String allocatedSetKey) {
        this.jedisPool = jedisPool;
        this.counterKey = counterKey;
        this.allocatedSetKey = allocatedSetKey;
    }

    @Override
    public Long nextId() {
        Long id;
        try (Jedis jedis = jedisPool.getResource()) {
            id = jedis.incr(counterKey);

        }
        return id;
    }

    @Override
    public Boolean putIfAbsent(Object what) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.sadd(allocatedSetKey, String.valueOf(what)) == 1L;
        }
    }
}
