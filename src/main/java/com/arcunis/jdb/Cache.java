package com.arcunis.jdb;

import com.arcunis.jdb.structures.Database;
import com.google.gson.Gson;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Cache {

    public Map<String, Database> databaseMap = new HashMap<>();

    public Cache(JDB jdb) throws IOException {
        load(jdb);
    }

    public Database createDatabase(JavaDB javaDB) {
        Database database = new Database(javaDB);
        this.databaseMap.put(javaDB.name, database);
        return database;
    }

    public void save(JDB jdb) throws IOException {
        File file = getCacheFile(jdb);
        Writer writer = new FileWriter(file, false);
        new Gson().toJson(this, writer);
    }

    public Map<String, Database> load(JDB jdb) throws IOException {
        File file = getCacheFile(jdb);
        Reader reader = new FileReader(file);
        databaseMap = new Gson().fromJson(reader, Cache.class).databaseMap;
        return databaseMap;
    }

    private File getCacheFile(JDB jdb) throws IOException {
        File file = new File(jdb.dataFolder, "cache.json");
        if (!file.exists()) {
            file.createNewFile();
            Writer writer = new FileWriter(file, false);
            new Gson().toJson(this, writer);
            writer.flush();
            writer.close();
        }
        return file;
    }

}