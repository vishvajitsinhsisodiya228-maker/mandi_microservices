package com.mandi.produce;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class Produce {

    private int id;

    @NotBlank(message = "name must not be empty")
    private String name;

    @Positive(message = "pricePerKg must be greater than 0")
    private double pricePerKg;   // in rupees

    @PositiveOrZero(message = "stockKg must not be negative")
    private int stockKg;

    public Produce() {
    }

    public Produce(int id, String name, double pricePerKg, int stockKg) {
        this.id = id;
        this.name = name;
        this.pricePerKg = pricePerKg;
        this.stockKg = stockKg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPricePerKg() {
        return pricePerKg;
    }

    public void setPricePerKg(double pricePerKg) {
        this.pricePerKg = pricePerKg;
    }

    public int getStockKg() {
        return stockKg;
    }

    public void setStockKg(int stockKg) {
        this.stockKg = stockKg;
    }
}
