package com.daowoo.bigdata.chap4;
import org.mockito.ArgumentMatcher;

/**
 * Created by apple on 24/08/2017.
 */
public  class BlueChipStockMatcher extends ArgumentMatcher<String>{
    @Override
    public boolean matches(Object symbol) {
        return "FB".equals(symbol) ||
                "AAPL".equals(symbol);
    }
}
