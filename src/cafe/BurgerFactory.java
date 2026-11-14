package cafe;

import java.util.Objects;

/**
 * Concrete Factory for creating Burger items.
 */
public class BurgerFactory extends MenuItemFactory {
    private final String name;
    private final int price;

    public BurgerFactory() {
        this("Burger", 1500);
    }

    public BurgerFactory(String name, int price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        this.name = Objects.requireNonNull(name, "Name cannot be null.");
        this.price = price;
    }

    @Override
    public Order.Item createItem() {
        return new Order.MenuItem(name, price);
    }
}
