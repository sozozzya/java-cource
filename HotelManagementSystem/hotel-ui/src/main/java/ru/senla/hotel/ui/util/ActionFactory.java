package ru.senla.hotel.ui.util;

import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.di.processor.DependencyInjector;
import ru.senla.hotel.ui.menu.IAction;

@Component
public class ActionFactory {

    @Inject
    private DependencyInjector di;

    public <T extends IAction> T get(Class<T> actionClass) {
        return di.getBean(actionClass);
    }

    public <T> T inject(T instance) {
        di.injectDependencies(instance);
        return instance;
    }
}
