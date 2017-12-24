package com.anurag.allocator.service;

import com.anurag.allocator.common.StoreProvider;
import com.anurag.allocator.service.strategy.NumberGenerationStrategy;

public class AllocationServiceBuilder {
    private NumberGenerationStrategy numberGenerationStrategy;
    private StoreProvider<?> storeProvider;

    public AllocationServiceBuilder() {

    }

    public AllocationServiceBuilder numberGenerationStrategy(NumberGenerationStrategy numberGenerationStrategy) {
        this.numberGenerationStrategy = numberGenerationStrategy;
        return this;
    }

    public AllocationServiceBuilder storeProvider(StoreProvider<?> storeProvider) {
        this.storeProvider = storeProvider;
        return this;
    }


    public AllocationService build() {
        return new BasicAllocationService(numberGenerationStrategy, storeProvider);
    }

}
