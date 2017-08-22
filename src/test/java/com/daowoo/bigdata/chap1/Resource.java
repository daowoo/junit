package com.daowoo.bigdata.chap1;

/**
 * Created by apple on 22/08/2017.
 */
class Resource{
    public void open() {
        System.out.println("Opened");
    }

    public void close() {
        System.out.println("Closed");
    }

    public double get() {
        return Math.random();
    }
}
