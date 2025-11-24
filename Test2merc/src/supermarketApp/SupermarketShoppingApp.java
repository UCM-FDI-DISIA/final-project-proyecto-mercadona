package supermarketApp;


import javax.swing.*;
import javax.swing.border.EmptyBorder;

import supermarketApp.SupermarketShoppingApp.Product;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;

public class SupermarketShoppingApp extends JFrame {
    private JPanel mainPanel;
    private JPanel contentPanel;
    private JPanel topBar;
    private Products products = new Products(this, this.searchField);;
    private List<Product> allProducts = new ArrayList<>();
    
    private JTextField searchField;
    private JButton searchButton, cartButton, homeButton;
    private JLabel cartCountLabel;

    private Map<Product, Integer> cart = new HashMap<>();
    
    private JComboBox<String> supermarketFilter, brandFilter, categoryFilter;
    
    public SupermarketShoppingApp() {
        setTitle("Supermarket Shopping App");
        setSize(900, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        products.initializeProducts(allProducts);
        
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel);
        
        buildTopBar();
        buildHomeContent();
    }
    
    public void checkComponentPanel(JPanel productsPanel){
    	JScrollPane scrollPane = new JScrollPane(productsPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
    	if (contentPanel.getComponentCount() > 1) {
            contentPanel.remove(1);
        }
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void buildTopBar() {
        topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(0, 150, 0));
        topBar.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Panel izquierdo: Home button
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(new Color(0, 150, 0));
        homeButton = new JButton("🏠 Home");
        homeButton.setBackground(Color.WHITE);
        homeButton.setFocusPainted(false);
        homeButton.addActionListener(e -> buildHomeContent());
        leftPanel.add(homeButton);
        
        // Panel central: Búsqueda
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setBackground(new Color(0, 150, 0));
        searchField = new JTextField(25);
        searchButton = new JButton("🔍 Buscar");
        searchButton.setBackground(Color.WHITE);
        searchButton.setFocusPainted(false);
        searchButton.addActionListener(e -> products.searchProducts(allProducts));
        centerPanel.add(new JLabel("Buscar: "));
        centerPanel.add(searchField);
        centerPanel.add(searchButton);
        
        // Panel derecho: Carrito
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(new Color(0, 150, 0));
        cartButton = new JButton("🛒 Carrito");
        cartButton.setBackground(Color.WHITE);
        cartButton.setFocusPainted(false);
        cartButton.addActionListener(e -> showCart());
        cartCountLabel = new JLabel("(0)");
        cartCountLabel.setForeground(Color.WHITE);
        cartCountLabel.setFont(new Font("Arial", Font.BOLD, 14));
        rightPanel.add(cartButton);
        rightPanel.add(cartCountLabel);
        
        topBar.add(leftPanel, BorderLayout.WEST);
        topBar.add(centerPanel, BorderLayout.CENTER);
        topBar.add(rightPanel, BorderLayout.EAST);
        
        mainPanel.add(topBar, BorderLayout.NORTH);
    }
    
    private void buildHomeContent() {
        if (contentPanel != null) {
            mainPanel.remove(contentPanel);
        }
        
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Filtros
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filterPanel.setBackground(new Color(240, 240, 240));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filtros"));
        
        filterPanel.add(new JLabel("Supermercado:"));
        supermarketFilter = new JComboBox<>(new String[]{"Todos", "Mercadona", "Día", "Lidl"});
        supermarketFilter.addActionListener(e -> applyFilters());
        filterPanel.add(supermarketFilter);
        
        filterPanel.add(new JLabel("Marca:"));
        brandFilter = new JComboBox<>(new String[]{"Todas", "Hacendado", "Día", "Lidl", "Danone", "Cola Cao", "Ariel"});
        brandFilter.addActionListener(e -> applyFilters());
        filterPanel.add(brandFilter);
        
        filterPanel.add(new JLabel("Categoría:"));
        categoryFilter = new JComboBox<>(new String[]{"Todas", "Lácteos", "Panadería", "Dulces", "Limpieza", "Desayuno", "Pasta", "Café", "Aceites"});
        categoryFilter.addActionListener(e -> applyFilters());
        filterPanel.add(categoryFilter);
        
        contentPanel.add(filterPanel, BorderLayout.NORTH);
        
        // Productos
        products.displayProducts(allProducts);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void addToCart(Product p) {
        if (p.stock <= 0) {
            JOptionPane.showMessageDialog(this, "Producto sin stock", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int currentQuantity = cart.getOrDefault(p, 0);
        if (currentQuantity < p.stock) {
            cart.put(p, currentQuantity + 1);
            updateCartCount();
            JOptionPane.showMessageDialog(this, p.name + " añadido al carrito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No hay más stock disponible", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateCartCount() {
        int total = cart.values().stream().mapToInt(Integer::intValue).sum();
        cartCountLabel.setText("(" + total + ")");
    }
    
    private void applyFilters() {
        String supermarket = (String) supermarketFilter.getSelectedItem();
        String brand = (String) brandFilter.getSelectedItem();
        String category = (String) categoryFilter.getSelectedItem();
        
        List<Product> filtered = new ArrayList<>();
        for (Product p : allProducts) {
            boolean matches = true;
            if (!supermarket.equals("Todos") && !p.supermarket.equals(supermarket)) matches = false;
            if (!brand.equals("Todas") && !p.brand.equals(brand)) matches = false;
            if (!category.equals("Todas") && !p.category.equals(category)) matches = false;
            if (matches) filtered.add(p);
        }
        
        products.displayProducts(filtered);
    }
    
    private void showCart() {
        if (contentPanel != null) {
            mainPanel.remove(contentPanel);
        }
        
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel title = new JLabel("🛒 Mi Carrito");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(title, BorderLayout.NORTH);
        
        if (cart.isEmpty()) {
            JLabel empty = new JLabel("El carrito está vacío");
            empty.setFont(new Font("Arial", Font.PLAIN, 16));
            empty.setHorizontalAlignment(SwingConstants.CENTER);
            contentPanel.add(empty, BorderLayout.CENTER);
        } else {
            JPanel cartPanel = new JPanel();
            cartPanel.setLayout(new BoxLayout(cartPanel, BoxLayout.Y_AXIS));
            cartPanel.setBackground(Color.WHITE);
            
            double subtotal = 0;
            for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
                Product p = entry.getKey();
                int quantity = entry.getValue();
                double itemTotal = p.price * quantity;
                subtotal += itemTotal;
                
                JPanel itemPanel = new JPanel(new BorderLayout(10, 5));
                itemPanel.setBackground(new Color(245, 245, 245));
                itemPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
                itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
                
                JPanel infoPanel = new JPanel();
                infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
                infoPanel.setBackground(new Color(245, 245, 245));
                infoPanel.add(new JLabel("<html><b>" + p.name + "</b></html>"));
                infoPanel.add(new JLabel(p.supermarket + " - " + p.brand));
                infoPanel.add(new JLabel(String.format("%.2f€ x %d = %.2f€", p.price, quantity, itemTotal)));
                
                JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                controlPanel.setBackground(new Color(245, 245, 245));
                
                JButton decreaseBtn = new JButton("-");
                decreaseBtn.addActionListener(e -> {
                    if (quantity > 1) {
                        cart.put(p, quantity - 1);
                    } else {
                        cart.remove(p);
                    }
                    updateCartCount();
                    showCart();
                });
                
                JLabel qtyLabel = new JLabel(" " + quantity + " ");
                
                JButton increaseBtn = new JButton("+");
                increaseBtn.addActionListener(e -> {
                    if (quantity < p.stock) {
                        cart.put(p, quantity + 1);
                        updateCartCount();
                        showCart();
                    } else {
                        JOptionPane.showMessageDialog(this, "No hay más stock", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                
                JButton removeBtn = new JButton("🗑");
                removeBtn.addActionListener(e -> {
                    cart.remove(p);
                    updateCartCount();
                    showCart();
                });
                
                controlPanel.add(decreaseBtn);
                controlPanel.add(qtyLabel);
                controlPanel.add(increaseBtn);
                controlPanel.add(removeBtn);
                
                itemPanel.add(infoPanel, BorderLayout.CENTER);
                itemPanel.add(controlPanel, BorderLayout.EAST);
                
                cartPanel.add(itemPanel);
                cartPanel.add(Box.createVerticalStrut(10));
            }
            
            double shipping = subtotal >= 50 ? 0 : 12;
            double total = subtotal + shipping;
            
            JPanel summaryPanel = new JPanel();
            summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
            summaryPanel.setBackground(new Color(230, 255, 230));
            summaryPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 150, 0), 2),
                new EmptyBorder(15, 15, 15, 15)
            ));
            
            summaryPanel.add(new JLabel(String.format("Subtotal: %.2f€", subtotal)));
            summaryPanel.add(new JLabel(String.format("Envío: %.2f€ %s", shipping, subtotal >= 50 ? "(¡Gratis!)" : "")));
            JLabel totalLabel = new JLabel(String.format("TOTAL: %.2f€", total));
            totalLabel.setFont(new Font("Arial", Font.BOLD, 18));
            summaryPanel.add(totalLabel);
            
            cartPanel.add(summaryPanel);
            
            JScrollPane scrollPane = new JScrollPane(cartPanel);
            scrollPane.setBorder(null);
            contentPanel.add(scrollPane, BorderLayout.CENTER);
            
            JButton checkoutBtn = new JButton("Proceder al Pago");
            checkoutBtn.setBackground(new Color(0, 150, 0));
            checkoutBtn.setForeground(Color.WHITE);
            checkoutBtn.setFont(new Font("Arial", Font.BOLD, 16));
            checkoutBtn.setFocusPainted(false);
            checkoutBtn.addActionListener(e -> showCheckout(total));
            
            JPanel bottomPanel = new JPanel();
            bottomPanel.setBackground(Color.WHITE);
            bottomPanel.add(checkoutBtn);
            contentPanel.add(bottomPanel, BorderLayout.SOUTH);
        }
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }
    
    private void showCheckout(double total) {
        if (contentPanel != null) {
            mainPanel.remove(contentPanel);
        }
        
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(20, 50, 20, 50));
        
        JLabel title = new JLabel("Información de Envío y Pago");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(title, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField phoneField = new JTextField();
        
        addFormField(formPanel, "Nombre completo:", nameField);
        addFormField(formPanel, "Email:", emailField);
        addFormField(formPanel, "Dirección de envío:", addressField);
        addFormField(formPanel, "Teléfono:", phoneField);
        
        formPanel.add(Box.createVerticalStrut(20));
        
        JPanel paymentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        paymentPanel.setBackground(Color.WHITE);
        paymentPanel.add(new JLabel("Método de pago: "));
        
        ButtonGroup paymentGroup = new ButtonGroup();
        JRadioButton cardRadio = new JRadioButton("Tarjeta");
        JRadioButton transferRadio = new JRadioButton("Transferencia");
        JRadioButton bizumRadio = new JRadioButton("Bizum");
        JRadioButton paypalRadio = new JRadioButton("PayPal");
        
        cardRadio.setSelected(true);
        paymentGroup.add(cardRadio);
        paymentGroup.add(transferRadio);
        paymentGroup.add(bizumRadio);
        paymentGroup.add(paypalRadio);
        
        paymentPanel.add(cardRadio);
        paymentPanel.add(transferRadio);
        paymentPanel.add(bizumRadio);
        paymentPanel.add(paypalRadio);
        
        formPanel.add(paymentPanel);
        formPanel.add(Box.createVerticalStrut(20));
        
        JLabel totalLabel = new JLabel(String.format("Total a pagar: %.2f€", total));
        totalLabel.setFont(new Font("Arial", Font.BOLD, 18));
        totalLabel.setForeground(new Color(0, 120, 0));
        formPanel.add(totalLabel);
        
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        JButton confirmBtn = new JButton("Confirmar Compra");
        confirmBtn.setBackground(new Color(0, 150, 0));
        confirmBtn.setForeground(Color.WHITE);
        confirmBtn.setFont(new Font("Arial", Font.BOLD, 16));
        confirmBtn.setFocusPainted(false);
        confirmBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String address = addressField.getText().trim();
            String phone = phoneField.getText().trim();
            
            if (name.isEmpty() || email.isEmpty() || address.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor rellena todos los campos obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!email.matches("^[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) {
                JOptionPane.showMessageDialog(this, "Email inválido", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String paymentMethod = cardRadio.isSelected() ? "Tarjeta" :
                                 transferRadio.isSelected() ? "Transferencia" :
                                 bizumRadio.isSelected() ? "Bizum" : "PayPal";
            
            completePurchase(email, paymentMethod, total);
        });
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(confirmBtn);
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }
    
    private void addFormField(JPanel panel, String label, JTextField field) {
        panel.add(Box.createVerticalStrut(10));
        JLabel lbl = new JLabel(label);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lbl);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(field);
    }
    
    private void completePurchase(String email, String paymentMethod, double total) {
        // Actualizar stock
        for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
            entry.getKey().stock -= entry.getValue();
        }
        
        String receipt = "Recibo de compra #" + System.currentTimeMillis() + 
                        "\n\nProductos:\n";
        for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
            Product p = entry.getKey();
            int qty = entry.getValue();
            receipt += String.format("- %s x%d: %.2f€\n", p.name, qty, p.price * qty);
        }
        receipt += String.format("\nTotal: %.2f€", total);
        receipt += "\nMétodo de pago: " + paymentMethod;
        receipt += "\n\nFecha estimada de entrega: 2 días hábiles";
        receipt += "\n\nSe ha enviado un email de confirmación a: " + email;
        
        JOptionPane.showMessageDialog(this, receipt, "¡Compra Completada!", JOptionPane.INFORMATION_MESSAGE);
        
        cart.clear();
        updateCartCount();
        buildHomeContent();
    }
    
    static class Product {
        String name;
        String supermarket;
        String brand;
        String category;
        double price;
        int stock;
        
        Product(String name, String supermarket, String brand, String category, double price, int stock) {
            this.name = name;
            this.supermarket = supermarket;
            this.brand = brand;
            this.category = category;
            this.price = price;
            this.stock = stock;
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SupermarketShoppingApp().setVisible(true));
    }
}