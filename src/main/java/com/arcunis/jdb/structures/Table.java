package com.arcunis.jdb.structures;

import com.arcunis.jdb.exeptions.InvalidColumnsExeption;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Table {

    public Database database;
    public String name;
    public Map<String, Column> columnMap = new HashMap<>();

    public Table(Database database, String name, Column[] columns) throws InvalidColumnsExeption {
        this.database = database;
        this.name = name;
        if (validate(columns)) throw new InvalidColumnsExeption("cannot declare multiple primary values in table '" + name + "'");
        for (Column column : columns) columnMap.put(column.name, column);
    }

    private boolean validate(Column[] columns) {
        int primaryCount = 0;
        for (Column column : columns) {
            if (column.primary) primaryCount ++;
        }
        return primaryCount > 1;
    }

    public void insert(String[] keys, Object[] values) throws SQLException {
        String query = "INSERT INTO " + name + " (" + String.join(", ", keys) + ") VALUES (" + String.join(", ", values.toString()) + ")";
        database.jdb.connection.prepareStatement(query).execute();
    }

    public void update(String[] keys, Object[] values, String where) throws SQLException {
        ArrayList<String> set = new ArrayList<>();
        for (String key : keys) {
            int index = Arrays.asList(keys).indexOf(key);
            set.add(key + " = " + values[index]);
        }
        String query = "UPDATE " + name + " SET " + String.join(", ", set) + " WHERE " + where;
        database.jdb.connection.prepareStatement(query).execute();
    }

    public Map<String, Object> select(String[] keys, Integer limit, String where) throws SQLException {
        Map<String, Object> response = new HashMap<>();
        String query = "SELECT (" + String.join(", ", keys) + ") FROM " + name;
        if (where != null) query += " WHERE " + where;
        if (limit != null) query += " LIMIT " + limit;
        PreparedStatement statement = database.jdb.connection.prepareStatement(query);
        ResultSet result = statement.executeQuery();
        for (String key : keys) {
            DataType type = columnMap.get(key).type;
            if (type == DataType.text) response.put(key, result.getString(Arrays.asList(keys).indexOf(key)));
            else if (type == DataType.integer) response.put(key, result.getInt(Arrays.asList(keys).indexOf(key)));
            else response.put(key, result.getBlob(Arrays.asList(keys).indexOf(key)));
        }
        return response;
    }

    public Map<String, Object> selectAll(Integer limit, String where) throws SQLException {
        String[] keys = columnMap.keySet().toArray(new String[columnMap.size()]);
        Map<String, Object> response = new HashMap<>();
        String query = "SELECT (" + String.join(", ", keys) + ") FROM " + name;
        if (where != null) query += " WHERE " + where;
        if (limit != null) query += " LIMIT " + limit;
        PreparedStatement statement = database.jdb.connection.prepareStatement(query);
        ResultSet result = statement.executeQuery();
        for (String key : keys) {
            DataType type = columnMap.get(key).type;
            if (type == DataType.text) response.put(key, result.getString(Arrays.asList(keys).indexOf(key)));
            else if (type == DataType.integer) response.put(key, result.getInt(Arrays.asList(keys).indexOf(key)));
            else response.put(key, result.getBlob(Arrays.asList(keys).indexOf(key)));
        }
        return response;
    }

    public void remove(String where) throws SQLException {
        database.jdb.connection.prepareStatement("DELETE FROM " + name + " WHERE " + where).execute();
    }

}
