package supermarketApp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import supermarketApp.SupermarketShoppingApp.Product;

public class ShoppingCart{
	
	private JLabel cartCountLabel;
	public JPanel mainPanel;
    public JPanel contentPanel;
    private SupermarketShoppingApp app;
	
	public ShoppingCart(SupermarketShoppingApp app, JLabel cartCountLabel, JPanel mainPanel, JPanel contentPanel) {
		this.cartCountLabel = cartCountLabel;
		this.mainPanel = mainPanel;
		this.contentPanel = contentPanel;
		this.app = app;
	}
	
	public void setCartCount(JLabel newCartCount) {
		this.cartCountLabel = newCartCount;
	}
	
	public void setMainPanel(JPanel newMainPanel) {
		this.mainPanel = newMainPanel;
	}
	
	public void setContentPanel(JPanel newContentPanel) {
		this.contentPanel = newContentPanel;
	}

	public void addToCart(Product p, Map<Product, Integer> cart) {
        if (p.stock <= 0) {
            app.noMoreStock();
            return;
        }
        
        int currentQuantity = cart.getOrDefault(p, 0);
        if (currentQuantity < p.stock) {
            cart.put(p, currentQuantity + 1);
            updateCartCount(cart);
            app.addedToCart(this, p);
        } else {
            app.noMoreStock();
        }
    }
    
    public void updateCartCount(Map<Product, Integer> cart) {
        int total = cart.values().stream().mapToInt(Integer::intValue).sum();
        cartCountLabel.setText("(" + total + ")");
    }
    
    public JPanel showCart(Map<Product, Integer> cart) {
        // IMPORTANTE: Limpiar el contentPanel antes de crear uno nuevo
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
                    updateCartCount(cart);
                    showCart(cart);
                });
                
                JLabel qtyLabel = new JLabel(" " + quantity + " ");
                
                JButton increaseBtn = new JButton("+");
                increaseBtn.addActionListener(e -> {
                    if (quantity < p.stock) {
                        cart.put(p, quantity + 1);
                        updateCartCount(cart);
                        showCart(cart);
                    } else {
                       app.noMoreStock();
                    }
                });
                
                JButton removeBtn = new JButton("🗑");
                removeBtn.addActionListener(e -> {
                    cart.remove(p);
                    updateCartCount(cart);
                    showCart(cart);
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
            checkoutBtn.addActionListener(e -> app.showCheckout(total));
            
            JPanel bottomPanel = new JPanel();
            bottomPanel.setBackground(Color.WHITE);
            bottomPanel.add(checkoutBtn);
            contentPanel.add(bottomPanel, BorderLayout.SOUTH);
        }
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
        
        return contentPanel;
    } 
   }