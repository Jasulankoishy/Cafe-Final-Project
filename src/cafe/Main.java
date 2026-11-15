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
import cafe.observer.KitchenListener;
import cafe.observer.CashierListener;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 1. Консольдік демо
        MenuItemFactory burgerFactory = new BurgerFactory();
        MenuItemFactory teaFactory = new TeaFactory();

        Order.Item baseBurger = burgerFactory.createItem();
        Order.Item baseTea = teaFactory.createItem();
        Order.Item customBurger = new BaconExtra(new CheeseExtra(baseBurger));

        OrderEventPublisher publisher = new OrderEventPublisher();
        publisher.subscribe(new KitchenListener());
        publisher.subscribe(new CashierListener());

        CafeFacade cafe = new CafeFacade(new DiscountPricing(10), publisher);
        CafeFacade.Receipt receipt = cafe.placeOrder("Zhasulan", List.of(customBurger, baseTea));

        System.out.println("Receipt total: " + receipt.total() + " KZT");

        // 2. Графикалық интерфейсті (Swing UI) іске қосу
        CafeSwingUI.main(args);
    }
}
