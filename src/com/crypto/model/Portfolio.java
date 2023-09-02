package com.crypto.model;

import com.crypto.enums.Ticker;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public final class Portfolio {
    private final List<String> lines;

    public Portfolio(List<String> lines) {
        this.lines = lines;
    }

    public List<String> getLines() {
        return lines;
    }

}
