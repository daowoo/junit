package com.daowoo.bigdata.chap4;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertNotNull;

/**
 * Created by apple on 23/08/2017.
 */
public class StockBrokerTest {
    @Mock
    MarketWatcher marketWatcher;
    @Mock
    Portfolio portfolio;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void sanity() throws Exception {
        assertNotNull(marketWatcher);
        assertNotNull(portfolio);
    }
}
