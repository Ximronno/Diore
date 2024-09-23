package ximronno.bukkit.account.managment;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import ximronno.bukkit.Permissions;
import ximronno.bukkit.message.type.CommandMessagesPaths;
import ximronno.bukkit.message.type.ErrorMessagesPaths;
import ximronno.bukkit.message.type.LanguagePath;
import ximronno.diore.api.DioreAPI;
import ximronno.diore.api.account.Account;
import ximronno.diore.api.account.AccountResponse;
import ximronno.diore.api.account.Transaction;
import ximronno.diore.api.account.managment.AccountController;
import ximronno.diore.api.event.DepositEvent;
import ximronno.diore.api.event.TransferEvent;
import ximronno.diore.api.event.WithdrawEvent;
import ximronno.diore.api.message.MessageManager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Map;

public class DioreAccountController implements AccountController {

    private final DioreAPI api;

    private final JavaPlugin plugin;

    private final MessageManager messageManager;

    public DioreAccountController(DioreAPI api, JavaPlugin plugin) {
        this.api = api;
        this.plugin = plugin;
        this.messageManager = api.getMessageManager();
    }

    @Override
    public boolean setLocale(Player p, Account acc, Locale locale) {
        if(!p.hasPermission(Permissions.BALANCE_LOCALE.getPermission())) return false;
        if(!messageManager.getMessageProvider().getProvidedLanguages().contains(locale)) {
            p.sendMessage(messageManager.getMessage(ErrorMessagesPaths.LOCALE_NOT_FOUND, acc.getLocale(), true));
            return false;
        }

        acc.setLocale(locale);
        p.sendMessage(messageManager.getMessage(CommandMessagesPaths.BALANCE_LOCALE_SET, locale, true, Map.of(
                "{locale}", messageManager.getMessage(LanguagePath.NAME, locale, true)
        )));

        return true;
    }

    @Override
    public boolean setPrivateBalance(Player p, Account acc, Locale locale, boolean privateToSet) {
        if(!p.hasPermission(Permissions.BALANCE_PRIVACY.getPermission())) return false;
        acc.setPrivateBalance(privateToSet);
        p.sendMessage(messageManager.getMessage(CommandMessagesPaths.BALANCE_PRIVATE_BALANCE_SET, locale, true, Map.of(
                "{balance_status}",api.getAccountInfoFormatter().getFormattedBalanceStatus(acc, locale)
        )));
        return true;
    }

    @Override
    public boolean withdraw(Player p, Account acc, Locale locale, double amount) {
        if(!p.hasPermission(Permissions.BALANCE_WITHDRAW.getPermission())) return false;
        if(amount <= 0) {
            p.sendMessage(messageManager.getMessage(CommandMessagesPaths.BALANCE_INVALID_AMOUNT, locale, true));
            return false;
        }
        BigDecimal bd = new BigDecimal(Double.toString(amount));
        bd = bd.setScale(1, RoundingMode.FLOOR);

        amount = bd.doubleValue();

        AccountResponse response;

        if (!acc.canWithdraw(amount)) {
            response = AccountResponse.NOT_ENOUGH_FUNDS;
        } else {
            WithdrawEvent event = new WithdrawEvent(acc, amount);
            Bukkit.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                response = event.getResponse();
            } else {
                response = AccountResponse.SUCCESS;
            }
        }
        return switch(response) {
            case NOT_ENOUGH_FUNDS -> {
                p.sendMessage(messageManager.getMessage(CommandMessagesPaths.BALANCE_WITHDRAW_INSUFFICIENT, locale, true));
                yield false;
            }
            case SUCCESS -> {
                acc.withdraw(amount);
                Transaction transaction = Transaction.of(-amount, System.currentTimeMillis());

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        try {
                            api.getDataBase().addRecentTransaction(acc.getUuid(), transaction);
                        } catch (SQLException ignored) {
                        }
                    }
                }.runTaskAsynchronously(plugin);

                acc.addRecentTransaction(transaction);
                p.sendMessage(messageManager.getMessage(CommandMessagesPaths.BALANCE_WITHDRAW_SUCCESS, locale, true,
                        Map.of("{amount}", api.getAccountInfoFormatter().getFormattedAmount(amount, locale))));
                yield true;
            }
        };
    }

    @Override
    public boolean deposit(Player p, Account acc, Locale locale, double amount) {
        if(!p.hasPermission(Permissions.BALANCE_DEPOSIT.getPermission())) return false;
        if (amount <= 0) {
            p.sendMessage(messageManager.getMessage(CommandMessagesPaths.BALANCE_INVALID_AMOUNT, locale, true));
            return false;
        }
        BigDecimal bd = new BigDecimal(Double.toString(amount));
        bd = bd.setScale(1, RoundingMode.FLOOR);

        amount = bd.doubleValue();

        AccountResponse response;

        DepositEvent event = new DepositEvent(acc, amount);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if(event.isCancelled()) {
            response = event.getResponse();
        }
        else  {
            response = AccountResponse.SUCCESS;
        }
        return switch(response) {
            case NOT_ENOUGH_FUNDS -> {
                p.sendMessage(messageManager.getMessage(CommandMessagesPaths.BALANCE_DEPOSIT_INSUFFICIENT, locale, true));
                yield false;
            }
            case SUCCESS -> {
                acc.deposit(amount);
                Transaction transaction = Transaction.of(amount, System.currentTimeMillis());

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        try {
                            api.getDataBase().addRecentTransaction(acc.getUuid(), transaction);
                        } catch (SQLException ignored) {
                        }
                    }
                }.runTaskAsynchronously(plugin);

                acc.addRecentTransaction(transaction);
                p.sendMessage(messageManager.getMessage(CommandMessagesPaths.BALANCE_DEPOSIT_SUCCESS, locale, true,
                        Map.of("{amount}", api.getAccountInfoFormatter().getFormattedAmount(amount, locale))));
                yield true;
            }
        };
    }

    @Override
    public boolean transfer(Player sender, OfflinePlayer target, Account from, Account to, Locale senderLocale, double amount) {
        if(!sender.hasPermission(Permissions.BALANCE_TRANSFER.getPermission())) return false;
        if(amount <= 0) {
            sender.sendMessage(messageManager.getMessage(CommandMessagesPaths.BALANCE_INVALID_AMOUNT, senderLocale, true));
            return false;
        }
        if(to == null) {
            sender.sendMessage(api.getMessageManager().getDefaultMessage(ErrorMessagesPaths.TARGET_NO_ACCOUNT, true));
            return true;
        }
        AccountResponse response;

        TransferEvent event = new TransferEvent(from, to, amount);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if(!from.canTransfer(amount)) {
            response = AccountResponse.NOT_ENOUGH_FUNDS;
        }
        else if(event.isCancelled()) {
            response = event.getResponse();
        }
        else {
            response = AccountResponse.SUCCESS;
        }
        return switch (response) {
            case NOT_ENOUGH_FUNDS -> {
                sender.sendMessage(messageManager.getMessage(CommandMessagesPaths.BALANCE_TRANSFER_INSUFFICIENT, senderLocale, true));
                yield false;
            }
            case SUCCESS -> {
                from.transfer(to, amount);
                Transaction fromTransaction = Transaction.of(-amount, System.currentTimeMillis());
                Transaction toTransaction = Transaction.of(amount, System.currentTimeMillis());

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        try {
                            api.getDataBase().addRecentTransaction(from.getUuid(), fromTransaction);
                            api.getDataBase().addRecentTransaction(to.getUuid(), toTransaction);
                        } catch (SQLException ignored) {
                        }
                    }
                }.runTaskAsynchronously(plugin);

                from.addRecentTransaction(fromTransaction);
                to.addRecentTransaction(toTransaction);
                sender.sendMessage(messageManager.getMessage(CommandMessagesPaths.BALANCE_TRANSFER_SUCCESS, senderLocale, true,
                        Map.of("{amount}",  api.getAccountInfoFormatter().getFormattedAmount(amount, senderLocale),
                                "{target}", target.getName())));

                if(target.isOnline()) {
                    Player onlineTarget = target.getPlayer();
                    assert onlineTarget != null;

                    Locale targetLocale = to.getLocale();
                    onlineTarget.sendMessage(messageManager.getMessage(CommandMessagesPaths.BALANCE_TRANSFER_RECEIVED, targetLocale, true,
                            Map.of("{amount}", api.getAccountInfoFormatter().getFormattedAmount(amount, targetLocale),
                                    "{from}", sender.getName())));
                }
                yield true;
            }
        };
    }
}
