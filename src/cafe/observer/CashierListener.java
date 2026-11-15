package cafe.observer;

import cafe.builder.Order;
import cafe.facade.CafeFacade;

public final class CashierListener implements CafeFacade.OrderEvents {
    @Override
    public void placed(Order order, int total) {
        System.out.println("Cashier: order for " + order.customer() + ", total: " + total + " KZT");
    }
}
