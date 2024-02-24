class ProductInfo:
    def __init__(self, name, price):
        self.name = name
        self.price = price
        self.quantity = 0

class ProductCatalogue:
    def __init__(self):
        self.products = {}

    def add_product(self, product):
        self.products[product.name] = product

    def get_product_price(self, name):
        return self.products[name].price

    def get_product_quantity(self, name):
        return self.products[name].quantity

def apply_discount_rule(cart_total, cart_quantity):
    discount_rules = {
        "flat_10_discount": (cart_total > 200, 10),
        "bulk_5_discount": (any(cart_quantity[name] > 10 for name in cart_quantity), 5),
        "bulk_10_discount": (sum(cart_quantity.values()) > 20, 10),
        "tiered_50_discount": (sum(cart_quantity.values()) > 30 and any(cart_quantity[name] > 15 for name in cart_quantity), 50)
    }

    applicable_discounts = {rule: discount for rule, (is_applicable, discount) in discount_rules.items() if is_applicable}
    if not applicable_discounts:
        return None, 0

    max_discount_rule = max(applicable_discounts, key=applicable_discounts.get)
    return max_discount_rule, applicable_discounts[max_discount_rule]

def main_shopping_cart():
    catalogue = ProductCatalogue()
    catalogue.add_product(ProductInfo("Product A", 20))
    catalogue.add_product(ProductInfo("Product B", 40))
    catalogue.add_product(ProductInfo("Product C", 50))

    cart_quantity = {}
    cart_total = 0
    for product_name in catalogue.products.keys():
        quantity = int(input(f"Enter quantity for {product_name}: "))
        cart_quantity[product_name] = quantity
        cart_total += quantity * catalogue.get_product_price(product_name)

    discount_rule, discount_amount = apply_discount_rule(cart_total, cart_quantity)

    subtotal = cart_total
    if discount_rule:
        discount = (subtotal * discount_amount) / 100
        subtotal -= discount

    shipping_fee = (sum(cart_quantity.values()) // 10) * 5
    gift_wrap_fee = sum(cart_quantity.values())

    total = subtotal + shipping_fee + gift_wrap_fee

    print("\n=== Order Details ===")
    for product_name, quantity in cart_quantity.items():
        print(f"{product_name}: Quantity - {quantity}, Total - {quantity * catalogue.get_product_price(product_name)}")
    print(f"\nSubtotal: {subtotal}")
    print(f"Discount applied: {discount_rule} - {discount_amount}%") if discount_rule else print("No discount applied")
    print(f"Shipping fee: {shipping_fee}")
    print(f"Gift wrap fee: {gift_wrap_fee}")
    print(f"\nTotal: {total}")

if __name__ == "__main__":
    main_shopping_cart()
