package com.anurag.allocator;

import com.anurag.allocator.common.impl.RedisStoreProviderImplTest;
import com.anurag.allocator.service.AllocationServiceTest;
import com.anurag.allocator.service.strategy.impl.BoundedNumberGenerationStrategyTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        RedisStoreProviderImplTest.class,
        BoundedNumberGenerationStrategyTest.class,
        AllocationServiceTest.class
})
public class TestSuite {
}
