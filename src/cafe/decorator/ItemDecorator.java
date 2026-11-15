package cafe.decorator;

import cafe.builder.Order;

import java.util.Objects;

/**
 * Decorator pattern: Abstract wrapper for Order.Item instances.
 */
public abstract class ItemDecorator implements Order.Item {
    protected final Order.Item wrapped;

    public ItemDecorator(Order.Item wrapped) {
        this.wrapped = Objects.requireNonNull(wrapped, "Wrapped item must not be null.");
    }

    @Override
    public String name() {
        return wrapped.name();
    }

    @Override
    public int price() {
        return wrapped.price();
    }

    public Order.Item getWrapped() {
        return wrapped;
    }
}
