package ximronno.bukkit.account;

import org.bukkit.Bukkit;
import ximronno.diore.api.DioreAPI;
import ximronno.diore.api.SortingVariant;
import ximronno.diore.api.account.Account;
import ximronno.diore.api.account.AccountLeaderBoard;
import ximronno.diore.api.account.managment.AccountManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DioreAccountLeaderBoard implements AccountLeaderBoard {

    private final AccountManager manager;

    public DioreAccountLeaderBoard(DioreAPI api) {
        this.manager = api.getAccountManager();
    }

    @Override
    public Account fromPlace(int place, SortingVariant sortVariant) {
        place--;

        List<Account> accounts = getSortedLeaderBoard(sortVariant);
        if(accounts == null || place < 0 || place >= accounts.size()) return null;

        return accounts.get(place);
    }

    @Override
    public int getPlace(Account acc, SortingVariant sortVariant) {
        List<Account> accounts = getSortedLeaderBoard(sortVariant);

        if(accounts == null) return -1;

        int index = accounts.indexOf(acc);
        if(index == -1) {
            return index;
        }
        else {
            return ++index;
        }
    }

    @Override
    public List<Account> getSortedLeaderBoard(SortingVariant sortVariant) {
        List<Account> accounts;

        switch (sortVariant) {
            case SORT_OFFLINE_PLAYERS -> accounts = getOnlineLeaders();
            case SORT_ONLINE_PLAYERS -> accounts = getOfflineLeaders();
            default -> accounts = getLeaderBoard();
        }

        return accounts;
    }

    @Override
    public List<Account> getLeaderBoard() {
        List<Account> accounts = manager.getAccountsList();
        if(accounts.isEmpty()) return null;

        List<Account> accountsCopy = new ArrayList<>(accounts);

        accountsCopy.removeIf(Account::isPrivateBalance);
        accountsCopy.sort(Comparator.comparing(Account::getBalance).reversed());

        return Collections.unmodifiableList(accountsCopy);
    }

    @Override
    public List<Account> getOnlineLeaders() {
        List<Account> accounts = manager.getAccountsList();
        if(accounts.isEmpty()) return null;

        List<Account> accountsCopy = new ArrayList<>(accounts);

        accountsCopy.removeIf(Account::isPrivateBalance);
        accountsCopy.removeIf(account -> !Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(account.getUuid())));
        accountsCopy.sort(Comparator.comparing(Account::getBalance).reversed());

        return Collections.unmodifiableList(accountsCopy);
    }

    @Override
    public List<Account> getOfflineLeaders() {
        List<Account> accounts = manager.getAccountsList();
        if(accounts.isEmpty()) return null;

        List<Account> accountsCopy = new ArrayList<>(accounts);

        accountsCopy.removeIf(Account::isPrivateBalance);
        accountsCopy.removeIf(account -> Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(account.getUuid())));
        accountsCopy.sort(Comparator.comparing(Account::getBalance).reversed());

        return Collections.unmodifiableList(accountsCopy);
    }

}
