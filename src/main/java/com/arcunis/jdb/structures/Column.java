package com.arcunis.jdb.structures;

public class Column {

    public String name;
    public DataType type;
    public Object defaultValue;
    public boolean notNull;
    public boolean primary;

    public Column(String name, DataType type, boolean primary, boolean notNull, Object defaultValue) {
        this.name = name;
        this.type = type;
        this.defaultValue = defaultValue;
        this.notNull = notNull;
        this.primary = primary;
    }

    public String getString() {
        String str = name;
        str += type.toString().toUpperCase();
        if (primary) str += " PRIMARY KEY";
        if (notNull) str += " NOT NULL";
        if (defaultValue != null) str += " DEFAULT " + defaultValue;
        return str;
    }
}
