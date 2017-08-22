package com.daowoo.bigdata.chap1;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;
import org.junit.rules.TestName;


/**
 * Created by apple on 22/08/2017.
 */
public class ExternalResourceTest {
    Resource resource;
    public @Rule TestName name = new TestName();

    public @Rule
    ExternalResource rule = new ExternalResource() {
        @Override protected void before() throws Throwable {
            resource = new Resource();
            resource.open();
            System.out.println(name.getMethodName());
        }

        @Override protected void after()  {
            resource.close();
            System.out.println("\n");
        }
    };

    @Test
    public void someTest() throws Exception {
        System.out.println(resource.get());
    }

    @Test
    public void someTest2() throws Exception {
        System.out.println(resource.get());
    }
}
