package com.anurag.allocator.service;

import com.anurag.allocator.common.StoreProvider;
import com.anurag.allocator.service.strategy.NumberGenerationStrategy;

public class BasicAllocationService implements AllocationService {
    private final StoreProvider storeProvider;
    private NumberGenerationStrategy numberGenerationStrategy;

    public BasicAllocationService(NumberGenerationStrategy numberStrategy, StoreProvider storeProvider) {
        this.numberGenerationStrategy = numberStrategy;
        this.storeProvider = storeProvider;
    }

    public NumberGenerationStrategy getNumberGenerationStrategy() {
        return numberGenerationStrategy;
    }

    public void setNumberGenerationStrategy(NumberGenerationStrategy numberGenerationStrategy) {
        this.numberGenerationStrategy = numberGenerationStrategy;
    }

    @Override
    public String allocate(Long pref) {
        if (pref == null || !numberGenerationStrategy.validate(pref)) return noPrefAllocation();
        return preferredAllocation(pref);
    }

    @Override
    public String serviceKey() {
        return "BasicAllocationService";
    }

    protected String preferredAllocation(Long pref) {
        if (storeProvider.putIfAbsent(pref)) {
            return format(pref);
        }
        return noPrefAllocation();
    }

    /**
     * Allocates a new number without any preference
     *
     * @return
     */
    protected String noPrefAllocation() {
        Long result;
        do {
            result = nextNumber(nextId());
            if (null == result) return "-1";
        } while (!storeProvider.putIfAbsent(result));
        return format(result);
    }

    protected String format(long number) {
        long f3 = number / 10000000;
        long mid3 = (number / 10000) % 1000;
        long l4 = number % 10000;
        return String.format("%03d-%03d-%04d", f3, mid3, l4);
    }

    /**
     * Generates a new number from the given id
     *
     * @param fromId
     * @return
     */
    protected Long nextNumber(long fromId) {
        return numberGenerationStrategy.nextNumber(fromId);
    }

    /**
     * Generates a new id
     *
     * @return
     */
    protected Long nextId() {
        return storeProvider.nextId();
    }
}
