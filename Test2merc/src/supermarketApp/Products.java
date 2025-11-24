package supermarketApp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import supermarketApp.SupermarketShoppingApp.*;

public class Products {
	
	private JTextField searchField;
	private SupermarketShoppingApp app;
	
	public Products(SupermarketShoppingApp app, JTextField searchField) {
		this.app = app;
		this.searchField= searchField;
	}

	public void initializeProducts(List<Product> allProducts) {        
        // Mercadona - Hacendado
        allProducts.add(new Product("Leche Entera Hacendado", "Mercadona", "Hacendado", "Lácteos", 0.95, 50));
        allProducts.add(new Product("Pan de Molde Hacendado", "Mercadona", "Hacendado", "Panadería", 0.85, 30));
        allProducts.add(new Product("Yogur Natural Hacendado", "Mercadona", "Hacendado", "Lácteos", 1.20, 40));
        allProducts.add(new Product("Galletas María Hacendado", "Mercadona", "Hacendado", "Dulces", 0.75, 60));
        allProducts.add(new Product("Aceite de Oliva Hacendado", "Mercadona", "Hacendado", "Aceites", 3.50, 25));
        
        // Día
        allProducts.add(new Product("Leche Semidesnatada Día", "Día", "Día", "Lácteos", 0.89, 45));
        allProducts.add(new Product("Cereales Chocolate Día", "Día", "Día", "Desayuno", 2.10, 35));
        allProducts.add(new Product("Detergente Lavadora Día", "Día", "Día", "Limpieza", 4.50, 20));
        allProducts.add(new Product("Pasta Espagueti Día", "Día", "Día", "Pasta", 0.65, 55));
        
        // Lidl
        allProducts.add(new Product("Chocolate Negro Lidl", "Lidl", "Lidl", "Dulces", 1.29, 40));
        allProducts.add(new Product("Café Molido Lidl", "Lidl", "Lidl", "Café", 2.99, 30));
        allProducts.add(new Product("Queso Gouda Lidl", "Lidl", "Lidl", "Lácteos", 1.85, 28));
        
        // Marcas comunes en varios supermercados
        allProducts.add(new Product("Yogur Danone Natural", "Mercadona", "Danone", "Lácteos", 2.40, 50));
        allProducts.add(new Product("Yogur Danone Natural", "Día", "Danone", "Lácteos", 2.35, 45));
        allProducts.add(new Product("Yogur Danone Natural", "Lidl", "Danone", "Lácteos", 2.30, 40));
        
        allProducts.add(new Product("Cola Cao Original", "Mercadona", "Cola Cao", "Desayuno", 3.20, 35));
        allProducts.add(new Product("Cola Cao Original", "Día", "Cola Cao", "Desayuno", 3.15, 30));
        
        allProducts.add(new Product("Detergente Ariel", "Mercadona", "Ariel", "Limpieza", 8.50, 22));
        allProducts.add(new Product("Detergente Ariel", "Lidl", "Ariel", "Limpieza", 8.30, 18));
    }
	
	public void displayProducts(List<Product> products) {
        JPanel productsPanel = new JPanel(new GridLayout(0, 3, 15, 15));
        productsPanel.setBackground(Color.WHITE);
        
        if (products.isEmpty()) {
            JLabel noProducts = new JLabel("No se encontraron productos");
            noProducts.setFont(new Font("Arial", Font.BOLD, 16));
            noProducts.setHorizontalAlignment(SwingConstants.CENTER);
            productsPanel.add(noProducts);
        } else {
            for (Product p : products) {
                productsPanel.add(createProductCard(p));
            }
        }
        
        app.checkComponentPanel(productsPanel);
    }
	
	public JPanel createProductCard(Product p) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(new Color(250, 250, 250));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        // Información del producto
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(250, 250, 250));
        
        JLabel nameLabel = new JLabel("<html><b>" + p.name + "</b></html>");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JLabel supermarketLabel = new JLabel(p.supermarket);
        supermarketLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        supermarketLabel.setForeground(Color.GRAY);
        
        JLabel brandLabel = new JLabel("Marca: " + p.brand);
        brandLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        
        JLabel categoryLabel = new JLabel("Categoría: " + p.category);
        categoryLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        
        JLabel priceLabel = new JLabel(String.format("%.2f€", p.price));
        priceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        priceLabel.setForeground(new Color(0, 120, 0));
        
        JLabel stockLabel = new JLabel(p.stock > 0 ? "Stock: " + p.stock : "Sin stock");
        stockLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        stockLabel.setForeground(p.stock > 0 ? Color.BLACK : Color.RED);
        
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(supermarketLabel);
        infoPanel.add(brandLabel);
        infoPanel.add(categoryLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(priceLabel);
        infoPanel.add(stockLabel);
        
        // Botón añadir
        JButton addButton = new JButton("Añadir al carrito");
        addButton.setBackground(new Color(0, 150, 0));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setEnabled(p.stock > 0);
        addButton.addActionListener(e -> app.addToCart(p));
        
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(addButton, BorderLayout.SOUTH);
        
        return card;
    }
	
	public void searchProducts(List<Product> allProducts) {
        String query = searchField.getText().trim().toLowerCase();
        if (query.isEmpty()) {
            displayProducts(allProducts);
            return;
        }
        
        List<Product> results = new ArrayList<>();
        for (Product p : allProducts) {
            if (p.name.toLowerCase().contains(query) || 
                p.brand.toLowerCase().contains(query) ||
                p.category.toLowerCase().contains(query)) {
                results.add(p);
            }
        }
        
        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(this.app, 
                "No se encontraron productos que coincidan con: " + query, 
                "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
        }
        
        displayProducts(results);
    }
	
}
