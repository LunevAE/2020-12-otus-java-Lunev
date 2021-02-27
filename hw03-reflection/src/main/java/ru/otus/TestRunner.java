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

    int testCnt = 0;
    int failedTestsCnt = 0;
    int successfulTestsCnt = 0;

    public TestRunner(String classWithTests) throws ClassNotFoundException {
        testClass = Class.forName(classWithTests);
        Method[] methods = testClass.getDeclaredMethods();

        beforeMethods = Arrays.stream(methods)
                .filter(method -> method.getAnnotation(Before.class) != null)
                .collect(Collectors.toList());

        afterMethods = Arrays.stream(methods)
                .filter(method -> method.getAnnotation(After.class) != null)
                .collect(Collectors.toList());

        testMethods = Arrays.stream(methods)
                .filter(method -> method.getAnnotation(Test.class) != null)
                .collect(Collectors.toList());
    }

    public void run() throws Exception {
        for (Method testMethod : testMethods) {
            invokeTest(testClass, beforeMethods, afterMethods, testMethod);
        }
        printStats(testCnt, successfulTestsCnt, failedTestsCnt);
    }

    private void invokeTest(Class<?> testClass, List<Method> beforeMethods,
                            List<Method> afterMethods, Method testMethod) throws Exception {
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

    private void printStats(int testCnt, int successfulTestsCnt, int failedTestsCnt) {
        System.out.printf("\n***RESULTS***\nTOTAL TESTS: %s\nSUCCESSFUL TESTS: %s\n" +
                "FAILED TESTS: %s\n", testCnt, successfulTestsCnt, failedTestsCnt);
    }
}
