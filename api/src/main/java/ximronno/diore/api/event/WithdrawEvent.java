package ximronno.diore.api.event;

import ximronno.diore.api.account.Account;

public class WithdrawEvent extends TransactionEvent {
    public WithdrawEvent(Account account, double amount) {
        super(account, amount);
    }

}
