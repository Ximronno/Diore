package ximronno.diore.model;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import ximronno.api.interfaces.Account;
import ximronno.api.interfaces.Language;
import ximronno.api.interfaces.Transaction;
import ximronno.diore.Diore;
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
    private final DioreConfigManager configManager;
    public AccountManager(Diore plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();

        if(plugin.getConfig().getBoolean("auto-save")){
            new BukkitRunnable() {
                @Override
                public void run() {

                    for(Account account : getAccounts()) {
                        saveAccountData(account);
                    }
                    plugin.getLogger().info("Successfully saved account data for " + getAccounts().size() + " accounts.");
                }
            }.runTaskTimer(plugin, 0, (plugin.getConfig().getLong("auto-save-interval") * 60) * 20);
        }



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

        List<Transaction> list = new ArrayList<>();

        cfg.getMapList("transactions").forEach(m -> m.forEach((k, v) -> list.add(new Transaction(Double.parseDouble(k.toString()), Long.parseLong(v.toString())))));

        UUID id = player.getUniqueId();
        String name = player.getName();
        double balance = cfg.getDouble("balance");
        String languageRaw = cfg.getString("language");
        boolean publicBalance = cfg.getBoolean("public_balance");

        Language language = null;
        if(languageRaw != null) {
            try {
                language = Languages.valueOf(languageRaw);
            } catch (IllegalArgumentException e) {
                language = Languages.ENGLISH;
            }
        }

        createAccount(id, name, balance, language, publicBalance, list);

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
    public void createAccount(UUID id, String name, double balance, Language language, boolean publicBalance) {
        addAccount(new FundAccount(id, name, balance, language, publicBalance));
    }
    public void createAccount(UUID id, String name, double balance, Language language, boolean publicBalance, List<Transaction> transactions) {
        addAccount(new FundAccount(id, name, balance, language, publicBalance, transactions));
    }


    public boolean saveAccountData(UUID id) {

        Account account = getAccount(id).orElse(null);
        if(account == null) return false;

        return saveAccountData(account);

    }
    public boolean saveAccountData(Account acc) {

        String path = plugin.getDataFolder().getAbsolutePath() + "/data/" + acc.getOwner().toString() + ".yml";
        File data = new File(path);
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(data);

        cfg.set("uuid", acc.getOwner().toString());
        cfg.set("name", acc.getName());
        cfg.set("balance", acc.getBalance());
        cfg.set("language", acc.getLanguage().toString());
        cfg.set("public_balance", acc.isPublicBalance());

        List<Map<?,?>> mapList = new ArrayList<>();

        acc.getTransactions().forEach(t -> mapList.add(t.serialize()));

        cfg.set("transactions", mapList);

        try {
            cfg.save(data);
            return true;
        } catch (Exception e) {
            plugin.getLogger().severe("§cFailed to save account data for " + acc.getOwner());
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
