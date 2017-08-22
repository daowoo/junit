package com.daowoo.bigdata.chap1;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Created by apple on 21/08/2017.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestExecutionOrder {
    @Test
    public void edit() throws Exception {
        System.out.println("edit executed");
    }
    @Test   public void create() throws Exception {
        System.out.println("create executed");
    }
    @Test   public void remove() throws Exception {
        System.out.println("remove executed");
    }
}
