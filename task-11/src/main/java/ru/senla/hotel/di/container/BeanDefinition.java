package ru.senla.hotel.di.container;

import ru.senla.hotel.di.annotation.Scope;

public class BeanDefinition {

    private final Class<?> implClass;
    private final Scope scope;
    private Object instance;

    public BeanDefinition(Class<?> implClass, Scope scope) {
        this.implClass = implClass;
        this.scope = scope;
    }

    public Class<?> getImplClass() {
        return implClass;
    }

    public Scope getScope() {
        return scope;
    }

    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }
}
