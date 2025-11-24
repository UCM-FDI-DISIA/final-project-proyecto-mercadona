import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class ShoppingApp extends JFrame {
    private ShoppingCart cart;
    private Product[] products;
    private JLabel cartCountLabel;
    
    public ShoppingApp() {
        cart = new ShoppingCart();
        initializeProducts();
        setupUI();
    }
    
    private void initializeProducts() {
        products = new Product[] {
            new Product("Laptop", 999.99, "https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=400"),
            new Product("Smartphone", 699.99, "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=400"),
            new Product("Headphones", 149.99, "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=400"),
            new Product("Smartwatch", 299.99, "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=400"),
            new Product("Tablet", 499.99, "https://images.unsplash.com/photo-1561154464-82e9adf32764?w=400"),
            new Product("Camera", 799.99, "https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?w=400")
        };
    }
    
    private void setupUI() {
        setTitle("Shopping App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(51, 51, 51));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("Shopping Store");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        cartCountLabel = new JLabel("Cart: 0 items");
        cartCountLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        cartCountLabel.setForeground(Color.WHITE);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(cartCountLabel, BorderLayout.EAST);
        
        // Products panel
        JPanel productsPanel = new JPanel(new GridLayout(2, 3, 15, 15));
        productsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        productsPanel.setBackground(Color.WHITE);
        
        for (Product product : products) {
            productsPanel.add(createProductPanel(product));
        }
        
        // Bottom panel with buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        bottomPanel.setBackground(Color.WHITE);
        
        JButton viewCartButton = new JButton("View Shopping Cart");
        viewCartButton.setFont(new Font("Arial", Font.BOLD, 16));
        viewCartButton.setPreferredSize(new Dimension(200, 45));
        viewCartButton.setBackground(new Color(70, 130, 180));
        viewCartButton.setForeground(Color.WHITE);
        viewCartButton.setFocusPainted(false);
        viewCartButton.addActionListener(e -> viewCart());
        
        JButton checkoutButton = new JButton("Checkout & Pay");
        checkoutButton.setFont(new Font("Arial", Font.BOLD, 16));
        checkoutButton.setPreferredSize(new Dimension(200, 45));
        checkoutButton.setBackground(new Color(34, 139, 34));
        checkoutButton.setForeground(Color.WHITE);
        checkoutButton.setFocusPainted(false);
        checkoutButton.addActionListener(e -> checkout());
        
        bottomPanel.add(viewCartButton);
        bottomPanel.add(checkoutButton);
        
        // Add all panels to frame
        add(headerPanel, BorderLayout.NORTH);
        add(productsPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        
        setSize(900, 700);
        setLocationRelativeTo(null);
    }
    
    private JPanel createProductPanel(Product product) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
        panel.setBackground(Color.WHITE);
        
        // Image panel with actual image loading
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setPreferredSize(new Dimension(200, 150));
        imagePanel.setBackground(Color.WHITE);
        
        try {
            URL url = new URL(product.getImageUrl());
            BufferedImage originalImage = ImageIO.read(url);
            Image scaledImage = originalImage.getScaledInstance(200, 150, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
            imagePanel.add(imageLabel, BorderLayout.CENTER);
        } catch (Exception e) {
            // Fallback to colored panel if image fails to load
            imagePanel.setBackground(new Color(200, 200, 200));
            JLabel errorLabel = new JLabel("Image not available", SwingConstants.CENTER);
            errorLabel.setForeground(Color.GRAY);
            imagePanel.add(errorLabel, BorderLayout.CENTER);
        }
        
        // Product info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        infoPanel.setBackground(Color.WHITE);
        
        JLabel nameLabel = new JLabel(product.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel priceLabel = new JLabel(String.format("$%.2f", product.getPrice()));
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        priceLabel.setForeground(new Color(0, 128, 0));
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton addButton = new JButton("Add to Cart");
        addButton.setFont(new Font("Arial", Font.BOLD, 12));
        addButton.setBackground(new Color(255, 140, 0));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addButton.addActionListener(e -> addToCart(product));
        
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(priceLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(addButton);
        
        panel.add(imagePanel, BorderLayout.CENTER);
        panel.add(infoPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void addToCart(Product product) {
        cart.addItem(product);
        updateCartCount();
        JOptionPane.showMessageDialog(this, 
            product.getName() + " added to cart!", 
            "Added", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void updateCartCount() {
        int totalItems = 0;
        for (int quantity : cart.getItems().values()) {
            totalItems += quantity;
        }
        cartCountLabel.setText("Cart: " + totalItems + " items");
    }
    
    private void viewCart() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Your cart is empty!", 
                "Shopping Cart", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        StringBuilder cartContents = new StringBuilder();
        cartContents.append("Shopping Cart Contents:\n\n");
        
        for (Map.Entry<Product, Integer> entry : cart.getItems().entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            double subtotal = product.getPrice() * quantity;
            cartContents.append(String.format("%s x%d - $%.2f\n", 
                product.getName(), quantity, subtotal));
        }
        
        cartContents.append(String.format("\nTotal: $%.2f", cart.getTotal()));
        
        JOptionPane.showMessageDialog(this, 
            cartContents.toString(), 
            "Shopping Cart", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void checkout() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Your cart is empty! Add items before checkout.", 
                "Checkout", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            String.format("Total amount: $%.2f\nProceed with payment?", cart.getTotal()), 
            "Checkout", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, 
                "Payment successful! Thank you for your purchase.", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            cart.clear();
            updateCartCount();
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ShoppingApp app = new ShoppingApp();
            app.setVisible(true);
        });
    }
}