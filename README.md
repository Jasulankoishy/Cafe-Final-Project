# Cafe Ordering System

Team project starter. Builder and Facade are implemented. The demo uses regular prices and prints a single order confirmation. The other four patterns are team tasks.

Run from this directory with Java 17:

```sh
javac --release 17 -d out src/cafe/*.java
java -cp out cafe.Main
```

- Builder: Order.Builder collects items and creates an order with an immutable item list.
- Facade: CafeFacade.placeOrder builds the order, calculates the price, creates a receipt, and announces that the order was placed.

Keep all classes in package cafe. Use separate branches for team tasks. During integration, Main wires the menu factory, toppings, price strategy and event publisher into one order demo.
