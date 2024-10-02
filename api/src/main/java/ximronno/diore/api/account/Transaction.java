package ximronno.diore.api.account;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public record Transaction(double amount, long time,
                          TransactionType type) implements ConfigurationSerializable {

    public enum TransactionType {

        DEPOSIT(1),
        WITHDRAW(2),
        TRANSFER(3),
        VAULT_DEPOSIT(5),
        VAULT_WITHDRAW(6),
        STOLEN(7),
        UNKNOWN(-1),
        ;

        private final int id;

        TransactionType(int id) {
            this.id = id;
        }

        public static TransactionType fromId(int id) {
            for (TransactionType type : values()) {
                if (type.getId() == id) {
                    return type;
                }
            }
            return UNKNOWN;
        }

        public int getId() {
            return id;
        }
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        return Map.of(
                "amount", amount,
                "time", time,
                "type", type.getId()
        );
    }

    public static Transaction valueOf(double amount, long time) {
        return Transaction.valueOf(amount, time, TransactionType.UNKNOWN);
    }

    public static Transaction valueOf(double amount, long time, TransactionType type) {
        return new Transaction(amount, time, type);
    }

    public static Transaction valueOf(double amount, long time, int typeId) {
        return Transaction.valueOf(amount, time, TransactionType.fromId(typeId));
    }

    public static Transaction valueOf(Map<?, ?> serialized) {
        Object amount = serialized.get("amount");
        if (amount == null) amount = 0;
        Object time = serialized.get("time");
        if (time == null) time = 0L;
        Object type = serialized.get("type");
        if (type == null) type = -1;

        return Transaction.valueOf((double) amount, (long) time, TransactionType.fromId((int) type));
    }
}
