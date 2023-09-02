package com.crypto.model;

import com.crypto.enums.Ticker;

public final class Position {
    private final Ticker ticker;
    private final Integer positionSize;

    public Position(Ticker ticker, Integer positionSize) {
        this.ticker = ticker;
        this.positionSize = positionSize;
    }

    public Ticker getTicker() {
        return ticker;
    }

    public Integer getPositionSize() {
        return positionSize;
    }
}
