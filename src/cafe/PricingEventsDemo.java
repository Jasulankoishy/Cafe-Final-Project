package cafe;

import java.util.List;

/** Member 2's standalone demo; the team's Main stays unchanged. */
public final class PricingEventsDemo {
    public static void main(String[] args) {
        List<Order.Item> items = List.of(
            new Order.MenuItem("Burger", 1500),
            new Order.MenuItem("Tea", 500)
        );
        Order order = new Order.Builder("Zhasulan")
            .add(items.get(0)).add(items.get(1)).build();

        CafeFacade.Pricing regular = new RegularPricing();
        CafeFacade.Pricing discounted = new DiscountPricing(10);
        System.out.println("Same order, regular pricing: " + regular.total(order) + " KZT");
        System.out.println("Same order, 10% discount: " + discounted.total(order) + " KZT");

        OrderEventPublisher events = new OrderEventPublisher();
        events.subscribe(new KitchenListener());
        events.subscribe(new CashierListener());

        // CafeFacade selects its strategy through its constructor, not a setter.
        CafeFacade regularCafe = new CafeFacade(regular, events);
        CafeFacade discountCafe = new CafeFacade(discounted, events);

        System.out.println("\nPlacing with regular pricing:");
        CafeFacade.Receipt regularReceipt = regularCafe.placeOrder(order.customer(), items);
        System.out.println("Receipt: " + regularReceipt.total() + " KZT");

        System.out.println("\nPlacing with 10% discount:");
        CafeFacade.Receipt discountReceipt = discountCafe.placeOrder(order.customer(), items);
        System.out.println("Receipt: " + discountReceipt.total() + " KZT");
    }
}
