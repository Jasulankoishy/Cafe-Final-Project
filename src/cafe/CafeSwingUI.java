package cafe;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CafeSwingUI extends JFrame {
    private static final Color BG_CREAM = new Color(250, 246, 240);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color COFFEE_DARK = new Color(78, 52, 46);
    private static final Color COFFEE_ACCENT = new Color(109, 76, 65);
    private static final Color BORDER_LIGHT = new Color(224, 214, 204);
    private static final Color GREEN_TOTAL = new Color(46, 125, 50);

    private final MenuItemFactory burgerFactory = new BurgerFactory();
    private final MenuItemFactory teaFactory = new TeaFactory();
    private final List<Order.Item> cart = new ArrayList<>();

    private final DefaultListModel<String> cartListModel = new DefaultListModel<>();
    private final JTextField customerField = new JTextField("Zhasulan");
    private final JCheckBox cheeseBox = new JCheckBox("Extra Cheese (+200 KZT)");
    private final JCheckBox baconBox = new JCheckBox("Extra Bacon (+300 KZT)");
    private final JComboBox<String> discountBox = new JComboBox<>(new String[]{
            "Regular Pricing (0%)",
            "Student Discount (10%)",
            "Happy Hour Discount (20%)"
    });
    private final JLabel subtotalLabel = new JLabel("Subtotal: 0 KZT");
    private final JLabel totalLabel = new JLabel("Total: 0 KZT");
    private final JTextArea liveLogArea = new JTextArea(6, 30);

    public CafeSwingUI() {
        setTitle("Cafe Ordering System — SE-2537");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1080, 680);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_CREAM);
        setLayout(new BorderLayout(16, 16));

        add(createHeader(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);
        add(createBottomLogPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_CREAM);
        header.setBorder(new EmptyBorder(18, 24, 4, 24));

        JLabel title = new JLabel("AITU CAFE ORDERING SYSTEM", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 26));
        title.setForeground(COFFEE_DARK);

        JLabel subtitle = new JLabel("6 GoF Design Patterns • Factory Method • Decorator • Builder • Facade • Strategy • Observer", SwingConstants.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitle.setForeground(new Color(120, 100, 90));

        header.add(title, BorderLayout.CENTER);
        header.add(subtitle, BorderLayout.SOUTH);
        return header;
    }

    private JPanel createMainContent() {
        JPanel main = new JPanel(new GridLayout(1, 2, 20, 0));
        main.setBackground(BG_CREAM);
        main.setBorder(new EmptyBorder(8, 24, 8, 24));

        main.add(createMenuPanel());
        main.add(createOrderPanel());
        return main;
    }

    private JPanel createMenuPanel() {
        JPanel wrapper = new JPanel(new BorderLayout(0, 12));
        wrapper.setBackground(BG_CREAM);

        JLabel sectionTitle = new JLabel("OUR MENU");
        sectionTitle.setFont(new Font("Serif", Font.BOLD, 18));
        sectionTitle.setForeground(COFFEE_DARK);
        wrapper.add(sectionTitle, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 2, 14, 14));
        grid.setBackground(BG_CREAM);

        grid.add(createMenuCard("Classic Burger", "1500 KZT", "Juicy beef patty, fresh bun & signature sauce", () -> addItemToCart(burgerFactory.createItem())));
        grid.add(createMenuCard("Hot Tea", "500 KZT", "Freshly brewed black tea in a warm cup", () -> addItemToCart(teaFactory.createItem())));
        grid.add(createMenuCard("Double Burger", "2200 KZT", "Custom BurgerFactory(\"Double Burger\", 2200)", () -> addItemToCart(new BurgerFactory("Double Burger", 2200).createItem())));
        grid.add(createMenuCard("Green Tea", "600 KZT", "Custom TeaFactory(\"Green Tea\", 600)", () -> addItemToCart(new TeaFactory("Green Tea", 600).createItem())));

        wrapper.add(grid, BorderLayout.CENTER);

        JPanel extrasPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 10));
        extrasPanel.setBackground(CARD_BG);
        extrasPanel.setBorder(BorderFactory.createCompoundBorder(new LineBorder(BORDER_LIGHT, 1), new EmptyBorder(4, 10, 4, 10)));

        JLabel extrasTitle = new JLabel("Toppings (Decorator):");
        extrasTitle.setFont(new Font("SansSerif", Font.BOLD, 12));
        extrasTitle.setForeground(COFFEE_DARK);

        cheeseBox.setBackground(CARD_BG);
        baconBox.setBackground(CARD_BG);
        extrasPanel.add(extrasTitle);
        extrasPanel.add(cheeseBox);
        extrasPanel.add(baconBox);

        wrapper.add(extrasPanel, BorderLayout.SOUTH);
        return wrapper;
    }

    private JPanel createMenuCard(String name, String price, String desc, Runnable onAdd) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(new LineBorder(BORDER_LIGHT, 1), new EmptyBorder(14, 14, 14, 14)));

        JPanel topInfo = new JPanel(new GridLayout(2, 1, 0, 6));
        topInfo.setBackground(CARD_BG);

        JLabel nameLbl = new JLabel(name, SwingConstants.CENTER);
        nameLbl.setFont(new Font("Serif", Font.BOLD, 17));
        nameLbl.setForeground(COFFEE_DARK);

        JLabel priceLbl = new JLabel(price, SwingConstants.CENTER);
        priceLbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        priceLbl.setForeground(COFFEE_ACCENT);

        JLabel descLbl = new JLabel("<html><center>" + desc + "</center></html>", SwingConstants.CENTER);
        descLbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
        descLbl.setForeground(Color.GRAY);

        topInfo.add(nameLbl);
        topInfo.add(priceLbl);

        JButton addBtn = createStyledButton("ADD TO CART", COFFEE_DARK);
        addBtn.addActionListener(e -> onAdd.run());

        card.add(topInfo, BorderLayout.CENTER);
        card.add(addBtn, BorderLayout.SOUTH);
        return card;
    }

    private JPanel createOrderPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(CARD_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(new LineBorder(BORDER_LIGHT, 1), new EmptyBorder(16, 18, 16, 18)));

        JLabel title = new JLabel("YOUR ORDER");
        title.setFont(new Font("Serif", Font.BOLD, 18));
        title.setForeground(COFFEE_DARK);
        panel.add(title, BorderLayout.NORTH);

        JList<String> cartList = new JList<>(cartListModel);
        cartList.setFont(new Font("Monospaced", Font.PLAIN, 12));
        cartList.setBackground(new Color(252, 250, 247));
        JScrollPane scrollPane = new JScrollPane(cartList);
        scrollPane.setBorder(new LineBorder(BORDER_LIGHT, 1));
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel controls = new JPanel();
        controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
        controls.setBackground(CARD_BG);

        JLabel custLbl = new JLabel("CUSTOMER NAME");
        custLbl.setFont(new Font("SansSerif", Font.BOLD, 11));
        custLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        customerField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        customerField.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel discLbl = new JLabel("PRICING STRATEGY");
        discLbl.setFont(new Font("SansSerif", Font.BOLD, 11));
        discLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        discountBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        discountBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        discountBox.addActionListener(e -> refreshTotals());

        subtotalLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtotalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        totalLabel.setForeground(GREEN_TOTAL);
        totalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel btnRow = new JPanel(new GridLayout(1, 2, 10, 0));
        btnRow.setBackground(CARD_BG);
        btnRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btnRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton clearBtn = createStyledButton("CLEAR CART", new Color(141, 110, 99));
        clearBtn.addActionListener(e -> clearCart());

        JButton placeBtn = createStyledButton("PLACE ORDER", COFFEE_DARK);
        placeBtn.addActionListener(e -> submitOrder());

        btnRow.add(clearBtn);
        btnRow.add(placeBtn);

        controls.add(Box.createVerticalStrut(8));
        controls.add(custLbl);
        controls.add(Box.createVerticalStrut(4));
        controls.add(customerField);
        controls.add(Box.createVerticalStrut(8));
        controls.add(discLbl);
        controls.add(Box.createVerticalStrut(4));
        controls.add(discountBox);
        controls.add(Box.createVerticalStrut(10));
        controls.add(subtotalLabel);
        controls.add(totalLabel);
        controls.add(Box.createVerticalStrut(10));
        controls.add(btnRow);

        panel.add(controls, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createBottomLogPanel() {
        JPanel logPanel = new JPanel(new BorderLayout(0, 4));
        logPanel.setBackground(BG_CREAM);
        logPanel.setBorder(new EmptyBorder(0, 24, 16, 24));

        JLabel logTitle = new JLabel("OBSERVER NOTIFICATIONS (KITCHEN & CASHIER LIVE FEED)");
        logTitle.setFont(new Font("SansSerif", Font.BOLD, 11));
        logTitle.setForeground(COFFEE_DARK);

        liveLogArea.setEditable(false);
        liveLogArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        liveLogArea.setBackground(new Color(30, 41, 59));
        liveLogArea.setForeground(new Color(248, 250, 252));
        liveLogArea.setBorder(new EmptyBorder(8, 10, 8, 10));
        liveLogArea.setText("Ready. Select items from the menu and click PLACE ORDER...");

        logPanel.add(logTitle, BorderLayout.NORTH);
        logPanel.add(new JScrollPane(liveLogArea), BorderLayout.CENTER);
        return logPanel;
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setBorder(new EmptyBorder(8, 12, 8, 12));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void addItemToCart(Order.Item baseItem) {
        Order.Item item = baseItem;
        if (cheeseBox.isSelected()) item = new CheeseExtra(item);
        if (baconBox.isSelected()) item = new BaconExtra(item);
        cart.add(item);
        cartListModel.addElement(String.format("%-32s %6d KZT", item.name(), item.price()));
        refreshTotals();
    }

    private CafeFacade.Pricing getSelectedStrategy() {
        return switch (discountBox.getSelectedIndex()) {
            case 1 -> new DiscountPricing(10);
            case 2 -> new DiscountPricing(20);
            default -> new RegularPricing();
        };
    }

    private void refreshTotals() {
        if (cart.isEmpty()) {
            subtotalLabel.setText("Subtotal: 0 KZT");
            totalLabel.setText("Total: 0 KZT");
            return;
        }
        int subtotal = cart.stream().mapToInt(Order.Item::price).sum();
        int finalTotal = getSelectedStrategy().total(new Order("Preview", cart));
        subtotalLabel.setText("Subtotal: " + subtotal + " KZT");
        totalLabel.setText("Total: " + finalTotal + " KZT");
    }

    private void clearCart() {
        cart.clear();
        cartListModel.clear();
        refreshTotals();
    }

    private void submitOrder() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please add at least one item to the cart.", "Empty Cart", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String customer = customerField.getText().trim();
        if (customer.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a customer name.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        OrderEventPublisher publisher = new OrderEventPublisher();
        publisher.subscribe(new KitchenListener());
        publisher.subscribe(new CashierListener());
        publisher.subscribe((order, total) -> {
            String itemsStr = String.join(", ", order.items().stream().map(Order.Item::name).toList());
            liveLogArea.append("\n[KitchenListener] Prepare order for " + order.customer() + ": " + itemsStr);
        });
        publisher.subscribe((order, total) ->
            liveLogArea.append("\n[CashierListener] Invoice recorded for " + order.customer() + " | Total: " + total + " KZT")
        );

        CafeFacade cafe = new CafeFacade(getSelectedStrategy(), publisher);
        liveLogArea.setText("=== NEW ORDER DISPATCHED VIA CAFE FACADE ===");
        CafeFacade.Receipt receipt = cafe.placeOrder(customer, cart);

        StringBuilder receiptText = new StringBuilder("Customer: ").append(receipt.order().customer()).append("\n\n");
        for (Order.Item item : receipt.order().items()) {
            receiptText.append("• ").append(item.name()).append(" — ").append(item.price()).append(" KZT\n");
        }
        receiptText.append("\nFinal Paid Total: ").append(receipt.total()).append(" KZT");

        JOptionPane.showMessageDialog(this, receiptText.toString(), "Order Receipt", JOptionPane.INFORMATION_MESSAGE);
        clearCart();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new CafeSwingUI().setVisible(true));
    }
}
