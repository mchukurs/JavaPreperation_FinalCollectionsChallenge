package com.chukurs;

import java.util.*;

public class Store {
    //Random is used to generate random prices, qty for items (testing)
    private static Random random = new Random();

    //inventory is a map (SKU, InventoryItem)
    private Map<String, InventoryItem> inventory;

    // carts is a NavigableSet so make it easy to look for abandoned carts
    //comparator is the ID, so its sorted that way
    private NavigableSet<Cart> carts = new TreeSet<>(Comparator.comparing(Cart::getId));

    //(Category):(name: InventoryItem)
    private Map<Category, Map<String, InventoryItem>> aisleInventory;

    public static void main(String[] args) {
        System.out.println("hello from store");
        Store myStore = new Store();
        myStore.stockStore();
        myStore.listInventory();
    }

    private void manageStoreCarts() {

    }

    private boolean checkOutCart(Cart cart) {

        return false;
    }

    private void abandonCarts() {

    }

    private void listProductsByCategory() {

    }

    //helper methods to setup store
    private void stockStore() {
        inventory = new HashMap<>();
        List<Product> products = new ArrayList<>(List.of(
                new Product("A100", "apple", "local", Category.FRESH_PRODUCE),
                new Product("B100", "banana", "local", Category.FRESH_PRODUCE),
                new Product("P100", "pear", "local", Category.FRESH_PRODUCE),
                new Product("L103", "lemon", "local", Category.FRESH_PRODUCE),
                new Product("M201", "milk", "farm", Category.DAIRY),
                new Product("Y001", "yogurt", "farm", Category.DAIRY),
                new Product("C333", "cheese", "farm", Category.DAIRY),
                new Product("R777", "rice chex", "Nabisco", Category.BAKERY),
                new Product("G111", "granola", "Nat Valley", Category.BAKERY),
                new Product("BB11", "ground beef", "butcher", Category.FRESH_MEAT),
                new Product("CC11", "chicken", "butcher", Category.FRESH_MEAT),
                new Product("BC11", "bacon", "butcher", Category.FRESH_MEAT),
                new Product("BC77", "coke", "coca cola", Category.BEVERAGE),
                new Product("BC88", "coffee", "value", Category.BEVERAGE),
                new Product("BC99", "tea", "herbal", Category.BEVERAGE)
        ));
        products.forEach(p -> inventory.put(p.sku(), new InventoryItem(p, random.nextDouble(0, 1.25), 1000, 5)));
    }

    private void stockAisles() {

    }
private void listInventory(){
    System.out.println("-".repeat(20));
    inventory.values().forEach(System.out::println);
}

}
