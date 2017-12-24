package com.anurag.allocator;

import com.anurag.allocator.common.impl.RedisStoreProviderImpl;
import com.anurag.allocator.config.AllocatorConfiguration;
import com.anurag.allocator.resource.PhoneNumber;
import com.anurag.allocator.service.AllocationService;
import com.anurag.allocator.service.AllocationServiceBuilder;
import com.anurag.allocator.service.strategy.NumberGenerationStrategy;
import com.anurag.allocator.service.strategy.impl.BoundedNumberGenerationStrategyDecorator;
import com.anurag.allocator.service.strategy.impl.OffsetBasedNumberGenerationStrategyImpl;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class Main extends Application<AllocatorConfiguration> {

    @Override
    public void run(AllocatorConfiguration configuration, Environment environment) throws Exception {
        JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), configuration.getRedisHost());
        NumberGenerationStrategy numberStrategy = new BoundedNumberGenerationStrategyDecorator(new OffsetBasedNumberGenerationStrategyImpl(configuration.getNumberOffset()), configuration.getNumberLimit());
        AllocationService allocationService = new AllocationServiceBuilder()
                .numberGenerationStrategy(numberStrategy)
                .storeProvider(new RedisStoreProviderImpl(jedisPool, configuration.getRedisCounterKeyName(), configuration.getRedisAllocatedSetKeyName()))
                .build();

        environment.jersey().register(new PhoneNumber(allocationService));
    }

    public static void main(String[] args) throws Exception {
        new Main().run(args);
    }
}
