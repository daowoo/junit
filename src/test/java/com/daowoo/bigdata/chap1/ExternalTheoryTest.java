package com.daowoo.bigdata.chap1;

import org.junit.experimental.theories.ParametersSuppliedBy;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Created by apple on 22/08/2017.
 */
@RunWith(Theories.class)
public class ExternalTheoryTest {
    @Theory
    public void adds_numbers(
            @ParametersSuppliedBy(NumberSupplier.class) Number num1,
            @ParametersSuppliedBy(NumberSupplier.class) Number num2) {
        Adder anAdder = new Adder();
        double expectedSum = num1.doubleValue()+num2.doubleValue();
        double actualResult = (Double)anAdder.add(num1, num2);
        assertEquals(actualResult, expectedSum, 0.01);
    }
}
