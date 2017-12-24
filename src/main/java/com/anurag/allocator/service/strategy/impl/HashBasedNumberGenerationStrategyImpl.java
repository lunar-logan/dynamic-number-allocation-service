package com.anurag.allocator.service.strategy.impl;

import com.anurag.allocator.common.HashFunction;
import com.anurag.allocator.common.impl.StringToIntHashFunctionImpl;
import com.anurag.allocator.service.strategy.NumberGenerationStrategy;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class HashBasedNumberGenerationStrategyImpl implements NumberGenerationStrategy {
    private final long offset;
    private final long length;
    private Random rng = new SecureRandom();
    private HashFunction<String, Integer> hashFunction;

    public HashBasedNumberGenerationStrategyImpl(long offset, long len, HashFunction<String, Integer> hashFunction) {
        this.offset = offset;
        this.length = len;
        this.hashFunction = hashFunction;
    }


    public HashBasedNumberGenerationStrategyImpl(long offset, long len) {
        this(offset, len, new StringToIntHashFunctionImpl());
    }

    public long getOffset() {
        return offset;
    }

    public long getLength() {
        return length;
    }

    public HashFunction<String, Integer> getHashFunction() {
        return hashFunction;
    }

    public <T extends HashFunction<String, Integer>> void setHashFunction(T hashFunction) {
        this.hashFunction = hashFunction;
    }

    @Override
    public Long nextNumber(long id) {
        Integer value = hashFunction.hash(String.format("%d-%d", id, rng.nextLong()));
        return offset + Math.abs(value % length);
    }

    @Override
    public boolean validate(long num) {
        return num >= offset && num <= (offset + length);
    }

    public static void main(String[] args) {
        NumberGenerationStrategy strategy = new HashBasedNumberGenerationStrategyImpl(2, 50);

        Set<Long> s = new HashSet<>();
        for (int i = 0; i < 52; i++) {
            s.add(strategy.nextNumber(i));
        }

        System.out.println(s.size());
    }
}
