package com.github.redfox197.di;

import com.github.redfox197.di.annotation.Autowired;
import com.github.redfox197.di.annotation.Bean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Una semplice implementazione di un Dependency Injection Container
 * Come si può osservare si stanno ignorando vari aspetti come adottare un approccio Thread-Safe
 */
public class DIContainer {
    private final Map<Class<?>, Object> container = new HashMap<>();

    public void register(ClassLoader classLoader, String packageName) {
        findBeans(classLoader, packageName)
                .forEach(this::getInstanceOfBean);
    }

    public Set<Class<?>> findBeans(ClassLoader classLoader, String packageName) {
        Set<Class<?>> beans = new HashSet<>();

        try (
                InputStream inputStream = classLoader.getResourceAsStream(packageName.replace('.', '/'));
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.endsWith(".class")) {
                    String className = line.substring(0, line.length() - 6);
                    Class<?> clazz = Class.forName(packageName + "." + className);
                    if (clazz.isAnnotationPresent(Bean.class)) {
                        beans.add(clazz);
                    }
                }
            }
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }

        return beans;
    }

    public <T> T getInstanceOfBean(Class<T> clazz) {
        try {
            if (!clazz.isAnnotationPresent(Bean.class))
                throw new IllegalArgumentException();

            if (container.containsKey(clazz)) {
                return (T) container.get(clazz);
            }

            T instance = instantiate(clazz);
            container.putIfAbsent(clazz, instance);

            return instance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T instantiate(Class<T> clazz) throws Exception {
        Constructor<T> constructor = clazz.getDeclaredConstructor();
        T instance = constructor.newInstance();

        for (Field declaredField : clazz.getDeclaredFields()) {
            if (declaredField.isAnnotationPresent(Autowired.class)) {
                declaredField.setAccessible(true);
                declaredField.set(instance, getInstanceOfBean(declaredField.getType()));
            }
        }

        return instance;
    }

}
