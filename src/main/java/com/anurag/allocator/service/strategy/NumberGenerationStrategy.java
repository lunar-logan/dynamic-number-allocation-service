package com.anurag.allocator.service.strategy;

public interface NumberGenerationStrategy {
    Long nextNumber(long id);

    boolean validate(long num);
}
