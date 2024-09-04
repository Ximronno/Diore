package ximronno.diore.api.account;

import java.util.Map;

public record Transaction(double amount, long time) {

    public Map<Double, Long> serialize() {
        return Map.of(amount, time);
    }

    public static Transaction of(double amount, long time) {
        return new Transaction(amount, time);
    }

}
