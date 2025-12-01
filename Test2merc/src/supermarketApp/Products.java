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
import java.awt.image.BufferedImage;
import java.net.URI;
import javax.imageio.ImageIO;
import java.awt.Dimension;
import java.awt.Image;
import javax.swing.ImageIcon;

import supermarketApp.SupermarketShoppingApp.*;

public class Products {
	
	private JTextField searchField;
	private SupermarketShoppingApp app;
	
	public Products(SupermarketShoppingApp app, JTextField searchField) {
		this.app = app;
		this.searchField= searchField;
	}
	
	public void setSearchfield(JTextField newSearchField) {
		this.searchField = newSearchField;
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
        
        
     // Pasta, Arroz y Legumbres
        allProducts.add(new Product("Espaguetis 500g", "Mercadona", "Gallo", "Pasta", 1.10, 300));
        allProducts.add(new Product("Arroz Basmati 1kg", "Lidl", "Brillante", "Arroz", 2.45, 180));
        allProducts.add(new Product("Lentejas 500g", "Carrefour", "Carrefour", "Legumbres", 1.25, 150));
        allProducts.add(new Product("Macarrones 500g", "Alcampo", "Barilla", "Pasta", 1.35, 200));
        allProducts.add(new Product("Garbanzos Cocidos 400g", "Mercadona", "Hacendado", "Legumbres", 0.85, 220));

        // Aceites y Condimentos
        allProducts.add(new Product("Aceite de Oliva Virgen Extra 1L", "Mercadona", "Hacendado", "Aceites", 4.99, 140));
        allProducts.add(new Product("Vinagre de Vino 500ml", "Carrefour", "Carrefour", "Condimentos", 0.95, 100));
        allProducts.add(new Product("Sal Marina 1kg", "Lidl", "Alipende", "Condimentos", 0.45, 280));
        allProducts.add(new Product("Mayonesa 450ml", "Alcampo", "Hellmanns", "Condimentos", 2.35, 120));
        allProducts.add(new Product("Ketchup 560g", "Mercadona", "Heinz", "Condimentos", 2.15, 150));

        // Bebidas
        allProducts.add(new Product("Agua Mineral 1.5L Pack 6", "Carrefour", "Font Vella", "Bebidas", 2.10, 400));
        allProducts.add(new Product("Coca Cola 2L", "Mercadona", "Coca Cola", "Refrescos", 1.85, 300));
        allProducts.add(new Product("Zumo de Naranja 1L", "Lidl", "Don Simon", "Zumos", 1.45, 180));
        allProducts.add(new Product("Cerveza Pack 6", "Alcampo", "Mahou", "Alcohol", 3.99, 250));
        allProducts.add(new Product("Vino Tinto Crianza", "Carrefour", "Marqués de Cáceres", "Alcohol", 6.50, 90));

        // Snacks y Dulces
        allProducts.add(new Product("Patatas Fritas 150g", "Mercadona", "Lays", "Snacks", 1.75, 200));
        allProducts.add(new Product("Chocolate con Leche 100g", "Lidl", "Milka", "Dulces", 1.20, 180));
        allProducts.add(new Product("Galletas María 800g", "Carrefour", "Fontaneda", "Dulces", 1.99, 150));
        allProducts.add(new Product("Frutos Secos Mixtos 200g", "Alcampo", "Hacendado", "Snacks", 2.85, 120));
        allProducts.add(new Product("Chocolate Negro 70% 100g", "Mercadona", "Nestlé", "Dulces", 1.65, 140));

        // Congelados
        allProducts.add(new Product("Pizza 4 Quesos", "Lidl", "Dr. Oetker", "Congelados", 3.45, 100));
        allProducts.add(new Product("Guisantes Congelados 1kg", "Carrefour", "Carrefour", "Congelados", 1.89, 160));
        allProducts.add(new Product("Helado de Vainilla 900ml", "Mercadona", "Häagen-Dazs", "Congelados", 4.99, 80));
        allProducts.add(new Product("Lasaña Boloñesa 400g", "Alcampo", "Findus", "Congelados", 3.25, 90));
        allProducts.add(new Product("Croquetas Jamón 500g", "Lidl", "La Cocinera", "Congelados", 2.99, 110));

        // Limpieza y Hogar
        allProducts.add(new Product("Detergente Líquido 40 lavados", "Mercadona", "Deliplus", "Limpieza", 5.95, 120));
        allProducts.add(new Product("Suavizante 1.5L", "Carrefour", "Vernel", "Limpieza", 2.45, 140));
        allProducts.add(new Product("Lavavajillas 650ml", "Lidl", "Fairy", "Limpieza", 1.85, 180));
        allProducts.add(new Product("Papel Higiénico 12 rollos", "Alcampo", "Scottex", "Hogar", 4.50, 200));
        allProducts.add(new Product("Servilletas Pack 100", "Mercadona", "Hacendado", "Hogar", 0.95, 250));
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
	    
	    // Panel de imagen del producto
	    JPanel imagePanel = new JPanel(new BorderLayout());
	    imagePanel.setPreferredSize(new Dimension(180, 150));
	    imagePanel.setBackground(Color.WHITE);
	    imagePanel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
	    
	    try {
	        // Generar URL de imagen basada en el producto
	        String imageUrl = getProductImageUrl(p);
	        URI uri = new URI(imageUrl);
	        BufferedImage originalImage = ImageIO.read(uri.toURL());
	        Image scaledImage = originalImage.getScaledInstance(180, 150, Image.SCALE_SMOOTH);
	        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
	        imagePanel.add(imageLabel, BorderLayout.CENTER);
	    } catch (Exception e) {
	        // Fallback: mostrar un panel con el icono de la categoría
	        JLabel placeholderLabel = new JLabel(getCategoryIcon(p.category), SwingConstants.CENTER);
	        placeholderLabel.setFont(new Font("Arial", Font.PLAIN, 10));
	        imagePanel.add(placeholderLabel, BorderLayout.CENTER);
	    }
	    
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
	    
	    card.add(imagePanel, BorderLayout.NORTH);
	    card.add(infoPanel, BorderLayout.CENTER);
	    card.add(addButton, BorderLayout.SOUTH);
	    
	    return card;
	}

	private String getProductImageUrl(Product p) {
	    // Mapear productos a imágenes reales
	    String productName = p.name.toLowerCase();
	    
	    if (productName.contains("leche")) {
	        return "https://images.unsplash.com/photo-1550583724-b2692b85b150?w=400";
	    } else if (productName.contains("pan")) {
	        return "https://images.unsplash.com/photo-1509440159596-0249088772ff?w=400";
	    } else if (productName.contains("yogur")) {
	        return "https://images.unsplash.com/photo-1488477181946-6428a0291777?w=400";
	    } else if (productName.contains("galletas")) {
	        return "https://images.unsplash.com/photo-1558961363-fa8fdf82db35?w=400";
	    } else if (productName.contains("aceite")) {
	        return "https://images.unsplash.com/photo-1474979266404-7eaacbcd87c5?w=400";
	    } else if (productName.contains("cereales")) {
	        return "https://www.dia.es/product_images/74305/74305_ISO_0_ES.jpg";
	    } else if (productName.contains("detergente")) {
	        return "https://images.unsplash.com/photo-1563453392212-326f5e854473?w=400";
	    } else if (productName.contains("pasta") || productName.contains("espagueti")) {
	        return "https://images.unsplash.com/photo-1621996346565-e3dbc646d9a9?w=400";
	    } else if (productName.contains("chocolate")) {
	        return "https://elavegan.com/wp-content/uploads/2023/01/homemade-chocolate-bars.jpg";
	    } else if (productName.contains("café")) {
	        return "https://images.unsplash.com/photo-1447933601403-0c6688de566e?w=400";
	    } else if (productName.contains("queso")) {
	        return "https://images.unsplash.com/photo-1486297678162-eb2a19b0a32d?w=400";
	    } else if (productName.contains("cola cao")) {
	        return "https://images.unsplash.com/photo-1517487881594-2787fef5ebf7?w=400";
	    }
	    
	    return "https://images.unsplash.com/photo-1534723452862-4c874018d66d?w=400"; // Imagen genérica de supermercado
	}

	private String getCategoryIcon(String category) {
	    // Texto de fallback según la categoría
	    switch (category.toLowerCase()) {
	        case "lácteos": return "🥛";
	        case "panadería": return "🍞";
	        case "dulces": return "🍪";
	        case "aceites": return "🫒";
	        case "desayuno": return "☕";
	        case "limpieza": return "🧼";
	        case "pasta": return "🍝";
	        case "café": return "☕";
	        default: return "🛒";
	    }
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
