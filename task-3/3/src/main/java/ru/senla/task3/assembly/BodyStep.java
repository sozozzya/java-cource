package ru.senla.task3.assembly;

public class BodyStep implements ILineStep {
    @Override
    public IProductPart buildProductPart() {
        System.out.println("Building car body...");
        return new Body();
    }
}
