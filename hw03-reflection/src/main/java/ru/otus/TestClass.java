package ru.otus;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

public class TestClass {
    @Before
    void setUp(){
        System.out.println("setUp");
    }

    @Test
    void test1() {
        System.out.println("test1");
    }

    @Test
    void test2() {
        System.out.println("test2");
        throw new IllegalArgumentException();
    }

    @Test
    void test3() {
        System.out.println("test3");
    }

    @After
    void tearDown() {
        System.out.println("tearDown");
    }
}
