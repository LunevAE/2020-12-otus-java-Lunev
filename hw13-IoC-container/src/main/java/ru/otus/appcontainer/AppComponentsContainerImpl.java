package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.appcontainer.exceptions.ClassNotFoundException;
import ru.otus.appcontainer.exceptions.InstanceCreationException;
import ru.otus.appcontainer.exceptions.MethodInvokeException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        final Object instance = getInstanceClass(configClass);
        var sortedMethods = sortMethodsByOrder(configClass);
        for (Method method : sortedMethods) {
            var componentName = method.getDeclaredAnnotation(AppComponent.class).name();
            var args = getArgsForMethod(method);
            try {
                var component = method.invoke(instance, args);
                appComponentsByName.put(componentName, component);
                appComponents.add(component);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new MethodInvokeException(e);
            }
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    private Object getInstanceClass(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException
                | IllegalAccessException
                | InvocationTargetException
                | NoSuchMethodException e) {
            throw new InstanceCreationException(e);
        }
    }

    private List<Method> sortMethodsByOrder(Class<?> clazz) {
        return Stream.of(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparingInt(
                        method -> method.getAnnotation(AppComponent.class).order()))
                .collect(Collectors.toList());
    }

    private Object[] getArgsForMethod(Method method) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        return Stream.of(parameterTypes)
                .map(this::findObject)
                .toArray();
    }

    private Object findObject(Class<?> param) {
        return appComponents.stream()
                .filter(component -> param.isAssignableFrom(component.getClass()))
                .findFirst()
                .orElseThrow(() -> new ClassNotFoundException("Class not found: " + param.getName()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <C> C getAppComponent(final Class<C> componentClass) {
        return (C) findObject(componentClass);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) appComponentsByName.get(componentName);
    }
}