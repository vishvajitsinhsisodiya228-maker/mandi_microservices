package com.mandi.order;

/**
 * order-service's own small copy of the shape produce-service returns.
 * Only used to read the reply; no produce data is stored in order-service.
 */
public class ProduceDto {

    private int id;
    private String name;
    private double pricePerKg;
    private int stockKg;

    public ProduceDto() {
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
