package com.chukurs;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

enum CartType {PHYSICAL, VIRTUAL};

public class Cart {
    //it belongs to class, so can be used as bases for generation
    private static int lastId = 1;

    private int id;
    private LocalDate cartDate;
    //key is product SKU and value will be quantity placed in the cart
    private Map<String, Integer> products;
    private CartType type;

    public Cart(CartType type, int days) {
        //adjusted 'days' are for testing purposes.
        this.type = type;
        id = lastId++;
        cartDate = LocalDate.now().minusDays(days);
        products = new HashMap<>();
    }

    public Cart(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public LocalDate getCartDate() {
        return cartDate;
    }

    public void addItem(InventoryItem item, int qty) {
        if (item.reserveItem(qty)) {
            //integer::sum is a reference to add up the values of the map entry values (if had 2, and add 3 more, then set 5)
            products.merge(item.getProduct().sku(), qty, Integer::sum);
        } else {
            System.out.println("not enough in stock");
        }

    }

    public void removeItem(InventoryItem item, int qty) {
        //current will store the VALUE of key-value
        int current = products.get(item.getProduct().sku());

        if (current <= qty) {
            qty = current;
            products.remove(item.getProduct().sku());
            System.out.println("Tried to remove more than you had in cart. Removed %s from cart!%n".formatted(item.getProduct().name()));
        } else {
            products.merge(item.getProduct().sku(), qty, (oldVal, newVal) -> oldVal - newVal);
            System.out.printf("%d [%s]s removed %n", qty, item.getProduct().name());

            products.replace(String.valueOf(current), products.get(current), products.get(current) - qty);
        }

        item.releaseItem(qty);
    }

    public void printSalesSlip(Map<String, InventoryItem> inventory) {
        //stores inventory is passed because that is where the PRICE is stored
        double total = 0;
        System.out.println("-".repeat(20));
        System.out.println("Thank you for your sale: ");
        for (var cartItem : products.entrySet()) {
            var item = inventory.get(cartItem.getKey());
            int qty = cartItem.getValue();
            double itemizedPrice = qty * item.getPrice();
            total += itemizedPrice;
            System.out.printf("\t%s %-10s (%d)@ $%.2f = $%.2f%n",
                    cartItem.getKey(), item.getProduct().name(),qty,
                    item.getPrice(), itemizedPrice);
        }
        System.out.printf("Total Sale: $%.2f%n", total);
        System.out.println("-".repeat(20));
    }

    @Override
    public String toString() {
        return "Cart{"+
                "id=" + id+
                ", cartDate="+cartDate+
                ", products="+products+
                "}";
    }
}
