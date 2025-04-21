package com.example.liquorstore.dto;

import jakarta.persistence.Column;

import java.io.Serializable;
import java.math.BigDecimal;

public class LiquorDto implements Serializable {
    private String title;

    private BigDecimal price;

    private String imageUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
