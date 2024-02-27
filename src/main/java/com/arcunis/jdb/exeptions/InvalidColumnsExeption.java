package com.arcunis.jdb.exeptions;

public class InvalidColumnsExeption extends Exception {
    public InvalidColumnsExeption(String errorMessage) {
        super(errorMessage);
    }
}
