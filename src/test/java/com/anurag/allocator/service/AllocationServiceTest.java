package com.anurag.allocator.service;

import com.anurag.allocator.common.StoreProvider;
import com.anurag.allocator.common.impl.RedisStoreProviderImpl;
import com.anurag.allocator.service.strategy.NumberGenerationStrategy;
import com.anurag.allocator.service.strategy.impl.OffsetBasedNumberGenerationStrategyImpl;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class AllocationServiceTest {
    private static JedisPool jedisPool;
    private static StoreProvider<Object> storeProvider;
    private static NumberGenerationStrategy strategy;
    private static AllocationService allocationService;

    static final String COUNTER_KEY = "counter-test-key";
    static final String ALLOCATED_SET_KEY = "allocated-set-test-key";

    @BeforeClass
    public static void setup() {
        jedisPool = new JedisPool(new JedisPoolConfig(), "localhost");
        storeProvider = new RedisStoreProviderImpl(jedisPool, COUNTER_KEY, ALLOCATED_SET_KEY);
        strategy = new OffsetBasedNumberGenerationStrategyImpl(1);
        allocationService = new BasicAllocationService(strategy, storeProvider);
    }

//    @Test
    public void testRedisBasedService() throws InterruptedException {
        int numThreads = 100;
        final CountDownLatch before = new CountDownLatch(numThreads);
        final CountDownLatch after = new CountDownLatch(numThreads);
        final Map<String, String> numGenerated = new ConcurrentHashMap<>();

        initThreadsAndStart(numThreads, before, after, () -> {
            numGenerated.put(Thread.currentThread().getName(),
                    allocationService.allocate(1L)
            );
        });

        after.await();
        Assert.assertTrue(numGenerated.size() == numThreads);
        Assert.assertTrue(numThreads == new HashSet<>(numGenerated.values()).size());

        Set<String> numbersGen = new HashSet<>(numGenerated.values());
        System.out.println(numbersGen);
        for (int i = 1; i <= numThreads; i++) {
            Assert.assertTrue(numbersGen.contains(String.format("000-000-%04d", i)));
        }
    }

    @Test
    public void testRedisBasedServiceWithInvalidPreference() throws InterruptedException {
        int numThreads = 100;
        final CountDownLatch before = new CountDownLatch(numThreads);
        final CountDownLatch after = new CountDownLatch(numThreads);
        final Map<String, String> numGenerated = new ConcurrentHashMap<>();

        initThreadsAndStart(numThreads, before, after, () -> {
            numGenerated.put(Thread.currentThread().getName(),
                    allocationService.allocate(0L)
            );
        });

        after.await();
        Assert.assertTrue(numGenerated.size() == numThreads);
        Assert.assertTrue(numThreads == new HashSet<>(numGenerated.values()).size());

        Set<String> numbersGen = new HashSet<>(numGenerated.values());
        System.out.println(numbersGen);
        for (int i = 2; i <= numThreads; i++) {
            Assert.assertTrue(numbersGen.contains(String.format("000-000-%04d", i)));
        }
    }

    private Thread[] initThreadsAndStart(int numThreads, CountDownLatch b, CountDownLatch a, Runnable task) {
        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = thread(b, task, a);
        }

        for (int i = 0; i < numThreads; i++) {
            threads[i].start();
        }
        return threads;
    }


    private Thread thread(CountDownLatch b, Runnable then, CountDownLatch a) {
        return new Thread(() -> {
            b.countDown();
            try {
                b.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            then.run();
            a.countDown();
        });
    }

    @AfterClass
    public static void tearDownClass() {
        try (Jedis jedi = jedisPool.getResource()) {
            System.out.println("Deleted key: " + jedi.del(COUNTER_KEY));
            System.out.println("Deleted key: " + jedi.del(ALLOCATED_SET_KEY));
        }
        jedisPool.close();
    }

}