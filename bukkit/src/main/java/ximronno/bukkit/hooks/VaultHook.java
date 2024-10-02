package ximronno.bukkit.hooks;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import ximronno.diore.api.DioreAPI;
import ximronno.diore.api.account.Account;
import ximronno.diore.api.account.Transaction;
import ximronno.diore.api.account.managment.AccountManager;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

public class VaultHook implements Economy {

    private final DioreAPI api;

    private final AccountManager accManager;

    public VaultHook(DioreAPI api, JavaPlugin plugin) {
        this.api = api;
        this.accManager = api.getAccountManager();
        Bukkit.getServicesManager().register(Economy.class, this, plugin , ServicePriority.Highest);
        plugin.getLogger().log(Level.INFO,"Hooked into Vault");
    }

    public void unregister() {
        Bukkit.getServer().getServicesManager().unregister(Economy.class, this);
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "Diore";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 1;
    }

    @Override
    public String format(double amount) {
        return api.getAccountInfoFormatter().getFormattedAmount(amount);
    }

    @Override
    public String currencyNamePlural() {
        return "ore";
    }

    @Override
    public String currencyNameSingular() {
        return "ores";
    }

    @Override
    public boolean hasAccount(String playerName) {
        return hasAccount(Bukkit.getPlayer(playerName));
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return accManager.hasAccount(player.getUniqueId());
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return hasAccount(Bukkit.getPlayer(playerName));
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return hasAccount(player);
    }

    @Override
    public double getBalance(String playerName) {
        return getBalance(Bukkit.getPlayer(playerName));
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return api.getAccount(player.getUniqueId()).getBalance();
    }

    @Override
    public double getBalance(String playerName, String world) {
        return getBalance(Bukkit.getPlayer(playerName));
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        return getBalance(player);
    }

    @Override
    public boolean has(String playerName, double amount) {
        return has(Bukkit.getPlayer(playerName), amount);
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return api.getAccount(player.getUniqueId()).getBalance() >= amount;
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return has(Bukkit.getPlayer(playerName), amount);
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return has(player, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        return withdrawPlayer(Bukkit.getPlayer(playerName), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        Account acc = accManager.getAccount(player.getUniqueId());
        if(acc == null) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "No account found");
        }
        if(acc.canWithdraw(amount)) {
            acc.withdraw(amount);
            acc.addRecentTransaction(Transaction.valueOf(amount, System.currentTimeMillis(), Transaction.TransactionType.VAULT_WITHDRAW));
            return new EconomyResponse(amount, acc.getBalance() - amount, EconomyResponse.ResponseType.SUCCESS, null);
        }
        return new EconomyResponse(amount, acc.getBalance(), EconomyResponse.ResponseType.FAILURE, null);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return withdrawPlayer(Bukkit.getPlayer(playerName), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        return withdrawPlayer(player, amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        return depositPlayer(Bukkit.getPlayer(playerName), amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        Account acc = accManager.getAccount(player.getUniqueId());
        if(acc == null) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "No account found");
        }
        acc.deposit(amount);
        acc.addRecentTransaction(Transaction.valueOf(amount, System.currentTimeMillis(), Transaction.TransactionType.VAULT_DEPOSIT));
        return new EconomyResponse(amount, acc.getBalance() + amount, EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return depositPlayer(Bukkit.getPlayer(playerName), amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        return depositPlayer(player, amount);
    }

    @Override
    public EconomyResponse createBank(String name, String player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public List<String> getBanks() {
        return Collections.emptyList();
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        return createPlayerAccount(Bukkit.getPlayer(playerName));
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        api.getAccountManager().createNewAccount(player.getUniqueId());
        return true;
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return createPlayerAccount(Bukkit.getPlayer(playerName));
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return createPlayerAccount(player);
    }
}
