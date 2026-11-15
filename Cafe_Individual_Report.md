# My part of the Cafe Ordering System

My part of the project was Builder and Facade.

I used Builder to create an order step by step. In `Order.Builder`, I add each item with `add()` and call `build()` to create the order. The builder rejects an empty order.

I used Facade to make ordering easier. The main program calls `placeOrder()` once. This method uses Builder, asks the pricing strategy for the total, creates a receipt, and calls the notification handler.

In our demo, the burger with extras and tea cost 2500 KZT before the discount. After a 10% discount, the final total is 2250 KZT.
