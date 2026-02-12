package ru.senla.hotel.di.container;

import ru.senla.hotel.di.annotation.Scope;
import ru.senla.hotel.exception.DIException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BeanRegistry {

    private final Map<Class<?>, BeanDefinition> beans = new HashMap<>();

    public void register(Class<?> implClass, Scope scope) {
        beans.put(implClass, new BeanDefinition(implClass, scope));
    }

    public void register(Class<?> abstraction, Class<?> implClass, Scope scope) {
        if (beans.containsKey(abstraction)) {
            throw new DIException("Multiple implementations for " + abstraction.getName());
        }
        beans.put(abstraction, new BeanDefinition(implClass, scope));
    }

    public void registerInstance(Class<?> type, Object instance) {
        BeanDefinition def = new BeanDefinition(type, Scope.SINGLETON);
        def.setInstance(instance);
        beans.put(type, def);
    }

    public BeanDefinition get(Class<?> type) {
        return beans.get(type);
    }

    public Collection<BeanDefinition> getAll() {
        return beans.values();
    }
}
