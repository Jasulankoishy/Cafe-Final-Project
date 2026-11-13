package cafe;

import java.util.stream.Collectors;

public final class KitchenListener implements CafeFacade.OrderEvents {
    @Override
    public void placed(Order order, int total) {
        String items = order.items().stream()
            .map(Order.Item::name)
            .collect(Collectors.joining(", "));
        System.out.println("Kitchen: prepare order for " + order.customer() + ": " + items);
    }
}
