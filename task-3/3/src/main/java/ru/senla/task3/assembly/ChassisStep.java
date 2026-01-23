package ru.senla.task3.assembly;

public class ChassisStep implements ILineStep {
    @Override
    public IProductPart buildProductPart() {
        System.out.println("Building car chassis...");
        return new Chassis();
    }
}
