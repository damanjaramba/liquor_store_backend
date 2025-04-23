package com.example.liquorstore.enums;


public enum Category {
    BEER,WINE,SPIRITS,COCKTAILS,WHISKEY,VODKA,RUM,GIN,TEQUILA,BRANDY,CIDER;

    public static Category fromString(String category) {
        try {
            return Category.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid category: " + category);
        }
    }
}
