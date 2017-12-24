package com.anurag.allocator.service.strategy.impl;

import com.anurag.allocator.service.strategy.NumberGenerationStrategy;
import org.junit.Assert;
import org.junit.Test;

public class BoundedNumberGenerationStrategyTest {
    @Test
    public void testBoundedness() {
        int start = 1;
        int end = 10;
        NumberGenerationStrategy strategy = new BoundedNumberGenerationStrategyDecorator(new OffsetBasedNumberGenerationStrategyImpl(start), (long) end);

        Assert.assertTrue(2 == strategy.nextNumber(1L));
        Assert.assertTrue(3L == strategy.nextNumber(2L));
        Assert.assertTrue(10 == strategy.nextNumber(9));

        Assert.assertTrue(null == strategy.nextNumber(10));
        Assert.assertTrue(null == strategy.nextNumber(11));
        Assert.assertTrue(null == strategy.nextNumber(12));
        Assert.assertTrue(null == strategy.nextNumber(1200));
    }

}