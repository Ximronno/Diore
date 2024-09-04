package ximronno.diore.api.event;

import ximronno.diore.api.account.Account;

public class DepositEvent extends TransactionEvent{

    public DepositEvent(Account account, double amount) {
        super(account, amount);
    }

}
