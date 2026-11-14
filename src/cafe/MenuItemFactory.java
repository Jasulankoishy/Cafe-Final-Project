package cafe;

/**
 * Factory Method pattern: Creator defining the contract for item instantiation.
 */
public abstract class MenuItemFactory {
    public abstract Order.Item createItem();
}
