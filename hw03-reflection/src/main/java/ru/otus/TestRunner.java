package ru.otus;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TestRunner {
    private final Class<?> testClass;

    private final List<Method> beforeMethods;
    private final List<Method> afterMethods;
    private final List<Method> testMethods;

    private int testCnt = 0;
    private int failedTestsCnt = 0;
    private int successfulTestsCnt = 0;

    public TestRunner(String classWithTests) throws ClassNotFoundException {
        testClass = Class.forName(classWithTests);
        Method[] methods = testClass.getDeclaredMethods();

        beforeMethods = Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(Before.class))
                .collect(Collectors.toList());

        afterMethods = Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(After.class))
                .collect(Collectors.toList());

        testMethods = Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(Test.class))
                .collect(Collectors.toList());
    }

    public void run() throws Exception {
        for (Method testMethod : testMethods) {
            invokeTest(testMethod);
        }
        printStats();
    }

    private void invokeTest(Method testMethod) throws Exception {
        Object instance = testClass.getDeclaredConstructor().newInstance();
        try {
            System.out.println("***TEST " + ++testCnt + " BEGIN***");
            for (Method beforeMethod : beforeMethods) {
                beforeMethod.invoke(instance);
            }

            testMethod.invoke(instance);
            System.out.println("***TEST " + testCnt + " PASSED***");
            successfulTestsCnt++;
        } catch (Exception e) {
            System.out.println("***TEST " + testCnt + " FAILED***");
            failedTestsCnt++;
        } finally {
            for (Method afterMethod : afterMethods) {
                afterMethod.invoke(instance);
            }
            System.out.println();
        }
    }

    private void printStats() {
        System.out.printf("\n***RESULTS***\nTOTAL TESTS: %s\nSUCCESSFUL TESTS: %s\n" +
                "FAILED TESTS: %s\n", testCnt, successfulTestsCnt, failedTestsCnt);
    }
}
