# Member 1: Factory Method and Decorator

Branch: `menu-patterns`. All added Java classes are in `src/cafe`, package `cafe`.
The original `CafeFacade`, `Order`, and `Main` are unchanged.

## Factory Method

`MenuItemFactory` defines an abstract creator with the method `createItem()`, which returns an `Order.Item`.
Concrete creators:
- `BurgerFactory`: creates an `Order.MenuItem("Burger", 1500)` by default, with an optional constructor for custom burger varieties.
- `TeaFactory`: creates an `Order.MenuItem("Tea", 500)` by default, with an optional constructor for custom tea varieties.

Benefits:
- Decouples client code from concrete product classes.
- Follows the Open/Closed Principle (OCP): new menu items and factories can be added without modifying existing order logic.

## Decorator

`ItemDecorator` implements `Order.Item` and wraps another `Order.Item`.
Concrete decorators:
- `CheeseExtra`: appends `" + Cheese"` to the item name and adds 200 KZT (or a configured amount) to the price.
- `BaconExtra`: appends `" + Bacon"` to the item name and adds 300 KZT (or a configured amount) to the price.

Decorators can be stacked dynamically at runtime:
```java
Order.Item customBurger = new BaconExtra(new CheeseExtra(burgerFactory.createItem()));
// Result name: "Burger + Cheese + Bacon"
// Result price: 1500 + 200 + 300 = 2000 KZT
```

Because `ItemDecorator` satisfies `Order.Item`, the resulting object can be passed directly into `Order.Builder` and `CafeFacade.placeOrder(...)` without any modifications to core classes.

## Integration Example

```java
MenuItemFactory burgerFactory = new BurgerFactory();
MenuItemFactory teaFactory = new TeaFactory();

Order.Item burger = new CheeseExtra(burgerFactory.createItem());
Order.Item tea = teaFactory.createItem();

CafeFacade cafe = new CafeFacade(new RegularPricing(), (order, total) -> {});
CafeFacade.Receipt receipt = cafe.placeOrder("Zhasulan", List.of(burger, tea));
```

## Running with Java 17

```sh
javac --release 17 -d out -sourcepath src src/cafe/*.java
java -cp out cafe.MenuPatternsDemo
java -cp out cafe.MenuPatternsChecks
```
