package ximronno.diore.model;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import ximronno.diore.Diore;
import ximronno.api.interfaces.Account;
import ximronno.diore.impl.FundAccount;
import ximronno.diore.impl.Languages;
import ximronno.diore.impl.TopBalance;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

public class AccountManager {

    private final List<Account> accounts = Collections.synchronizedList(new ArrayList<>());
    private final Diore plugin;
    private final ConfigManager configManager;
    public AccountManager(Diore plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
    }

    /*
        Account management
         */
    public void addAccount(Account account) {
        synchronized (this.accounts) {
            if(this.accounts.contains(account)) return;
            this.accounts.add(account);
        }
    }
    public void removeAccount(UUID id) {
        synchronized (this.accounts) {
            this.accounts.removeIf(account -> account.getOwner().equals(id));
        }
    }
    public Optional<Account> getAccount(UUID id) {
        synchronized (this.accounts) {
            return this.accounts.stream().filter(account -> account.getOwner().equals(id)).findFirst();
        }
    }
    public Optional<Double> getBalance(UUID id) {
        Account account = getAccount(id).orElse(null);
        if(account == null) return Optional.empty();
        return Optional.of(account.getBalance());
    }
    public List<Account> getAccounts() {
        synchronized (this.accounts) {
            return Collections.unmodifiableList(this.accounts);
        }
    }
    public List<TopBalance> getTopBalances() {
        synchronized (this.accounts) {

            final List<TopBalance> topBalances = new ArrayList<>();

            this.accounts.forEach(account -> {
                double balance = account.isPublicBalance() ? account.getBalance() : 0.0;
                topBalances.add(new TopBalance(account, account.isPublicBalance(), balance));
            });

            topBalances.sort(Comparator.comparingDouble(TopBalance::balance).reversed());

            return topBalances;

        }
    }

    /*
    Data management
     */
    public void getOrCreateAccount(OfflinePlayer player) {

        if(getAccount(player.getUniqueId()).isPresent()) return;

        String path = plugin.getDataFolder().getAbsolutePath() + "/data/" + player.getUniqueId() + ".yml";
        File data = new File(path);

        if(!data.exists()) createAccount(player);

        FileConfiguration cfg = YamlConfiguration.loadConfiguration(data);

        createAccount(player.getUniqueId(), player.getName(), cfg.getDouble("balance"), Languages.valueOf(cfg.getString("language")), cfg.getBoolean("public_balance"));

    }
    public void getOrCreateAccount(UUID id) {
        getOrCreateAccount(Bukkit.getOfflinePlayer(id));
    }
    public void createAccount(OfflinePlayer player) {
        createAccount(player.getUniqueId(), player.getName(), 0D);
    }
    public void createAccount(OfflinePlayer player, double balance) {
        createAccount(player.getUniqueId(), player.getName(), balance);
    }
    public void createAccount(UUID id, String name, double balance) {
        addAccount(new FundAccount(id, name, balance));
    }
    public void createAccount(UUID id, String name, double balance, Languages language, boolean publicBalance) {
        addAccount(new FundAccount(id, name, balance, language, publicBalance));
    }


    public boolean saveAccountData(UUID id) {

        String path = plugin.getDataFolder().getAbsolutePath() + "/data/" + id.toString() + ".yml";
        File data = new File(path);
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(data);

        Account account = getAccount(id).orElse(null);
        if(account == null) return false;

        cfg.set("uuid", account.getOwner().toString());
        cfg.set("name", account.getName());
        cfg.set("balance", account.getBalance());
        cfg.set("language", account.getLanguage().toString());
        cfg.set("public_balance", account.isPublicBalance());

        try {
            cfg.save(data);
            return true;
        } catch (Exception e) {
            plugin.getLogger().severe("§cFailed to save account data for " + id);
            return false;
        }

    }
    /*
    Other
     */
    public String formatBalance(double balance) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');

        String pattern = "#,##0.0";
        DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);

        BigDecimal bd = new BigDecimal(Double.toString(balance));
        bd = bd.setScale(1, RoundingMode.FLOOR);

        balance = bd.doubleValue();

        return configManager.getFormattedString("currency-format")
                .replace("<amount>", decimalFormat.format(balance));
    }

}
