package com.arcunis.jdb;

import com.arcunis.jdb.exeptions.InvalidColumnsExeption;
import com.arcunis.jdb.structures.Column;
import com.arcunis.jdb.structures.Database;
import com.arcunis.jdb.structures.Table;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class JavaDB {

    public Connection connection;
    public String name;
    private Database database;

    public JavaDB(JDB jdb, Connection connection, String database) throws IOException {
        this.connection = connection;
        this.name = database;
        this.database = jdb.cache.databaseMap.get(database);
        if (database == null) this.database = jdb.cache.createDatabase(this);
    }

    public Table getTable(String table) {
        return database.getTable(table);
    }

    public void createTable(String name, Column[] columns, boolean overwrite) throws InvalidColumnsExeption, SQLException {
        database.createTable(new Table(database, name, columns), overwrite);
    }
    public void createTable(Table table, boolean overwrite) throws SQLException {
        database.createTable(table, overwrite);
    }

    public void deleteTable(String table) throws SQLException {
        database.deleteTable(table);
    }

}
