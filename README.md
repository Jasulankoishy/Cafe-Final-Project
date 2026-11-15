# Cafe Ordering System (Design Patterns Final Project)

A cohesive Java 17 restaurant ordering system demonstrating **6 software design patterns** implemented collaboratively across team branches.

## Implemented Design Patterns (6 Total)

| Pattern | Category | Responsible | Key Classes | Role & Responsibility |
| :--- | :--- | :--- | :--- | :--- |
| **Builder** | Creational | Jasulan Koishybay | `Order.Builder` | Validates customer and items, constructs immutable `Order` instances step-by-step. |
| **Facade** | Structural | Jasulan Koishybay | `CafeFacade` | Simplifies the subsystem API: coordinates building orders, pricing, receipts, and notifying event subscribers. |
| **Factory Method** | Creational | Adilet Ainadinov | `MenuItemFactory`, `BurgerFactory`, `TeaFactory` | Encapsulates item creation, decoupling client code from concrete product classes. |
| **Decorator** | Structural | Adilet Ainadinov | `ItemDecorator`, `CheeseExtra`, `BaconExtra` | Dynamically wraps `Order.Item` instances to append toppings and adjust prices at runtime. |
| **Strategy** | Behavioral | Dias (HumbleCherry10) | `CafeFacade.Pricing`, `RegularPricing`, `DiscountPricing` | Encapsulates interchangeable pricing and discount calculation algorithms. |
| **Observer** | Behavioral | Dias (HumbleCherry10) | `CafeFacade.OrderEvents`, `OrderEventPublisher`, `KitchenListener`, `CashierListener` | Dispatches order placement notifications to subscribed departments (Kitchen, Cashier). |

---

## Architecture Flow

```text
1. Factory Method : MenuItemFactory -> creates base Burger / Tea
2. Decorator      : CheeseExtra / BaconExtra -> wraps Item, adds toppings & price
3. Builder        : Order.Builder -> compiles items and customer into immutable Order
4. Facade         : CafeFacade -> orchestrates pricing calculation and receipt creation
5. Strategy       : RegularPricing / DiscountPricing -> calculates final price
6. Observer       : OrderEventPublisher -> notifies KitchenListener and CashierListener
```

---

## Team Branches & Contributions

- `main`: Fully integrated project combining all 6 patterns in `Main.java` and `IntegrationChecks.java`.
- `menu-patterns` (Adilet Ainadinov): Factory Method (`MenuItemFactory`, `BurgerFactory`, `TeaFactory`) and Decorator (`ItemDecorator`, `CheeseExtra`, `BaconExtra`), with standalone demo (`MenuPatternsDemo.java`) and checks (`MenuPatternsChecks.java`).
- `pricing-events` (Dias): Strategy (`RegularPricing`, `DiscountPricing`) and Observer (`OrderEventPublisher`, `KitchenListener`, `CashierListener`), with standalone demo (`PricingEventsDemo.java`) and checks (`PricingEventsChecks.java`).

---

## How to Build and Run (Java 17+)

### 1. Compile all classes
```sh
javac --release 17 -d out -sourcepath src src/cafe/*.java
```

### 2. Run the Integrated Demo
```sh
java -cp out cafe.Main
```

### 3. Run Standalone Demos
```sh
# Adilet Standalone Demo (Factory Method & Decorator)
java -cp out cafe.MenuPatternsDemo

# Dias Standalone Demo (Strategy & Observer)
java -cp out cafe.PricingEventsDemo
```

### 4. Run Behavioral Checks (Automated Tests)
```sh
# All 6 patterns integrated check
java -cp out cafe.IntegrationChecks

# Adilet Checks
java -cp out cafe.MenuPatternsChecks

# Dias Checks
java -cp out cafe.PricingEventsChecks
```

## Pattern packages

- `src/cafe/builder/`: Order
- `src/cafe/facade/`: CafeFacade
- `src/cafe/factory/`: MenuItemFactory, BurgerFactory, TeaFactory
- `src/cafe/decorator/`: ItemDecorator, CheeseExtra, BaconExtra
- `src/cafe/strategy/`: RegularPricing, DiscountPricing
- `src/cafe/observer/`: OrderEventPublisher, KitchenListener, CashierListener
