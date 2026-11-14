package cafe;

import java.util.Objects;

/**
 * Concrete Factory for creating Tea items.
 */
public class TeaFactory extends MenuItemFactory {
    private final String name;
    private final int price;

    public TeaFactory() {
        this("Tea", 500);
    }

    public TeaFactory(String name, int price) {
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
