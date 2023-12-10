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
        myStore.stockAisles();
        myStore.listProductsByCategory();
        myStore.manageStoreCarts();

        myStore.listProductsByCategory(false,true);

    }

    private void manageStoreCarts() {
        Cart cart1 = new Cart(Cart.CartType.PHYSICAL, 1);
        carts.add(cart1);
        InventoryItem item = aisleInventory.get(Category.FRESH_PRODUCE).get("apple");
        cart1.addItem(item,6);

        cart1.addItem(aisleInventory.get(Category.FRESH_PRODUCE).get("banana"),5);
        cart1.addItem(aisleInventory.get(Category.FRESH_MEAT).get("bacon"),3);
        cart1.addItem(aisleInventory.get(Category.BEVERAGE).get("coffee"),1);
        System.out.println(cart1);
        System.out.println("now removing bananas (had 5 in cart, now removing 3");
        cart1.removeItem(aisleInventory.get(Category.FRESH_PRODUCE).get("banana"),3);
        System.out.println(cart1);
    }

    private boolean checkOutCart(Cart cart) {

        return false;
    }

    private void abandonCarts() {

    }

    private void listProductsByCategory() {
        listProductsByCategory(true,false);
    }
    private void listProductsByCategory(boolean includeHeader, boolean includeDetail) {
        aisleInventory.keySet().forEach(k -> {
            System.out.println("-".repeat(20) + "\n");
            if(includeHeader) System.out.println( k);
            System.out.println("\n" + "-".repeat(20));
           if(!includeDetail) {
               aisleInventory.get(k).keySet().forEach(System.out::println);
           }else{
               aisleInventory.get(k).values().forEach(System.out::println);
           }
        });
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
        aisleInventory = new EnumMap<>(Category.class);
        for (InventoryItem item : inventory.values()) {
            Category aisle = item.getProduct().category();

            Map<String, InventoryItem> productMap = aisleInventory.get(aisle);
            if (productMap == null) {
                productMap = new TreeMap<>();
            }
            //storing products by name
            productMap.put(item.getProduct().name(), item);
            aisleInventory.putIfAbsent(aisle, productMap);

            //private Map<Category, Map<String, InventoryItem>> aisleInventory;
        }
    }

    private void listInventory() {
        System.out.println("-".repeat(20));
        inventory.values().forEach(System.out::println);
    }

}
