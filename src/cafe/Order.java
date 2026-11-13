package cafe;

import java.util.ArrayList;
import java.util.List;

public record Order(String customer, List<Item> items) {
    public Order {
        items = List.copyOf(items);
    }

    public interface Item {
        String name();
        int price();
    }

    public record MenuItem(String name, int price) implements Item {}

    public static class Builder {
        private final String customer;
        private final List<Item> items = new ArrayList<>();

        public Builder(String customer) {
            if (customer == null || customer.isBlank()) {
                throw new IllegalArgumentException("Customer name is required.");
            }
            this.customer = customer;
        }

        public Builder add(Item item) {
            items.add(java.util.Objects.requireNonNull(item));
            return this;
        }

        public Order build() {
            if (items.isEmpty()) {
                throw new IllegalStateException("Add at least one item.");
            }
            return new Order(customer, items);
        }
    }
}
