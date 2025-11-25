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
    private Products products = new Products(this, this.searchField);
    private List<Product> allProducts = new ArrayList<>();
    private ProfileSystem profileSystem;
    
    private JTextField searchField;
    private JButton searchButton, cartButton, homeButton;
    private JLabel cartCountLabel = new JLabel("(0)");
    private ShoppingCart shoppingCart = new ShoppingCart(this, cartCountLabel, mainPanel, contentPanel);
    private Map<Product, Integer> cart = new HashMap<>();

    
    
    private JComboBox<String> supermarketFilter, brandFilter, categoryFilter;
    
    public void productsNotFound(String query) {
    	JOptionPane.showMessageDialog(this, 
                "No se encontraron productos que coincidan con: " + query, 
                "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void noMoreStock() {
    	JOptionPane.showMessageDialog(this, "No hay más stock disponible", "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public void addedToCart(ShoppingCart cart, Product p) {
    	JOptionPane.showMessageDialog(this, p.name + " añadido al carrito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public SupermarketShoppingApp() {
        setTitle("Supermarket Shopping App");
        setSize(900, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        products.initializeProducts(allProducts);
        
        mainPanel = new JPanel(new BorderLayout());
        shoppingCart.setMainPanel(mainPanel);
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel);
        profileSystem = new ProfileSystem(this);
        
        
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
        
     // Panel derecho: Perfil y Carrito
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setBackground(new Color(0, 150, 0));

        // Botón de perfil
        JButton profileButton = new JButton("👤 Perfil");
        profileButton.setBackground(Color.WHITE);
        profileButton.setFocusPainted(false);
        profileButton.addActionListener(e -> {
            if (contentPanel != null) {
                mainPanel.remove(contentPanel);
            }
            contentPanel = new JPanel(new BorderLayout());
            mainPanel.add(contentPanel, BorderLayout.CENTER);
            profileSystem.showProfileScreen(contentPanel, mainPanel);
        });
        rightPanel.add(profileButton);

     // Botón de carrito con contador integrado
        cartCountLabel.setForeground(Color.BLACK);
        cartCountLabel.setFont(new Font("Arial", Font.BOLD, 12));

        JPanel cartInnerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 0));
        cartInnerPanel.setBackground(Color.WHITE);
        cartInnerPanel.add(new JLabel("🛒 Carrito "));
        cartInnerPanel.add(cartCountLabel);

        cartButton = new JButton();
        cartButton.setLayout(new BorderLayout());
        cartButton.add(cartInnerPanel, BorderLayout.CENTER);
        cartButton.setBackground(Color.WHITE);
        cartButton.setFocusPainted(false);
        cartButton.setBorderPainted(true);
        cartButton.addActionListener(e -> shoppingCart.showCart(cart));

        rightPanel.add(cartButton);

        // Actualizar la referencia del ShoppingCart
        shoppingCart.setCartCount(cartCountLabel);

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
        shoppingCart.setContentPanel(contentPanel);
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
    
    public void addToCart(Product p) {
    	shoppingCart.addToCart(p, cart);
    }
    
    public void showCheckout(double total) {
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

			//Ask for additional payment info based on selection
			String extraInfo = "";
			
			if (paymentMethod.equals("Tarjeta")) {
			JTextField cardNum = new JTextField();
			JTextField exp = new JTextField();
			JTextField cvv = new JTextField();
			
			Object[] fields = {
			   "Número de tarjeta:", cardNum,
			   "Fecha de expiración (MM/YY):", exp,
			   "CVV:", cvv
			};
			
			int result = JOptionPane.showConfirmDialog(this, fields,
			       "Datos de Tarjeta", JOptionPane.OK_CANCEL_OPTION);
			
			if (result != JOptionPane.OK_OPTION) return;
			
			extraInfo = "Tarjeta terminada en " +
			           cardNum.getText().substring(cardNum.getText().length() - 4);
			}
			
			else if (paymentMethod.equals("Transferencia")) {
			String iban = JOptionPane.showInputDialog(this,
			       "Introduce tu número de cuenta / IBAN:");
			
			if (iban == null || iban.isEmpty()) return;
			
			extraInfo = "IBAN: " + iban;
			}
			
			else if (paymentMethod.equals("Bizum")) {
			String bizumNum = JOptionPane.showInputDialog(this,
			       "Número de teléfono para Bizum:");
			
			if (bizumNum == null || bizumNum.isEmpty()) return;
			
			extraInfo = "Bizum enviado a: " + bizumNum;
			}
			
			else if (paymentMethod.equals("PayPal")) {
			try {
			   Desktop.getDesktop().browse(new java.net.URI("https://www.paypal.com"));
			} catch (Exception ex) {
			   JOptionPane.showMessageDialog(this, "No se pudo abrir PayPal",
			           "Error", JOptionPane.ERROR_MESSAGE);
			   return;
			}
			extraInfo = "Pago vía PayPal";
			}
            
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
        
        profileSystem.addPurchaseToHistory(cart, total, paymentMethod);
        
        cart.clear();
        shoppingCart.updateCartCount(cart);
        buildHomeContent();
    }
    
    public void addToFavorites(Product p) {
        profileSystem.addToFavorites(p);
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