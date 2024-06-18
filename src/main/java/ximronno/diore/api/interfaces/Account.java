package ximronno.diore.api.interfaces;

import java.util.UUID;

public interface Account {

    UUID getOwner();
    void setOwner(UUID owner);
    String getName();
    void setName(String name);
    double getBalance();
    void setBalance(double balance);

    boolean transfer(Account account, double amount);
    boolean withdraw(double amount);
    boolean deposit(double amount);
    void receive(double amount);


}
