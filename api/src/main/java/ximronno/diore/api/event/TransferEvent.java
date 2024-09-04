package ximronno.diore.api.event;

import ximronno.diore.api.account.Account;

public class TransferEvent extends TransactionEvent{
    private final Account to;

    public TransferEvent(Account account, Account to, double amount) {
        super(account, amount);
        this.to = to;
    }

    public Account getTo() {
        return to;
    }

}
