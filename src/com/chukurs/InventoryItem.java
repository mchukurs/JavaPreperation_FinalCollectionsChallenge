package com.chukurs;

public class InventoryItem {
    private Product product;
    private double price;

    //qtyTotal is amount that you still have (cart,aisle,warehouse)
    private int qtyTotal;


    //qtyReserved is in cart but not yet sold
    private int qtyReserved;
    //qtyReorder is the amount of usual re-order (to stock up)
    private int qtyReorder;
    //qtyLow is the threshold that triggers automatic re-order
    private int qtyLow;

    public InventoryItem(Product product, double price, int qtyTotal, int qtyLow) {
        this.product = product;
        this.price = price;
        this.qtyTotal = qtyTotal;
        this.qtyLow = qtyLow;
        this.qtyReorder = qtyTotal;
    }

    public Product getProduct() {
        return product;
    }

    public double getPrice() {
        return price;
    }

    public boolean reserveItem(int qty) {
        if ((qtyTotal - qtyReserved) >= qty) {
            qtyReserved += qty;
            return true;
        }
        return false;
    }
    /**
     * used when: 1-user wants to remove an item from cart;2-automatic release of carts occurs
     * @param qty The quantity to be released from cart
     */
    public void releaseItem(int qty){
        qtyReserved -= qty;
    }

    public boolean sellItem(int qty){
        //during checkout
        if(qtyTotal >=qty){
            qtyTotal -= qty;
            qtyReserved -= qty;
            if(qtyTotal <= qtyLow){
                placeInventoryOrder();
            }
            return true;
        }
        return false;
    }
    private void placeInventoryOrder(){
        System.out.printf("Ordering qty %d : %s%n",qtyReorder,product);
       // this.qtyTotal += qtyReorder;
    }

    @Override
    public String toString() {
        return "%s, $%.2f : [%04d, % 2d]".formatted(product, price, qtyTotal,qtyReserved);
    }
}
