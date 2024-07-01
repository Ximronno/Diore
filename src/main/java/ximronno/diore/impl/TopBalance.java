package ximronno.diore.impl;

import ximronno.diore.api.interfaces.Account;

public record TopBalance(Account account, boolean publicBalance, double balance) {
}
