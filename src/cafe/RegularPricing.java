package cafe;

/** Charges the sum of the item prices, in whole KZT. */
public final class RegularPricing implements CafeFacade.Pricing {
    @Override
    public int total(Order order) {
        long subtotal = order.items().stream().mapToLong(Order.Item::price).sum();
        return Math.toIntExact(subtotal);
    }
}
