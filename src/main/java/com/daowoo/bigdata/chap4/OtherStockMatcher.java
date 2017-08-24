package com.daowoo.bigdata.chap4;

/**
 * Created by apple on 24/08/2017.
 */
public class OtherStockMatcher extends BlueChipStockMatcher{
    @Override
    public boolean matches(Object symbol) {
        return !super.matches(symbol);
    }
}
