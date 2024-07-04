package ximronno.diore.impl;

import ximronno.api.interfaces.Account;

public record TopBalance(Account account, boolean publicBalance, double balance) {
}
