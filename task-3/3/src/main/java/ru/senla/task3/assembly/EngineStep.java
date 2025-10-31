package ru.senla.task3.assembly;

public class EngineStep implements ILineStep {
    @Override
    public IProductPart buildProductPart() {
        System.out.println("Building car engine...");
        return new Engine();
    }
}
