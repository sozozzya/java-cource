package ru.senla.hotel.presentation.console.util;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.senla.hotel.presentation.console.actions.IAction;

@Component
public class ActionFactory {

    private final ApplicationContext context;

    public ActionFactory(ApplicationContext context) {
        this.context = context;
    }

    public <T extends IAction> T get(Class<T> type) {
        return context.getBean(type);
    }
}
