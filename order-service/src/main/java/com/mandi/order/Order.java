package com.mandi.order;

public class Order {

    private int orderId;
    private int produceId;
    private String produceName;
    private int quantityKg;
    private double totalInRupees;   // total in ₹

    public Order() {
    }

    public Order(int orderId, int produceId, String produceName,
                 int quantityKg, double totalInRupees) {
        this.orderId = orderId;
        this.produceId = produceId;
        this.produceName = produceName;
        this.quantityKg = quantityKg;
        this.totalInRupees = totalInRupees;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProduceId() {
        return produceId;
    }

    public void setProduceId(int produceId) {
        this.produceId = produceId;
    }

    public String getProduceName() {
        return produceName;
    }

    public void setProduceName(String produceName) {
        this.produceName = produceName;
    }

    public int getQuantityKg() {
        return quantityKg;
    }

    public void setQuantityKg(int quantityKg) {
        this.quantityKg = quantityKg;
    }

    public double getTotalInRupees() {
        return totalInRupees;
    }

    public void setTotalInRupees(double totalInRupees) {
        this.totalInRupees = totalInRupees;
    }
}
