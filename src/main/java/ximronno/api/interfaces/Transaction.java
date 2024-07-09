package ximronno.api.interfaces;

import java.util.Map;

public record Transaction(double amount, long time) {

    public Map<Double, Long> serialize() {
        return Map.of(amount, time);
    }
}
