package ximronno.diore.api.storage;

import ximronno.diore.api.account.Account;
import ximronno.diore.api.account.Transaction;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

public interface DataBase {

    Connection getConnection() throws SQLException;

    void createDioreTables() throws SQLException;

    void initializeDataBase() throws SQLException;

    void putAccountIntoTable(Account acc) throws SQLException, IOException;

    Account getAccountFromTable(UUID uuid) throws SQLException;

    void addRecentTransaction(UUID uuid , Transaction transaction) throws SQLException;
}
