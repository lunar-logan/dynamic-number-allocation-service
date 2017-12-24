package com.anurag.allocator.common.impl;

import com.anurag.allocator.common.HashFunction;
import com.google.common.hash.Hashing;

public class StringToIntHashFunctionImpl implements HashFunction<String, Integer> {
    @Override
    public Integer hash(String value) {
        return Hashing.murmur3_128()
                .hashBytes(value.getBytes())
                .asInt();
    }
}
