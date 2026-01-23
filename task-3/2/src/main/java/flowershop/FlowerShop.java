package flowershop;

import java.util.Random;

public class FlowerShop {
    public static void main(String[] args) {
        Random random = new Random();

        int roseCount = 1 + random.nextInt(5);
        int tulipCount = 1 + random.nextInt(5);
        int chamomileCount = 1 + random.nextInt(5);

        Flower rose = new Rose();
        Flower tulip = new Tulip();
        Flower chamomile = new Chamomile();

        double totalPrice = roseCount * rose.getPrice()
                + tulipCount * tulip.getPrice()
                + chamomileCount * chamomile.getPrice();

        System.out.println("Bouquet composition:");
        System.out.println("- " + roseCount + " " + rose.getName(roseCount));
        System.out.println("- " + tulipCount + " " + tulip.getName(tulipCount));
        System.out.println("- " + chamomileCount + " " + chamomile.getName(chamomileCount));
        System.out.println("The total cost of the bouquet: " + totalPrice + " RUB");
    }
}