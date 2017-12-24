package com.anurag.allocator.service.strategy.impl;


import com.anurag.allocator.service.strategy.NumberGenerationStrategy;

public class OffsetBasedNumberGenerationStrategyImpl implements NumberGenerationStrategy {
    private final long offset;

    public OffsetBasedNumberGenerationStrategyImpl(long offset) {
        this.offset = offset;
    }

    @Override
    public Long nextNumber(long id) {
        return offset + id;
    }

    @Override
    public boolean validate(long num) {
        return num >= offset;
    }
}
