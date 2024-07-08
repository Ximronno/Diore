package ximronno.api.interfaces;

import java.util.Map;

public class Transaction {

    private final double amount;
    private final long time;

    public Transaction(double amount, long time) {
        this.amount = amount;
        this.time = time;
    }

    public double getAmount() {
        return amount;
    }
    public long getTime() {
        return time;
    }
    public Map<Double, Long> serialize() {
        return Map.of(amount, time);
    }
}
