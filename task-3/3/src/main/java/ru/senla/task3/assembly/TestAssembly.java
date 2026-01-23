package ru.senla.task3.assembly;

public class TestAssembly {
    public static void main(String[] args) {
        System.out.println("Initializing car assembly line...\n");

        ILineStep bodyStep = new BodyStep();
        ILineStep chassisStep = new ChassisStep();
        ILineStep engineStep = new EngineStep();

        IAssemblyLine assemblyLine = new CarAssemblyLine(bodyStep, chassisStep, engineStep);

        IProduct car = new Car();

        IProduct finishedCar = assemblyLine.assembleProduct(car);

        System.out.println("\nResult:");
        System.out.println(finishedCar);
    }
}