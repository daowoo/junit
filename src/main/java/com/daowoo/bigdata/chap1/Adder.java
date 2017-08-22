package com.daowoo.bigdata.chap1;

/**
 * Created by apple on 22/08/2017.
 */
public class Adder {

    public Object add(Number a, Number b) {
        return a.doubleValue()+b.doubleValue();
    }

    public Object add(String a, String b) {
        return a+b;
    }
}
