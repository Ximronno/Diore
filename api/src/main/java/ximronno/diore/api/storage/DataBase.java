package ximronno.diore.api.storage;

import ximronno.diore.api.account.Account;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

public interface DataBase {

    Connection getConnection() throws SQLException;

    void createDioreTables() throws SQLException;

    void initializeDataBase() throws SQLException;

    void putAccountIntoTable(Account acc) throws SQLException;

    Account getAccountFromTable(UUID uuid) throws SQLException;
}
