package com.mandi.monolith;

public class Produce {

    private int id;
    private String name;
    private double pricePerKg;   // in rupees
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
