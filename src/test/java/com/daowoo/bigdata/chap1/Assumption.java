package com.daowoo.bigdata.chap1;

import org.junit.Assume;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by apple on 21/08/2017.
 */
public class Assumption {

    boolean isSonarRunning = false;
    @Test
    public void very_critical_test() throws Exception {
        isSonarRunning = true;
        Assume.assumeFalse(isSonarRunning);
        assertTrue(true);
    }

}