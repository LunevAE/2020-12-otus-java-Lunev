package ru.otus.proxy;

import ru.otus.annotations.Log;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LogInvocationHandler implements InvocationHandler {

    private final Object obj;
    private final Set<String> logMethods;

    LogInvocationHandler(Object obj) {
        this.obj = obj;
        this.logMethods = filterMethodsByAnnotation(obj.getClass().getMethods(), Log.class);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        logInfo(method, args);
        return method.invoke(obj, args);
    }

    private void logInfo(Method method, Object[] args) {
        String methodParams = getMethodParams(method);
        if (logMethods.contains(methodParams)) {
            System.out.printf("executed method: %s, params: %s%n", method.getName(), argsAsString(args));
        }
    }

    public Set<String> filterMethodsByAnnotation(Method[] methods, Class<? extends Annotation> annotation) {
        Predicate<Method> predicate = (method) -> method.isAnnotationPresent(annotation);
        return Arrays.stream(methods)
                .filter(predicate)
                .map(LogInvocationHandler::getMethodParams)
                .collect(Collectors.toSet());
    }

    private String argsAsString(Object[] args) {
        return Arrays.stream(args)
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
    }

    private static String getMethodParams(Method method) {
        return method.getName() + Arrays.asList(method.getParameterTypes()).toString();
    }
}
