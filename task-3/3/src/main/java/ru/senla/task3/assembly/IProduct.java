package ru.senla.task3.assembly;

public interface IProduct {
    void installFirstPart(IProductPart part);

    void installSecondPart(IProductPart part);

    void installThirdPart(IProductPart part);
}
