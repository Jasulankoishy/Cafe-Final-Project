# Member 2: Strategy and Observer

Branch: `pricing-events`. All added Java classes are in `src/cafe`, package `cafe`.
The original `CafeFacade`, `Order`, and `Main` are unchanged.

## Strategy

`RegularPricing` and `DiscountPricing` implement `CafeFacade.Pricing`.
Regular pricing sums `Order.Item.price()`. Discount pricing accepts an integer
percentage from 0 to 100, applies it to the subtotal, and rounds the final total
down to whole KZT. The demo uses 10%: 2000 KZT becomes 1800 KZT.
Rounding happens once for the entire order, not separately for each item.
Arithmetic uses `long` intermediates; a final total outside the existing `int`
price contract raises `ArithmeticException` instead of silently overflowing.
Discount pricing can handle a subtotal above the `int` limit when the discounted
total fits. Multiplication is checked for overflow as well.

Both strategies use the `Order.Item` interface, so menu factory products and
topping decorators can participate without changes to these pricing classes.

## Observer

`OrderEventPublisher` implements `CafeFacade.OrderEvents`. Register listeners
with `subscribe`. `KitchenListener` prints the customer and items;
`CashierListener` prints the customer and the total provided by the facade.
Each registration receives the same order object and total, synchronously in
registration order. No subscribers is valid. Null subscribers are rejected.
Register a listener once to receive one callback per placement. Subscriptions
added during a callback start receiving events on the next placement.
This simple publisher is intended for the single-threaded console project;
listener exceptions propagate to the caller and stop the current notification.

## Integration example for the team

```java
OrderEventPublisher events = new OrderEventPublisher();
events.subscribe(new KitchenListener());
events.subscribe(new CashierListener());

CafeFacade cafe = new CafeFacade(new DiscountPricing(10), events);
// Pass the team's factory/decorator items to cafe.placeOrder(customer, items).
// Use new RegularPricing() in the constructor to select regular prices.
```

The facade's strategy field is final, so the standalone demo creates two
facades with different strategies. No setter or core-class modification is needed.
The team can use the example when it integrates its shared Main later.

## Run with Java 17

```sh
javac --release 17 -d out src/cafe/*.java
java -cp out cafe.Main
java -cp out cafe.PricingEventsDemo
java -cp out cafe.PricingEventsChecks
```

Both `java -version` and `javac -version` must identify Java 17 (or a newer JDK
capable of targeting 17). On the current Windows machine, the default `java`
points to Java 8. Use the installed Java 17 executables explicitly in PowerShell:

```powershell
$jdkBin = 'C:/Program Files/Eclipse Adoptium/jdk-17.0.14.7-hotspot/bin'
& "$jdkBin/javac.exe" --release 17 -d out src/cafe/*.java
& "$jdkBin/java.exe" -cp out cafe.Main
& "$jdkBin/java.exe" -cp out cafe.PricingEventsDemo
& "$jdkBin/java.exe" -cp out cafe.PricingEventsChecks
```

The original demo reports 2000 KZT. The new demo compares 2000 and 1800 KZT
and prints kitchen and cashier notifications for both placements. Successful
checks finish with `All pricing and event checks passed.` No external testing
library or `-ea` flag is required. Generated `out/` files remain ignored.

Checks cover subtotal calculation, custom Item implementations, discount bounds,
rounding, overflow, unchanged item prices, zero subscribers, both subscriber
deliveries, registration order, subscriptions during notification,
receipt/event consistency, and the actual kitchen/cashier output.
