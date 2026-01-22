package ru.senla.task3.assembly;

import java.util.Locale;

public class Car implements IProduct {
    private IProductPart body;
    private IProductPart chassis;
    private IProductPart engine;

    @Override
    public void installFirstPart(IProductPart part) {
        this.body = part;
        System.out.println("Installing: " + part + ".\n");
    }

    @Override
    public void installSecondPart(IProductPart part) {
        this.chassis = part;
        System.out.println("Installing: " + part + ".\n");
    }

    @Override
    public void installThirdPart(IProductPart part) {
        this.engine = part;
        System.out.println("Installing: " + part + ".\n");
    }

    @Override
    public String toString() {
        return "Car assembled with: " + body + ", " + chassis + ", " + engine + ".";
    }
}
