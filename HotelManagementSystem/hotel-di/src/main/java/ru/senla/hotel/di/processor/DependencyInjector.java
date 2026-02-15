package ru.senla.hotel.di.processor;

import ru.senla.hotel.autoconfig.util.ReflectionUtils;
import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.di.annotation.Scope;
import ru.senla.hotel.di.container.BeanDefinition;
import ru.senla.hotel.di.container.BeanRegistry;
import ru.senla.hotel.di.exception.DIException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DependencyInjector {

    private final BeanRegistry registry = new BeanRegistry();
    private final List<BeanPostProcessor> postProcessors = new ArrayList<>();

    private boolean initializing = false;
    private boolean initialized = false;

    public DependencyInjector() {
        registry.registerInstance(DependencyInjector.class, this);
    }

    public void register(Class<?>... components) {
        for (Class<?> clazz : components) {
            if (!clazz.isAnnotationPresent(Component.class)) continue;

            Component component = clazz.getAnnotation(Component.class);
            Scope scope = component.scope();

            registry.register(clazz, scope);

            for (Class<?> iFace : clazz.getInterfaces()) {
                registry.register(iFace, clazz, scope);
            }
        }
    }

    public void registerPrototype(Class<?>... classes) {
        for (Class<?> clazz : classes) {
            registry.register(clazz, Scope.PROTOTYPE);
        }
    }

    public void initialize() {
        if (initialized || initializing) return;

        initializing = true;

        for (BeanDefinition def : registry.getAll()) {
            if (BeanPostProcessor.class.isAssignableFrom(def.getImplClass())) {
                BeanPostProcessor processor = (BeanPostProcessor) createInstance(def.getImplClass(), false);
                postProcessors.add(processor);
            }
        }

        initializing = false;
        initialized = true;
    }

    public <T> T getBean(Class<T> type) {
        BeanDefinition def = registry.get(type);
        if (def == null) {
            throw new DIException("No bean registered: " + type);
        }

        if (def.getScope() == Scope.SINGLETON && def.getInstance() != null) {
            return type.cast(def.getInstance());
        }

        Object instance = createInstance(def.getImplClass(), true);

        if (def.getScope() == Scope.SINGLETON) {
            def.setInstance(instance);
        }

        return type.cast(instance);
    }

    private Object createInstance(Class<?> implClass, boolean applyPostProcessors) {
        if (!implClass.isAnnotationPresent(Component.class)) {
            throw new DIException("Class is not a component: " + implClass);
        }
        try {
            Object instance = implClass.getDeclaredConstructor().newInstance();

            injectDependencies(instance);

            if (applyPostProcessors && initialized) {
                for (BeanPostProcessor processor : postProcessors) {
                    processor.process(instance);
                }
            }

            return instance;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DIException("Failed to create bean: " + implClass, e);
        }
    }

    public void registerInstance(Class<?> type, Object instance) {
        registry.registerInstance(type, instance);
    }

    public void injectDependencies(Object target) {
        for (Field field : target.getClass().getDeclaredFields()) {
            if (!field.isAnnotationPresent(Inject.class)) continue;

            Object dependency = getBean(field.getType());
            ReflectionUtils.setField(field, target, dependency);
        }
    }
}
