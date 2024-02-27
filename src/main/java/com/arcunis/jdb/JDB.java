package com.arcunis.jdb;

import com.arcunis.jdb.exeptions.InvalidColumnsExeption;
import com.arcunis.jdb.structures.Column;
import com.arcunis.jdb.structures.DataType;
import com.arcunis.jdb.structures.Database;
import com.arcunis.jdb.structures.Table;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class JDB {

    Cache cache;
    File dataFolder;

    public JDB(File dataFolder) throws IOException {
        cache = new Cache(this);
        this.dataFolder = dataFolder;
    }

    public void close() throws IOException {
        cache.save(this);
    }

    public JavaDB getDatabase(String host, String port, String database, String username, String password) throws SQLException, IOException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
        return new JavaDB(this, connection, database);
    }

    public JavaDB getLocalDatabase(String database) throws SQLException, IOException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite://" + database);
        return new JavaDB(this, connection, database);
    }

    public static JavaDB JavaDB;
    public static Database Database;
    public static Table Table;
    public static Column Column;
    public static DataType DataType;
    public static InvalidColumnsExeption InvalidColumnsExeption;

}
