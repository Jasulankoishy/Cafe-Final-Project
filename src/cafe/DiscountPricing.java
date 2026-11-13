package cafe;

/** Applies an integer percentage discount and rounds the final total down. */
public final class DiscountPricing implements CafeFacade.Pricing {
    private final int discountPercent;

    public DiscountPricing(int discountPercent) {
        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("Discount must be between 0 and 100 percent.");
        }
        this.discountPercent = discountPercent;
    }

    @Override
    public int total(Order order) {
        long subtotal = order.items().stream().mapToLong(Order.Item::price).sum();
        long discounted = Math.floorDiv(Math.multiplyExact(subtotal, 100 - discountPercent), 100);
        return Math.toIntExact(discounted);
    }
}
