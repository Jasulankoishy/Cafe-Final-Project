package cafe;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        CafeFacade cafe = new CafeFacade(
            order -> order.items().stream().mapToInt(Order.Item::price).sum(),
            (order, total) -> System.out.println("Order placed for " + order.customer())
        );

        CafeFacade.Receipt receipt = cafe.placeOrder("Zhasulan", List.of(
            new Order.MenuItem("Burger", 1500),
            new Order.MenuItem("Tea", 500)
        ));

        for (Order.Item item : receipt.order().items()) {
            System.out.println(item.name() + ": " + item.price() + " KZT");
        }
        System.out.println("Total: " + receipt.total() + " KZT");
    }
}
