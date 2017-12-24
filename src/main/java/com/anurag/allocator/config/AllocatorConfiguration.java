package com.anurag.allocator.config;

import io.dropwizard.Configuration;

public class AllocatorConfiguration extends Configuration {
    private String redisHost = "localhost";
    private Integer redisPort = 6379;
    private Long numberOffset = 1111111111L;
    private Long numberLimit = 9999999999L;
    private String redisCounterKeyName = "counter-key";
    private String redisAllocatedSetKeyName = "allocated-set-key";

    public Long getNumberOffset() {
        return numberOffset;
    }

    public void setNumberOffset(Long numberOffset) {
        this.numberOffset = numberOffset;
    }

    public String getRedisHost() {
        return redisHost;
    }

    public void setRedisHost(String redisHost) {
        this.redisHost = redisHost;
    }

    public Integer getRedisPort() {
        return redisPort;
    }

    public void setRedisPort(Integer redisPort) {
        this.redisPort = redisPort;
    }

    public Long getNumberLimit() {
        return numberLimit;
    }

    public void setNumberLimit(Long numberLimit) {
        this.numberLimit = numberLimit;
    }

    public String getRedisCounterKeyName() {
        return redisCounterKeyName;
    }

    public void setRedisCounterKeyName(String redisCounterKeyName) {
        this.redisCounterKeyName = redisCounterKeyName;
    }

    public String getRedisAllocatedSetKeyName() {
        return redisAllocatedSetKeyName;
    }

    public void setRedisAllocatedSetKeyName(String redisAllocatedSetKeyName) {
        this.redisAllocatedSetKeyName = redisAllocatedSetKeyName;
    }
}
