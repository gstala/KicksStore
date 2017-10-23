package com.stala.grzegorz.utility;

public enum ShippingMethod {

    ECONOMIC_PARCEL(10,5),
    PRIORITY_PARCEL(20,2);

    private double cost;
    private int estimatedDelivery;

    ShippingMethod(double cost, int estimatedDelivery) {
        this.cost = cost;
        this.estimatedDelivery = estimatedDelivery;
    }

    public double getCost() {
        return cost;
    }

    public int getEstimatedDelivery() {
        return estimatedDelivery;
    }
}
