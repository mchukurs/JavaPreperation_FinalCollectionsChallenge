package com.chukurs;

enum Category {
    BAKERY, BAKING_SPICES, CANNED, DAIRY, FRESH_PRODUCE, FROZEN_FOODS, FRESH_MEAT, PASTA_LENTOS, BEVERAGE,OTHER;

}
public record Product(String sku, String name, String manufacturer, Category category) {
}
