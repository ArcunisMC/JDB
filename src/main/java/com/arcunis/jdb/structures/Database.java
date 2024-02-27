package com.arcunis.jdb.structures;

import com.arcunis.jdb.JavaDB;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Database {

    public JavaDB jdb;

    public Map<String, Table> tableMap = new HashMap<>();

    public Database(JavaDB jdb) {
        this.jdb = jdb;
    }

    public Table getTable(String name) {
        return tableMap.get(name);
    }

    public void createTable(Table table, boolean overwrite) throws SQLException {
        ArrayList<String> columns = new ArrayList<>();
        for (Column column : table.columnMap.values()) columns.add(column.getString());
        String str = "CREATE TABLE " + table.name + " IF NOT EXISTS (" + String.join(", ", columns) + ")";
        if (overwrite) str = str.replace("IF NOT EXISTS ", "");
        PreparedStatement statement =  jdb.connection.prepareStatement(str);
        statement.execute();
        tableMap.put(table.name, table);
    }

    public void deleteTable(String table) throws SQLException {
        PreparedStatement statement =  jdb.connection.prepareStatement("DROP TABLE " + table);
        statement.execute();
        tableMap.remove(table);
    }

}
