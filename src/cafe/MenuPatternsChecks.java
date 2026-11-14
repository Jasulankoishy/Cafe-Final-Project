package cafe;

import java.util.List;

/** Dependency-free behavioral checks for Factory Method and Decorator. */
public final class MenuPatternsChecks {
    public static void main(String[] args) {
        checkFactoryMethod();
        checkDecorator();
        checkChainedDecorators();
        checkIntegrationWithFacade();
        System.out.println("All menu and decorator checks passed.");
    }

    private static void checkFactoryMethod() {
        MenuItemFactory burgerFactory = new BurgerFactory();
        MenuItemFactory teaFactory = new TeaFactory();

        Order.Item burger = burgerFactory.createItem();
        check("Burger".equals(burger.name()), "BurgerFactory default name");
        check(burger.price() == 1500, "BurgerFactory default price");

        Order.Item tea = teaFactory.createItem();
        check("Tea".equals(tea.name()), "TeaFactory default name");
        check(tea.price() == 500, "TeaFactory default price");

        MenuItemFactory customBurger = new BurgerFactory("Double Burger", 2200);
        Order.Item custom = customBurger.createItem();
        check("Double Burger".equals(custom.name()), "Custom burger name");
        check(custom.price() == 2200, "Custom burger price");

        expectThrows(IllegalArgumentException.class, () -> new BurgerFactory("Burger", -100));
        expectThrows(NullPointerException.class, () -> new BurgerFactory(null, 1000));
    }

    private static void checkDecorator() {
        Order.Item base = new BurgerFactory().createItem();
        Order.Item cheeseBurger = new CheeseExtra(base);

        check("Burger + Cheese".equals(cheeseBurger.name()), "CheeseExtra name modification");
        check(cheeseBurger.price() == 1700, "CheeseExtra price addition (1500 + 200)");

        // Custom extra price
        Order.Item premiumCheese = new CheeseExtra(base, 350);
        check(premiumCheese.price() == 1850, "CheeseExtra custom price");

        expectThrows(NullPointerException.class, () -> new CheeseExtra(null));
        expectThrows(IllegalArgumentException.class, () -> new CheeseExtra(base, -50));
    }

    private static void checkChainedDecorators() {
        Order.Item base = new BurgerFactory().createItem();
        Order.Item cheeseBurger = new CheeseExtra(base);
        Order.Item deluxeBurger = new BaconExtra(cheeseBurger);

        check("Burger + Cheese + Bacon".equals(deluxeBurger.name()), "Chained decorator names");
        check(deluxeBurger.price() == 2000, "Chained decorator price sum (1500 + 200 + 300)");

        // Wrapping tea with decorators also adheres to Order.Item
        Order.Item tea = new TeaFactory().createItem();
        Order.Item cheeseTea = new CheeseExtra(tea, 150);
        check("Tea + Cheese".equals(cheeseTea.name()), "CheeseExtra on tea");
        check(cheeseTea.price() == 650, "CheeseExtra on tea price");
    }

    private static void checkIntegrationWithFacade() {
        MenuItemFactory burgerFactory = new BurgerFactory();
        MenuItemFactory teaFactory = new TeaFactory();

        Order.Item deluxeBurger = new BaconExtra(new CheeseExtra(burgerFactory.createItem()));
        Order.Item tea = teaFactory.createItem();

        CafeFacade cafe = new CafeFacade(
            new RegularPricing(),
            (order, total) -> {
                check(order.items().size() == 2, "Event received both items");
                check(total == 2500, "Event received total price (2000 + 500)");
            }
        );

        CafeFacade.Receipt receipt = cafe.placeOrder("Zhasulan", List.of(deluxeBurger, tea));
        check(receipt.total() == 2500, "Facade calculated correct decorated total");
        check(receipt.order().items().get(0).name().equals("Burger + Cheese + Bacon"), "Receipt contains decorated name");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError("Check failed: " + message);
        }
    }

    private static void expectThrows(Class<? extends Throwable> expected, Runnable action) {
        try {
            action.run();
        } catch (Throwable actual) {
            if (expected.isInstance(actual)) {
                return;
            }
            throw new AssertionError("Expected " + expected.getName() + " but caught " + actual.getClass().getName(), actual);
        }
        throw new AssertionError("Expected " + expected.getName() + " was not thrown.");
    }
}
