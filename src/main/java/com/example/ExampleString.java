package com.example;

public class ExampleString {

    private boolean oracleFlag;

    public void badFunction(String input) {
        oracleFlag = true;
    }

    public boolean isOracleLogin() {
        return oracleFlag;
    }

}
