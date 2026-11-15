package cafe.decorator;

import cafe.builder.Order;

/**
 * Concrete Decorator adding cheese topping to an item.
 */
public final class CheeseExtra extends ItemDecorator {
    private final int extraPrice;

    public CheeseExtra(Order.Item wrapped) {
        this(wrapped, 200);
    }

    public CheeseExtra(Order.Item wrapped, int extraPrice) {
        super(wrapped);
        if (extraPrice < 0) {
            throw new IllegalArgumentException("Extra price cannot be negative.");
        }
        this.extraPrice = extraPrice;
    }

    @Override
    public String name() {
        return wrapped.name() + " + Cheese";
    }

    @Override
    public int price() {
        return Math.addExact(wrapped.price(), extraPrice);
    }
}
