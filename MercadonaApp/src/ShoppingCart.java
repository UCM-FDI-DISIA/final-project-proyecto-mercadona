import java.util.HashMap;
import java.util.Map;

class ShoppingCart {
    private Map<Product, Integer> items;
    
    public ShoppingCart() {
        items = new HashMap<>();
    }
    
    public void addItem(Product product) {
        items.put(product, items.getOrDefault(product, 0) + 1);
    }
    
    public Map<Product, Integer> getItems() {
        return items;
    }
    
    public double getTotal() {
        double total = 0;
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            total += entry.getKey().getPrice() * entry.getValue();
        }
        return total;
    }
    
    public void clear() {
        items.clear();
    }
    
    public boolean isEmpty() {
        return items.isEmpty();
    }
}