package com.anurag.allocator.common;

public interface HashFunction<T, R> {
    R hash(T value);
}
