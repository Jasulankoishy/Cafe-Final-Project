package cafe;

import java.util.List;

/**
 * Integrated Demonstration of the Cafe Ordering System.
 * 
 * Showcases the coordinated interaction of all 6 design patterns:
 * 1. Factory Method : MenuItemFactory (BurgerFactory, TeaFactory) creates base items.
 * 2. Decorator      : CheeseExtra, BaconExtra dynamically augment items with toppings.
 * 3. Builder        : Order.Builder constructs and validates immutable orders.
 * 4. Facade         : CafeFacade orchestrates order creation, pricing, receipt, and events.
 * 5. Strategy       : RegularPricing and DiscountPricing calculate final costs.
 * 6. Observer       : OrderEventPublisher broadcasts events to KitchenListener and CashierListener.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("     CAFE ORDERING SYSTEM - DESIGN PATTERNS DEMO  ");
        System.out.println("==================================================");

        // Pattern 1: Factory Method - create base items
        MenuItemFactory burgerFactory = new BurgerFactory();
        MenuItemFactory teaFactory = new TeaFactory();

        Order.Item baseBurger = burgerFactory.createItem();
        Order.Item baseTea = teaFactory.createItem();
        System.out.println("\n[1. Factory Method]");
        System.out.println("Created base item: " + baseBurger.name() + " (" + baseBurger.price() + " KZT)");
        System.out.println("Created base item: " + baseTea.name() + " (" + baseTea.price() + " KZT)");

        // Pattern 2: Decorator - wrap item with toppings
        Order.Item customBurger = new BaconExtra(new CheeseExtra(baseBurger));
        System.out.println("\n[2. Decorator]");
        System.out.println("Decorated item: " + customBurger.name() + " (" + customBurger.price() + " KZT)");

        // Pattern 5: Observer - set up publisher and register listeners
        OrderEventPublisher publisher = new OrderEventPublisher();
        publisher.subscribe(new KitchenListener());
        publisher.subscribe(new CashierListener());
        System.out.println("\n[3. Observer]");
        System.out.println("Subscribed KitchenListener and CashierListener to OrderEventPublisher.");

        // Pattern 4: Strategy & Pattern 6: Facade - configure CafeFacade with discount strategy
        CafeFacade.Pricing discountPricing = new DiscountPricing(10); // 10% student/happy-hour discount
        CafeFacade cafe = new CafeFacade(discountPricing, publisher);
        System.out.println("\n[4. Strategy & Facade]");
        System.out.println("Initialized CafeFacade with 10% DiscountPricing and event publisher.");

        // Place order (Pattern 3: Builder is utilized inside CafeFacade.placeOrder)
        System.out.println("\n[5. Placing Order via Facade]");
        List<Order.Item> orderItems = List.of(customBurger, baseTea);
        CafeFacade.Receipt receipt = cafe.placeOrder("Zhasulan", orderItems);

        // Print final receipt
        System.out.println("\n[6. Customer Receipt]");
        System.out.println("Customer: " + receipt.order().customer());
        for (Order.Item item : receipt.order().items()) {
            System.out.printf(" - %-30s %5d KZT%n", item.name(), item.price());
        }
        System.out.println("--------------------------------------------------");
        System.out.printf("Final Total (after discount):          %5d KZT%n", receipt.total());
        System.out.println("==================================================");
    }
}
