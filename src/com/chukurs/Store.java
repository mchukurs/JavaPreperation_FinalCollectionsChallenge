package com.chukurs;

import java.time.LocalDate;
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

        myStore.listProductsByCategory(false, true);
        myStore.carts.forEach(System.out::println);
        myStore.abandonCarts();
        myStore.listProductsByCategory(false,true);
        myStore.carts.forEach(System.out::println);
    }

    private void manageStoreCarts() {
        Cart cart1 = new Cart(Cart.CartType.PHYSICAL, 1);
        carts.add(cart1);
        InventoryItem item = aisleInventory.get(Category.FRESH_PRODUCE).get("apple");
        cart1.addItem(item, 6);

        cart1.addItem(aisleInventory.get(Category.FRESH_PRODUCE).get("banana"), 5);
        cart1.addItem(aisleInventory.get(Category.FRESH_MEAT).get("bacon"), 3);
        cart1.addItem(aisleInventory.get(Category.BEVERAGE).get("coffee"), 1);
        cart1.removeItem(aisleInventory.get(Category.FRESH_PRODUCE).get("banana"), 3);

        Cart cart2 = new Cart(Cart.CartType.VIRTUAL, 1);
        carts.add(cart2);
        //20 lemons by using SKU, as its VIRTUAL cart
        cart2.addItem(inventory.get("L103"), 20);
        //10 bananas using SKU
        cart2.addItem(inventory.get("B100"), 10);

        //cart that should not get removed
        Cart cart3 = new Cart(Cart.CartType.VIRTUAL, 0);
        carts.add(cart3);
        //998 rice chex by using SKU
        cart3.addItem(inventory.get("R777"), 998);
        if (!checkOutCart(cart3)) {
            System.out.println("Something went wrong, could not check out");
        }
        Cart cart4 = new Cart(Cart.CartType.PHYSICAL, 0);
        carts.add(cart4);
        //1 tea
        cart4.addItem(aisleInventory.get(Category.BEVERAGE).get("tea"), 1);

    }

    private boolean checkOutCart(Cart cart) {
        for (var cartItem : cart.getProducts().entrySet()) {
            //gets SKU
            var item = inventory.get(cartItem.getKey());
            int qty = cartItem.getValue();
            if (!item.sellItem(qty)) return false;
            //if did not break, that means sellItem worked for every item
        }
        cart.printSalesSlip(inventory);
        carts.remove(cart);
        return true;
    }

    private void abandonCarts() {
        int dayOfYear = LocalDate.now().getDayOfYear();
        //last cart in set that is not with today date
        Cart lastCart = null;
        for (Cart cart : carts) {
            if (cart.getCartDate().getDayOfYear() == dayOfYear) {
                break;
            }
            lastCart = cart;
        }
        //carts.removeAll(carts.subSet(carts.getFirst(),true,lastCart,true));

        //create a view
        var oldCarts = carts.headSet(lastCart, true);
        Cart abandonedCart = null;
        //pollFirst is removing it
        while ((abandonedCart = oldCarts.pollFirst()) != null) {
            for (String sku : abandonedCart.getProducts().keySet()) {
                InventoryItem item = inventory.get(sku);
                item.releaseItem(abandonedCart.getProducts().get(sku));
            }
        }


    }

    private void listProductsByCategory() {
        listProductsByCategory(true, false);
    }

    private void listProductsByCategory(boolean includeHeader, boolean includeDetail) {
        aisleInventory.keySet().forEach(k -> {

            if (includeHeader) System.out.println(k);

            if (!includeDetail) {
                aisleInventory.get(k).keySet().forEach(System.out::println);
            } else {
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
