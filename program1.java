import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Product {
    String name;
    double price;
    int quantity;

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
        this.quantity = 0;
    }
}

class Catalogue {
    Map<String, Product> products;

    public Catalogue() {
        this.products = new HashMap<>();
    }

    public void addProduct(Product product) {
        this.products.put(product.name, product);
    }

    public double getProductPrice(String name) {
        return this.products.get(name).price;
    }

    public int getProductQuantity(String name) {
        return this.products.get(name).quantity;
    }
}

public class ShoppingCart {
    public static void main(String[] args) {
        Catalogue catalogue = new Catalogue();
        catalogue.addProduct(new Product("Product A", 20));
        catalogue.addProduct(new Product("Product B", 40));
        catalogue.addProduct(new Product("Product C", 50));

        Map<String, Integer> cartQuantity = new HashMap<>();
        double cartTotal = 0;

        Scanner scanner = new Scanner(System.in);
        for (String productName : catalogue.products.keySet()) {
            System.out.print("Enter quantity for " + productName + ": ");
            int quantity = scanner.nextInt();
            cartQuantity.put(productName, quantity);
            cartTotal += quantity * catalogue.getProductPrice(productName);
        }

        String discountRule = applyDiscount(cartTotal, cartQuantity);

        double subtotal = cartTotal;
        double discountAmount = 0;
        if (discountRule != null) {
            double discount = (subtotal * Double.parseDouble(discountRule.split("_")[1])) / 100;
            subtotal -= discount;
            discountAmount = discount;
        }

        int shippingFee = (cartQuantity.values().stream().mapToInt(Integer::intValue).sum() / 10) * 5;
        int giftWrapFee = cartQuantity.values().stream().mapToInt(Integer::intValue).sum();

        double total = subtotal + shippingFee + giftWrapFee;

        System.out.println("\n=== Order Details ===");
        for (Map.Entry<String, Integer> entry : cartQuantity.entrySet()) {
            String productName = entry.getKey();
            int quantity = entry.getValue();
            System.out.println(productName + ": Quantity - " + quantity + ", Total - " + quantity * catalogue.getProductPrice(productName));
        }
        System.out.println("\nSubtotal: " + subtotal);
        System.out.println("Discount applied: " + (discountRule != null ? discountRule : "No discount applied"));
        System.out.println("Shipping fee: " + shippingFee);
        System.out.println("Gift wrap fee: " + giftWrapFee);
        System.out.println("\nTotal: " + total);
    }

    private static String applyDiscount(double cartTotal, Map<String, Integer> cartQuantity) {
        if (cartTotal > 200) {
            return "flat_10_discount";
        } else if (cartQuantity.values().stream().anyMatch(value -> value > 10)) {
            return "bulk_5_discount";
        } else if (cartQuantity.values().stream().mapToInt(Integer::intValue).sum() > 20) {
            return "bulk_10_discount";
        } else if (cartQuantity.values().stream().mapToInt(Integer::intValue).sum() > 30 &&
                cartQuantity.values().stream().anyMatch(value -> value > 15)) {
            return "tiered_50_discount";
        } else {
            return null;
        }
    }
}
