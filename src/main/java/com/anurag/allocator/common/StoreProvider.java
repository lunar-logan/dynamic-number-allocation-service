package com.anurag.allocator.common;

/**
 * A {@code Provider} interface for storage. The implementation <b>must</b> guarantee the <b>atomicity</b>
 * of each implemented method
 *
 * @param <T>
 */
public interface StoreProvider<T> {
    /**
     * Atomically gets a new {@code Long} id
     *
     * @return a newly generated id
     */
    Long nextId();

    /**
     * Atomically saves a value in the store if it is not already present
     *
     * @return true if the new value was stored, or false in case the value was already present
     */
    Boolean putIfAbsent(T what);
}
