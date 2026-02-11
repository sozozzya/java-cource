package flowershop;

public abstract class Flower {
    protected String name;
    protected double price;

    public Flower(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public String getName(int count) {
        if (count == 1) {
            return name;
        } else {
            return name + "s";
        }
    }
}
