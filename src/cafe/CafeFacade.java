package cafe;

import java.util.List;

public class CafeFacade {
    public interface Pricing {
        int total(Order order);
    }

    public interface OrderEvents {
        void placed(Order order, int total);
    }

    public record Receipt(Order order, int total) {}

    private final Pricing pricing;
    private final OrderEvents events;

    public CafeFacade(Pricing pricing, OrderEvents events) {
        this.pricing = pricing;
        this.events = events;
    }

    public Receipt placeOrder(String customer, List<Order.Item> items) {
        Order.Builder builder = new Order.Builder(customer);
        items.forEach(builder::add);
        Order order = builder.build();
        int total = pricing.total(order);
        Receipt receipt = new Receipt(order, total);
        events.placed(order, total);
        return receipt;
    }
}
