package cafe;

/** Applies an integer percentage discount and rounds the final total down. */
public final class DiscountPricing implements CafeFacade.Pricing {
    private final int discountPercent;
    private final RegularPricing regularPricing = new RegularPricing();

    public DiscountPricing(int discountPercent) {
        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("Discount must be between 0 and 100 percent.");
        }
        this.discountPercent = discountPercent;
    }

    @Override
    public int total(Order order) {
        long subtotal = regularPricing.total(order);
        return Math.toIntExact(Math.floorDiv(subtotal * (100 - discountPercent), 100));
    }
}
