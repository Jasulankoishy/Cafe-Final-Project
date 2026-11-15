package cafe.decorator;

import cafe.builder.Order;

/**
 * Concrete Decorator adding bacon topping to an item.
 */
public final class BaconExtra extends ItemDecorator {
    private final int extraPrice;

    public BaconExtra(Order.Item wrapped) {
        this(wrapped, 300);
    }

    public BaconExtra(Order.Item wrapped, int extraPrice) {
        super(wrapped);
        if (extraPrice < 0) {
            throw new IllegalArgumentException("Extra price cannot be negative.");
        }
        this.extraPrice = extraPrice;
    }

    @Override
    public String name() {
        return wrapped.name() + " + Bacon";
    }

    @Override
    public int price() {
        return Math.addExact(wrapped.price(), extraPrice);
    }
}
