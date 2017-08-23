package com.daowoo.bigdata.chap4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by apple on 23/08/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class StockBrokerTestOne {
    @Mock MarketWatcher marketWatcher;
    @Mock
    Portfolio portfolio;

    @Test
    public void marketWatcher_Returns_current_stock_status() {
        Stock uvsityCorp = new Stock("UV", "Uvsity Corporation", new BigDecimal("100.00"));

        when(marketWatcher.getQuote(anyString())).
                thenReturn(uvsityCorp);

        assertNotNull(marketWatcher.getQuote("UV"));
    }
}
