package com.daowoo.bigdata.chap1;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;

/**
 * Created by apple on 22/08/2017.
 */
public class LessThanOrEqual<T extends Comparable<T>> extends BaseMatcher<Comparable<T>> {
    private final Comparable<T> expectedValue;

    public LessThanOrEqual(T expectedValue) {
        this.expectedValue = expectedValue;
    }



    public void describeTo(Description description) {
        description.appendText(" less than or equal(<=) "+expectedValue);
    }



    public boolean matches(Object t) {
        int compareTo = expectedValue.compareTo((T)t);
        return compareTo > -1;
    }

    @Factory
    public static<T extends Comparable<T>> Matcher<T>
    lessThanOrEqual(T t) {
        return new LessThanOrEqual(t);
    }
}
