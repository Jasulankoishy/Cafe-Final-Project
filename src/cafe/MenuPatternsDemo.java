package cafe;

import java.util.List;

/** Member 1's standalone demo: Factory Method and Decorator. */
public final class MenuPatternsDemo {
    public static void main(String[] args) {
        MenuItemFactory burgerFactory = new BurgerFactory();
        MenuItemFactory teaFactory = new TeaFactory();

        // 1. Factory Method: create base menu items
        Order.Item burger = burgerFactory.createItem();
        Order.Item tea = teaFactory.createItem();

        System.out.println("Factory Method created:");
        System.out.println(" - " + burger.name() + " (" + burger.price() + " KZT)");
        System.out.println(" - " + tea.name() + " (" + tea.price() + " KZT)");

        // 2. Decorator: wrap burger with CheeseExtra
        Order.Item cheeseBurger = new CheeseExtra(burger);
        System.out.println("\nDecorated with Cheese:");
        System.out.println(" - " + cheeseBurger.name() + " (" + cheeseBurger.price() + " KZT)");

        // 3. Stacking another Decorator: Cheese + Bacon
        Order.Item deluxeBurger = new BaconExtra(cheeseBurger);
        System.out.println("\nDecorated with Cheese and Bacon:");
        System.out.println(" - " + deluxeBurger.name() + " (" + deluxeBurger.price() + " KZT)");

        // 4. Pass items into CafeFacade
        CafeFacade cafe = new CafeFacade(
            new RegularPricing(),
            (order, total) -> System.out.println("Event: Order placed for " + order.customer())
        );

        System.out.println("\nPlacing order through CafeFacade:");
        CafeFacade.Receipt receipt = cafe.placeOrder("Zhasulan", List.of(deluxeBurger, tea));

        for (Order.Item item : receipt.order().items()) {
            System.out.println(" * " + item.name() + ": " + item.price() + " KZT");
        }
        System.out.println("Total receipt: " + receipt.total() + " KZT");
    }
}
