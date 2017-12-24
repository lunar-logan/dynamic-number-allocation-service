package com.anurag.allocator.service;

/**
 * @see BasicAllocationService
 */
public interface AllocationService {
    /**
     * Allocates a new number. If the preference is provided and is available, then it is allocated,
     * else it generates a new number based upon its config.
     *
     * @param pref the preferred number, {@code null} in case of no preference
     * @return the newly allocated number
     */
    String allocate(Long pref);

    String serviceKey();
}
