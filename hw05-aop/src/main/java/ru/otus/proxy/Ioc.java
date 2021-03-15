package ru.otus.proxy;

import java.lang.reflect.Proxy;

public class Ioc {

    private Ioc() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T createCalculator(T obj) {
        return (T) Proxy.newProxyInstance(
                Ioc.class.getClassLoader(),
                obj.getClass().getInterfaces(),
                new LogInvocationHandler(obj)
        );
    }
}
