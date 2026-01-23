package ru.senla.task3.assembly;

public class CarAssemblyLine implements IAssemblyLine {
    private final ILineStep bodyStep;
    private final ILineStep chassisStep;
    private final ILineStep engineStep;

    public CarAssemblyLine(ILineStep bodyStep, ILineStep chassisStep, ILineStep engineStep) {
        this.bodyStep = bodyStep;
        this.chassisStep = chassisStep;
        this.engineStep = engineStep;
    }

    @Override
    public IProduct assembleProduct(IProduct product) {
        System.out.println("Starting car assembly process...");

        try {
            IProductPart body = bodyStep.buildProductPart();
            Thread.sleep(1000);
            product.installFirstPart(body);

            IProductPart chassis = chassisStep.buildProductPart();
            Thread.sleep(1000);
            product.installSecondPart(chassis);

            IProductPart engine = engineStep.buildProductPart();
            Thread.sleep(1000);
            product.installThirdPart(engine);

        } catch (InterruptedException e) {
            System.out.println("Assembly process interrupted!");
            Thread.currentThread().interrupt();
        }

        System.out.println("Car assembly completed!");
        return product;
    }
}
