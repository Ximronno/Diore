package ximronno.bukkit.storage;

import ximronno.bukkit.account.DioreAccount;
import ximronno.diore.api.DioreAPI;
import ximronno.diore.api.account.Account;
import ximronno.diore.api.account.Transaction;
import ximronno.diore.api.config.SQLConfig;
import ximronno.diore.api.storage.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.logging.Logger;

public class MySQLDataBase implements DataBase {

    private final SQLConfig sqlConfig;

    private final Logger logger;

    private Connection connection;

    private final boolean useLogger;

    public MySQLDataBase(DioreAPI api, Logger logger) {
        this.sqlConfig = api.getMainConfig().getSQLConfig();
        this.logger = logger;
        this.useLogger = api.getMainConfig().useLogger();
    }

    public Connection getConnection() throws SQLException {
        if(connection == null) {
            String host = sqlConfig.getHost();
            String port = sqlConfig.getPort();
            String databaseName = sqlConfig.getDataBase();
            String user = sqlConfig.getUsername();
            String pass = sqlConfig.getPassword();

            String url = String.format("jdbc:mysql://%s:%s/%s", host, port, databaseName);

            connection = DriverManager.getConnection(url, user, pass);
            logger.info("Connected to MySQL with DB name: " + databaseName);
        }
        return connection;
    }

    @Override
    public void createDioreTables() throws SQLException {
        Connection conn = getConnection();

        final Statement statement = conn.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS account_data(uuid varchar(36) primary key, locale text, balance double, privateBalance boolean)");
        statement.execute("CREATE TABLE IF NOT EXISTS account_transactions(transactionID int AUTO_INCREMENT primary key, uuid varchar(36), amount double, time long)");
    }

    @Override
    public void putAccountIntoTable(Account acc) throws SQLException {
        Connection conn = getConnection();

        UUID uuid = acc.getUuid();
        Locale locale = acc.getLocale();
        double balance = acc.getBalance();
        boolean privateBalance = acc.isPrivateBalance();

        final Statement statement = conn.createStatement(
                ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE
        );
        ResultSet set = statement.executeQuery("SELECT * FROM account_data WHERE uuid = '" + uuid.toString() + "'");

        if(set.next()) {

            set.updateString("locale", locale.toString());
            set.updateDouble("balance", balance);
            set.updateBoolean("privateBalance", privateBalance);

            set.updateRow();

        }
        else {
            final PreparedStatement preparedStatement = conn
                    .prepareStatement("INSERT INTO account_data(uuid, locale, balance, privateBalance) VALUES(?,?,?,?)");

            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, locale.toString());
            preparedStatement.setDouble(3, balance);
            preparedStatement.setBoolean(4, privateBalance);

            preparedStatement.execute();
            preparedStatement.close();
        }

        set.close();
        statement.close();
    }

    @Override
    public void addRecentTransaction(UUID uuid, Transaction transaction) throws SQLException{
        Connection conn = getConnection();

        double amount = transaction.amount();
        long time = transaction.time();

        final PreparedStatement preparedStatement = conn
                .prepareStatement("INSERT INTO account_transactions(uuid, amount, time) VALUES(?,?,?)");

        preparedStatement.setString(1, uuid.toString());
        preparedStatement.setDouble(2, amount);
        preparedStatement.setLong(3, time);

        preparedStatement.execute();
        preparedStatement.close();
    }

    @Override
    public Account getAccountFromTable(UUID uuid) throws SQLException {
        Connection conn = getConnection();

        final Statement statement1 = conn.createStatement();
        ResultSet set1 = statement1.executeQuery("SELECT amount, time FROM account_transactions WHERE uuid = '" + uuid + "'");

        List<Transaction> recentTransactions = new ArrayList<>();

        while(set1.next()) {
            double amount = set1.getDouble("amount");
            long time = set1.getLong("time");

            recentTransactions.add(Transaction.of(amount, time));
        }

        set1.close();
        statement1.close();

        final Statement statement = conn.createStatement();
        ResultSet set = statement.executeQuery("SELECT locale, balance, privateBalance FROM account_data WHERE uuid = '" + uuid.toString() + "'");

        if(set.next()) {

            Locale locale = new Locale(set.getString("locale"));
            double balance = set.getDouble("balance");
            boolean privateBalance = set.getBoolean("privateBalance");

            if(useLogger) {
                logger.info("Loaded account using MySQL for: " + uuid);
            }

            return DioreAccount.builder()
                    .setRecentTransactions(recentTransactions)
                    .setUuid(uuid)
                    .setBalance(balance)
                    .setPrivateBalance(privateBalance)
                    .setLocale(locale)
                    .build();
        }

        set.close();
        statement.close();

        return null;
    }

    @Override
    public void initializeDataBase() throws SQLException {
        createDioreTables();
    }

}
