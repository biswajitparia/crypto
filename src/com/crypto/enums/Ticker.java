package com.crypto.enums;

public enum Ticker {
    AAPL("AAPL"),
    AAPL_OCT_2020_110_C("AAPL-OCT-2020-110-C"),
    AAPL_OCT_2020_110_P("AAPL-OCT-2020-110-P"),
    TELSA("TELSA"),
    TELSA_NOV_2020_400_C("TELSA-NOV-2020-400-C"),
    TELSA_DEC_2020_400_P("TELSA-DEC-2020-400-P");

    private final String name;

    Ticker(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
