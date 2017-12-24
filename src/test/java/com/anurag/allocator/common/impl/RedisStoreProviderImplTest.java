package com.anurag.allocator.common.impl;

import com.anurag.allocator.common.StoreProvider;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class RedisStoreProviderImplTest {
    private static JedisPool jedisPool;
    private static StoreProvider<Object> storeProvider;

    static final String COUNTER_KEY = "counter-test-key";
    static final String ALLOCATED_SET_KEY = "allocated-set-test-key";


    @BeforeClass
    public static void setup() {
        jedisPool = new JedisPool(new JedisPoolConfig(), "localhost");
        storeProvider = new RedisStoreProviderImpl(jedisPool, COUNTER_KEY, ALLOCATED_SET_KEY);
    }


    @Test
    public void testUniqueCounter() throws InterruptedException {
        int numThreads = 100;
        final CountDownLatch before = new CountDownLatch(numThreads);
        final CountDownLatch after = new CountDownLatch(numThreads);
        final Map<String, Long> idsGenerated = new ConcurrentHashMap<>();

        initThreadsAndStart(numThreads, before, after, () -> {
            idsGenerated.put(Thread.currentThread().getName(), storeProvider.nextId());
        });

        after.await();
//        idsGenerated.forEach((k, v) -> System.out.println(k + " ==> " + v));
        Assert.assertTrue(idsGenerated.size() == numThreads);
    }

    @Test
    public void testUniqueCanStore() throws InterruptedException {
        int numThreads = 100;
        final CountDownLatch before = new CountDownLatch(numThreads);
        final CountDownLatch after = new CountDownLatch(numThreads);
        final Map<String, Boolean> numGenerated = new ConcurrentHashMap<>();

        initThreadsAndStart(numThreads, before, after, () -> {
            numGenerated.put(Thread.currentThread().getName(), storeProvider.putIfAbsent(1));
        });

        after.await();
//        numGenerated.forEach((k, v) -> System.out.println(k + " ==> " + v));
        System.out.println("No of threads successful: " + numGenerated.values().stream().filter(x -> x).count());
        Assert.assertTrue(numGenerated.size() == numThreads);
        Assert.assertTrue(numGenerated.values().stream().filter(x -> x).count() == 1);
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