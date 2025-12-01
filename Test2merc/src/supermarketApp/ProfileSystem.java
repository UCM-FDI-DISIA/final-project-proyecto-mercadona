package supermarketApp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ProfileSystem {
    private SupermarketShoppingApp mainApp;
    private User currentUser;
    private Map<String, User> userDatabase;
    private static final String USER_DATA_FILE = "users.dat";
    
    public ProfileSystem(SupermarketShoppingApp app) {
        this.mainApp = app;
        this.userDatabase = new HashMap<>();
        loadUsers();
    }
    
    // ==================== USER CLASS ====================
    public static class User implements Serializable {
        private static final long serialVersionUID = 1L;
        
        String email;
        String password;
        String name;
        String address;
        String phone;
        List<Purchase> purchaseHistory;
        List<SupermarketShoppingApp.Product> favoriteItems;
        
        public User(String email, String password, String name, String address, String phone) {
            this.email = email;
            this.password = password;
            this.name = name;
            this.address = address;
            this.phone = phone;
            this.purchaseHistory = new ArrayList<>();
            this.favoriteItems = new ArrayList<>();
        }
        
        public void addPurchase(Purchase purchase) {
            purchaseHistory.add(0, purchase); // Añadir al principio
            if (purchaseHistory.size() > 3) { // Solo mantener las últimas 3
                purchaseHistory = new ArrayList<>(purchaseHistory.subList(0, 3));
            }
        }
        
        public void addFavorite(SupermarketShoppingApp.Product product) {
            // Evitar duplicados
            for (SupermarketShoppingApp.Product fav : favoriteItems) {
                if (fav.name.equals(product.name) && fav.supermarket.equals(product.supermarket)) {
                    return;
                }
            }
            favoriteItems.add(product);
        }
        
        public void removeFavorite(SupermarketShoppingApp.Product product) {
            favoriteItems.removeIf(p -> p.name.equals(product.name) && 
                                        p.supermarket.equals(product.supermarket));
        }
    }
    
    // ==================== PURCHASE CLASS ====================
    public static class Purchase implements Serializable {
        private static final long serialVersionUID = 1L;
        
        String purchaseId;
        LocalDateTime date;
        Map<SupermarketShoppingApp.Product, Integer> items;
        double total;
        String paymentMethod;
        String status; // "Procesando", "En camino", "Entregado"
        
        public Purchase(Map<SupermarketShoppingApp.Product, Integer> items, double total, String paymentMethod) {
            this.purchaseId = "PED-" + System.currentTimeMillis();
            this.date = LocalDateTime.now();
            this.items = new HashMap<>(items);
            this.total = total;
            this.paymentMethod = paymentMethod;
            this.status = "Procesando";
        }
        
        public String getFormattedDate() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            return date.format(formatter);
        }
    }
    
    // ==================== LOGIN / REGISTER ====================
    public void showLoginScreen(JPanel contentPanel, JPanel mainPanel) {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(50, 100, 50, 100));
        
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 150, 0), 2),
            new EmptyBorder(30, 30, 30, 30)
        ));
        
        JLabel title = new JLabel("Iniciar Sesión");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginPanel.add(title);
        loginPanel.add(Box.createVerticalStrut(30));
        
        JTextField emailField = new JTextField();
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        JPasswordField passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        
        loginPanel.add(new JLabel("Email:"));
        loginPanel.add(Box.createVerticalStrut(5));
        loginPanel.add(emailField);
        loginPanel.add(Box.createVerticalStrut(15));
        
        loginPanel.add(new JLabel("Contraseña:"));
        loginPanel.add(Box.createVerticalStrut(5));
        loginPanel.add(passwordField);
        loginPanel.add(Box.createVerticalStrut(10));
        
        JButton forgotPassword = new JButton("¿Olvidaste tu contraseña?");
        forgotPassword.setForeground(new Color(0, 100, 200));
        forgotPassword.setBorderPainted(false);
        forgotPassword.setContentAreaFilled(false);
        forgotPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
        forgotPassword.addActionListener(e -> handleForgotPassword(emailField.getText()));
        loginPanel.add(forgotPassword);
        loginPanel.add(Box.createVerticalStrut(20));
        
        JButton loginBtn = new JButton("Iniciar Sesión");
        loginBtn.setBackground(new Color(0, 150, 0));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("Arial", Font.BOLD, 14));
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(200, 40));
        loginBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            handleLogin(email, password, contentPanel, mainPanel);
        });
        loginPanel.add(loginBtn);
        loginPanel.add(Box.createVerticalStrut(15));
        
        JButton registerBtn = new JButton("Crear una cuenta");
        registerBtn.setForeground(new Color(0, 150, 0));
        registerBtn.setBorderPainted(false);
        registerBtn.setContentAreaFilled(false);
        registerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerBtn.addActionListener(e -> showRegisterScreen(contentPanel, mainPanel));
        loginPanel.add(registerBtn);
        
        contentPanel.add(loginPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    public void showRegisterScreen(JPanel contentPanel, JPanel mainPanel) {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(30, 80, 30, 80));
        
        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(new BoxLayout(registerPanel, BoxLayout.Y_AXIS));
        registerPanel.setBackground(Color.WHITE);
        registerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 150, 0), 2),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel title = new JLabel("Crear Cuenta");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerPanel.add(title);
        registerPanel.add(Box.createVerticalStrut(20));
        
        JTextField nameField = new JTextField();
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        JTextField emailField = new JTextField();
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        JPasswordField passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        JPasswordField confirmPasswordField = new JPasswordField();
        confirmPasswordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        JTextField addressField = new JTextField();
        addressField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        JTextField phoneField = new JTextField();
        phoneField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        
        addField(registerPanel, "Nombre completo:", nameField);
        addField(registerPanel, "Email:", emailField);
        addField(registerPanel, "Contraseña:", passwordField);
        addField(registerPanel, "Confirmar contraseña:", confirmPasswordField);
        addField(registerPanel, "Dirección:", addressField);
        addField(registerPanel, "Teléfono:", phoneField);
        
        registerPanel.add(Box.createVerticalStrut(20));
        
        JButton createBtn = new JButton("Crear Cuenta");
        createBtn.setBackground(new Color(0, 150, 0));
        createBtn.setForeground(Color.WHITE);
        createBtn.setFont(new Font("Arial", Font.BOLD, 14));
        createBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        createBtn.setMaximumSize(new Dimension(200, 40));
        createBtn.addActionListener(e -> {
            handleRegister(nameField.getText(), emailField.getText(),
                         new String(passwordField.getPassword()),
                         new String(confirmPasswordField.getPassword()),
                         addressField.getText(), phoneField.getText(),
                         contentPanel, mainPanel);
        });
        registerPanel.add(createBtn);
        registerPanel.add(Box.createVerticalStrut(10));
        
        JButton backBtn = new JButton("Volver al Login");
        backBtn.setForeground(new Color(0, 150, 0));
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        backBtn.addActionListener(e -> showLoginScreen(contentPanel, mainPanel));
        registerPanel.add(backBtn);
        
        JScrollPane scrollPane = new JScrollPane(registerPanel);
        scrollPane.setBorder(null);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void addField(JPanel panel, String label, JComponent field) {
        JLabel lbl = new JLabel(label);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lbl);
        panel.add(Box.createVerticalStrut(5));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(field);
        panel.add(Box.createVerticalStrut(10));
    }
    
    // ==================== PROFILE VIEW ====================
    public JPanel showProfileScreen(JPanel contentPanel, JPanel mainPanel) {
        if (currentUser == null) {
            showLoginScreen(contentPanel, mainPanel);
            return contentPanel;
        }
        
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Título
        JLabel title = new JLabel("Mi Perfil - " + currentUser.name);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(Color.WHITE);
        
        // Información personal
        JPanel infoPanel = createInfoPanel();
        mainContent.add(infoPanel);
        mainContent.add(Box.createVerticalStrut(15));
        
        // Historial de compras
        JPanel historyPanel = createPurchaseHistoryPanel();
        mainContent.add(historyPanel);
        mainContent.add(Box.createVerticalStrut(15));
        
        // Favoritos
        JPanel favoritesPanel = createFavoritesPanel();
        mainContent.add(favoritesPanel);
        
        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        
        // Botón cerrar sesión
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Color.WHITE);
        JButton logoutBtn = new JButton("Cerrar Sesión");
        logoutBtn.setBackground(Color.RED);
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.addActionListener(e -> {
            currentUser = null;
            JOptionPane.showMessageDialog(mainApp, "Sesión cerrada correctamente");
        });
        bottomPanel.add(logoutBtn);
        
        contentPanel.add(title, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        contentPanel.revalidate();
        contentPanel.repaint();
        
        return contentPanel;
    }
    
    private JPanel createInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createTitledBorder("Información Personal"));
        
        panel.add(new JLabel("📧 Email: " + currentUser.email));
        panel.add(new JLabel("📍 Dirección: " + currentUser.address));
        panel.add(new JLabel("📱 Teléfono: " + currentUser.phone));
        
        return panel;
    }
    
    private JPanel createPurchaseHistoryPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createTitledBorder("Últimas Compras (Máximo 3)"));
        
        if (currentUser.purchaseHistory.isEmpty()) {
            panel.add(new JLabel("No hay compras anteriores"));
        } else {
            for (Purchase purchase : currentUser.purchaseHistory) {
                JPanel purchaseCard = new JPanel(new BorderLayout(10, 5));
                purchaseCard.setBackground(Color.WHITE);
                purchaseCard.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.GRAY),
                    new EmptyBorder(10, 10, 10, 10)
                ));
                
                JPanel infoPanel = new JPanel();
                infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
                infoPanel.setBackground(Color.WHITE);
                
                infoPanel.add(new JLabel("<html><b>Pedido: " + purchase.purchaseId + "</b></html>"));
                infoPanel.add(new JLabel("Fecha: " + purchase.getFormattedDate()));
                infoPanel.add(new JLabel("Total: " + String.format("%.2f€", purchase.total)));
                infoPanel.add(new JLabel("Estado: " + purchase.status));
                
                JButton repeatBtn = new JButton("Repetir Compra");
                repeatBtn.setBackground(new Color(0, 150, 0));
                repeatBtn.setForeground(Color.WHITE);
                repeatBtn.addActionListener(e -> repeatPurchase(purchase));
                
                JButton trackBtn = new JButton("📦 Seguir Pedido");
                trackBtn.addActionListener(e -> showTracking(purchase));
                
                JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                btnPanel.setBackground(Color.WHITE);
                btnPanel.add(repeatBtn);
                btnPanel.add(trackBtn);
                
                purchaseCard.add(infoPanel, BorderLayout.CENTER);
                purchaseCard.add(btnPanel, BorderLayout.SOUTH);
                
                panel.add(purchaseCard);
                panel.add(Box.createVerticalStrut(10));
            }
        }
        
        return panel;
    }
    
    private JPanel createFavoritesPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createTitledBorder("Productos Favoritos"));
        
        if (currentUser.favoriteItems.isEmpty()) {
            panel.add(new JLabel("No tienes productos favoritos"));
        } else {
            for (SupermarketShoppingApp.Product product : currentUser.favoriteItems) {
                JPanel favCard = new JPanel(new BorderLayout(5, 5));
                favCard.setBackground(Color.WHITE);
                favCard.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.GRAY),
                    new EmptyBorder(5, 10, 5, 10)
                ));
                
                JLabel nameLabel = new JLabel(product.name + " - " + String.format("%.2f€", product.price));
                JButton removeBtn = new JButton("❌");
                removeBtn.addActionListener(e -> {
                    currentUser.removeFavorite(product);
                    saveUsers();
                    showProfileScreen((JPanel) mainApp.getContentPane().getComponent(0).getParent(), 
                                    (JPanel) mainApp.getContentPane());
                });
                
                favCard.add(nameLabel, BorderLayout.CENTER);
                favCard.add(removeBtn, BorderLayout.EAST);
                
                panel.add(favCard);
                panel.add(Box.createVerticalStrut(5));
            }
        }
        
        return panel;
    }
    
    // ==================== TRACKING ====================
    private void showTracking(Purchase purchase) {
        JDialog dialog = new JDialog(mainApp, "Seguimiento de Pedido", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(mainApp);
        dialog.setLayout(new BorderLayout());
        
        JPanel trackingPanel = new JPanel();
        trackingPanel.setLayout(new BoxLayout(trackingPanel, BoxLayout.Y_AXIS));
        trackingPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        trackingPanel.add(new JLabel("<html><h2>Pedido: " + purchase.purchaseId + "</h2></html>"));
        trackingPanel.add(Box.createVerticalStrut(20));
        
        String[] stages = {"Procesando", "En camino", "Entregado"};
        int currentStage = Arrays.asList(stages).indexOf(purchase.status);
        
        for (int i = 0; i < stages.length; i++) {
            JPanel stagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            if (i <= currentStage) {
                stagePanel.add(new JLabel("✅ " + stages[i]));
            } else {
                stagePanel.add(new JLabel("⭕ " + stages[i]));
            }
            trackingPanel.add(stagePanel);
        }
        
        trackingPanel.add(Box.createVerticalStrut(20));
        trackingPanel.add(new JLabel("Fecha estimada de entrega: 2 días hábiles"));
        
        JButton closeBtn = new JButton("Cerrar");
        closeBtn.addActionListener(e -> dialog.dispose());
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(closeBtn);
        
        dialog.add(trackingPanel, BorderLayout.CENTER);
        dialog.add(bottomPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    // ==================== HANDLERS ====================
    private void handleLogin(String email, String password, JPanel contentPanel, JPanel mainPanel) {
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(mainApp, "Por favor rellena todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        User user = userDatabase.get(email);
        if (user == null) {
            JOptionPane.showMessageDialog(mainApp, "Email no encontrado en nuestra base de datos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!user.password.equals(password)) {
            JOptionPane.showMessageDialog(mainApp, "Contraseña incorrecta", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        currentUser = user;
        JOptionPane.showMessageDialog(mainApp, "¡Bienvenido/a " + user.name + "!", "Login Exitoso", JOptionPane.INFORMATION_MESSAGE);
        showProfileScreen(contentPanel, mainPanel);
    }
    
    private void handleRegister(String name, String email, String password, String confirmPassword,
                               String address, String phone, JPanel contentPanel, JPanel mainPanel) {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(mainApp, "Por favor rellena todos los campos obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!email.matches("^[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) {
            JOptionPane.showMessageDialog(mainApp, "Email inválido", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(mainApp, "Las contraseñas no coinciden", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (userDatabase.containsKey(email)) {
            JOptionPane.showMessageDialog(mainApp, "Este email ya está registrado", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        User newUser = new User(email, password, name, address, phone);
        userDatabase.put(email, newUser);
        saveUsers();
        
        JOptionPane.showMessageDialog(mainApp, "Cuenta creada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        showLoginScreen(contentPanel, mainPanel);
    }
    
    private void handleForgotPassword(String email) {
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(mainApp, "Por favor introduce tu email", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (userDatabase.containsKey(email)) {
            JOptionPane.showMessageDialog(mainApp, 
                "Se ha enviado un email a " + email + " con instrucciones para recuperar tu contraseña", 
                "Email Enviado", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(mainApp, "Email no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void repeatPurchase(Purchase purchase) {
        int confirm = JOptionPane.showConfirmDialog(mainApp,
            "¿Quieres repetir esta compra?\nTotal: " + String.format("%.2f€", purchase.total),
            "Repetir Compra",
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            // Aquí podrías implementar la lógica para añadir los items al carrito
            JOptionPane.showMessageDialog(mainApp, 
                "Los productos han sido añadidos al carrito", 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    // ==================== PERSISTENCE ====================
    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_DATA_FILE))) {
            oos.writeObject(userDatabase);
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    private void loadUsers() {
        File file = new File(USER_DATA_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                userDatabase = (Map<String, User>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading users: " + e.getMessage());
                userDatabase = new HashMap<>();
            }
        }
    }
    
    // ==================== GETTERS / SETTERS ====================
    public User getCurrentUser() {
        return currentUser;
    }
    
    public boolean isLoggedIn() {
        return currentUser != null;
    }
    
    public void addPurchaseToHistory(Map<SupermarketShoppingApp.Product, Integer> cart, 
                                    double total, String paymentMethod) {
        if (currentUser != null) {
            Purchase purchase = new Purchase(cart, total, paymentMethod);
            currentUser.addPurchase(purchase);
            saveUsers();
        }
    }
    
    public void addToFavorites(SupermarketShoppingApp.Product product) {
        if (currentUser != null) {
            currentUser.addFavorite(product);
            saveUsers();
            JOptionPane.showMessageDialog(mainApp, 
                product.name + " añadido a favoritos", 
                "Favorito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(mainApp, 
                "Debes iniciar sesión para usar favoritos", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
