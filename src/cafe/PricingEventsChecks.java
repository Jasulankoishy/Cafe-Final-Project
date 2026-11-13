package cafe;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/** Dependency-free behavioral checks; assertions do not require the -ea flag. */
public final class PricingEventsChecks {
    public static void main(String[] args) {
        checkPricing();
        checkPublisherAndFacade();
        checkConcreteListeners();
        System.out.println("All pricing and event checks passed.");
    }

    private static void checkPricing() {
        RegularPricing regular = new RegularPricing();
        Order order = order(1500, 500);
        check(regular.total(order(500)) == 500, "Single item price");
        check(regular.total(order) == 2000, "Multiple item subtotal");
        check(new DiscountPricing(10).total(order) == 1800, "Ten percent discount");
        check(new DiscountPricing(10).total(order(999)) == 899, "Round final total down");
        check(new DiscountPricing(10).total(order(5, 5)) == 9, "Round once, after summing items");
        check(new DiscountPricing(0).total(order) == 2000, "Zero percent discount");
        check(new DiscountPricing(100).total(order) == 0, "Full discount");
        check(new DiscountPricing(10).total(order(0)) == 0, "Free item");
        expectThrows(IllegalArgumentException.class, () -> new DiscountPricing(-1));
        expectThrows(IllegalArgumentException.class, () -> new DiscountPricing(101));
        check(new DiscountPricing(10).total(order(Integer.MAX_VALUE)) == 1932735282,
            "Discount multiplication must not overflow int");
        expectThrows(ArithmeticException.class, () -> regular.total(order(Integer.MAX_VALUE, 1)));

        // A different member can supply any Item implementation, including a decorator.
        Order.Item customItem = new Order.Item() {
            public String name() { return "Custom item with topping"; }
            public int price() { return 750; }
        };
        Order customOrder = new Order.Builder("Customer").add(customItem).build();
        check(regular.total(customOrder) == 750, "Use the Item contract");
        check(new DiscountPricing(10).total(customOrder) == 675, "Discount custom items");
        check(order.items().get(0).price() == 1500 && order.items().get(1).price() == 500,
            "Strategies must preserve item prices");
    }

    private static void checkPublisherAndFacade() {
        OrderEventPublisher events = new OrderEventPublisher();
        events.placed(order(500), 500); // No listeners is a valid configuration.
        expectThrows(NullPointerException.class, () -> events.subscribe(null));
        List<CafeFacade.Receipt> kitchen = new ArrayList<>();
        List<CafeFacade.Receipt> cashier = new ArrayList<>();
        events.subscribe((order, total) -> kitchen.add(new CafeFacade.Receipt(order, total)));
        events.subscribe((order, total) -> cashier.add(new CafeFacade.Receipt(order, total)));

        List<Order.Item> items = order(1500, 500).items();
        CafeFacade.Receipt regular = new CafeFacade(new RegularPricing(), events)
            .placeOrder("Customer", items);
        CafeFacade.Receipt discounted = new CafeFacade(new DiscountPricing(10), events)
            .placeOrder("Customer", items);

        check(regular.total() == 2000 && discounted.total() == 1800, "Facade strategy selection");
        for (List<CafeFacade.Receipt> received : List.of(kitchen, cashier)) {
            check(received.size() == 2, "Each subscriber gets each placement exactly once");
            check(received.get(0).order() == regular.order(), "Forward original regular order");
            check(received.get(1).order() == discounted.order(), "Forward original discounted order");
            check(received.get(0).total() == regular.total(), "Forward regular receipt total");
            check(received.get(1).total() == discounted.total(), "Forward discounted receipt total");
        }
    }

    private static void checkConcreteListeners() {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream original = System.out;
        try (PrintStream capture = new PrintStream(bytes, true, StandardCharsets.UTF_8)) {
            System.setOut(capture);
            OrderEventPublisher events = new OrderEventPublisher();
            events.subscribe(new KitchenListener());
            events.subscribe(new CashierListener());
            new CafeFacade(new DiscountPricing(10), events).placeOrder("Customer", List.of(
                new Order.MenuItem("Burger", 1500), new Order.MenuItem("Tea", 500)));
        } finally {
            System.setOut(original);
        }
        List<String> lines = bytes.toString(StandardCharsets.UTF_8).lines().toList();
        check(lines.equals(List.of(
            "Kitchen: prepare order for Customer: Burger, Tea",
            "Cashier: order for Customer, total: 1800 KZT")),
            "Kitchen and cashier must both display the placed order");
    }

    private static Order order(int... prices) {
        Order.Builder builder = new Order.Builder("Customer");
        for (int price : prices) {
            builder.add(new Order.MenuItem("Item", price));
        }
        return builder.build();
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void expectThrows(Class<? extends RuntimeException> expected, Runnable action) {
        try {
            action.run();
        } catch (RuntimeException exception) {
            if (expected.isInstance(exception)) {
                return;
            }
            throw new AssertionError("Expected " + expected.getSimpleName(), exception);
        }
        throw new AssertionError("Expected " + expected.getSimpleName());
    }
}
