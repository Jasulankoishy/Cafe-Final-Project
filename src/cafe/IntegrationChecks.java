package cafe;

import cafe.builder.Order;
import cafe.facade.CafeFacade;
import cafe.factory.MenuItemFactory;
import cafe.factory.BurgerFactory;
import cafe.factory.TeaFactory;
import cafe.decorator.CheeseExtra;
import cafe.decorator.BaconExtra;
import cafe.strategy.DiscountPricing;
import cafe.observer.OrderEventPublisher;

import java.util.ArrayList;
import java.util.List;

/**
 * End-to-end integration checks confirming that all 6 patterns interact correctly.
 */
public final class IntegrationChecks {
    public static void main(String[] args) {
        checkIntegratedFlow();
        System.out.println("All integrated system checks passed.");
    }

    private static void checkIntegratedFlow() {
        // 1. Factory Method
        MenuItemFactory burgerFactory = new BurgerFactory();
        MenuItemFactory teaFactory = new TeaFactory();
        Order.Item burger = burgerFactory.createItem();
        Order.Item tea = teaFactory.createItem();

        // 2. Decorator
        Order.Item decoratedBurger = new BaconExtra(new CheeseExtra(burger));
        if (decoratedBurger.price() != 2000) {
            throw new AssertionError("Decorated burger price should be 2000 KZT, got " + decoratedBurger.price());
        }
        if (!"Burger + Cheese + Bacon".equals(decoratedBurger.name())) {
            throw new AssertionError("Decorated burger name should be 'Burger + Cheese + Bacon', got " + decoratedBurger.name());
        }

        // 3. Observer
        List<Order> kitchenOrders = new ArrayList<>();
        List<Integer> cashierTotals = new ArrayList<>();
        OrderEventPublisher publisher = new OrderEventPublisher();
        publisher.subscribe((order, total) -> kitchenOrders.add(order));
        publisher.subscribe((order, total) -> cashierTotals.add(total));

        // 4. Strategy & 5. Facade (which uses 6. Builder internally)
        CafeFacade cafe = new CafeFacade(new DiscountPricing(20), publisher); // 20% discount
        CafeFacade.Receipt receipt = cafe.placeOrder("Jasulan", List.of(decoratedBurger, tea));

        // Total before discount: 2000 + 500 = 2500 KZT.
        // Total after 20% discount: 2500 * 0.8 = 2000 KZT.
        if (receipt.total() != 2000) {
            throw new AssertionError("Expected receipt total 2000 KZT, got " + receipt.total());
        }
        if (kitchenOrders.size() != 1 || !"Jasulan".equals(kitchenOrders.get(0).customer())) {
            throw new AssertionError("Kitchen did not receive correct order notification.");
        }
        if (cashierTotals.size() != 1 || cashierTotals.get(0) != 2000) {
            throw new AssertionError("Cashier did not receive correct total notification.");
        }
    }
}
