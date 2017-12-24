package com.anurag.allocator.common.impl;

import com.anurag.allocator.common.StoreProvider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryStoreProviderImpl implements StoreProvider<Long> {
    private Map<Long, Boolean> allocatedNumbers = new ConcurrentHashMap<>();
    private AtomicLong counter = new AtomicLong(0);

    @Override
    public Long nextId() {
        return counter.incrementAndGet();
    }

    @Override
    public Boolean putIfAbsent(Long what) {
        return allocatedNumbers.putIfAbsent(what, true) == null;
    }
}
