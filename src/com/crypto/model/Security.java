package com.crypto.model;

import com.crypto.enums.SecurityType;
import com.crypto.enums.Ticker;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Objects;

public class Security {
    private final SecurityType securityType;
    private final Ticker mainSymbol;
    private final Ticker callOrPutOrStockOption;
    private BigDecimal callOrPutOptionOrStockPrice;
    private final BigDecimal strikePrice;
    private final LocalDateTime maturity;

    public Security(SecurityType securityType, Ticker mainSymbol, Ticker callOrPutOption, BigDecimal callOrPutOptionOrStockPrice, BigDecimal strikePrice, LocalDateTime maturity) {
        this.securityType = securityType;
        this.mainSymbol = mainSymbol;
        this.callOrPutOrStockOption = callOrPutOption;
        this.callOrPutOptionOrStockPrice = callOrPutOptionOrStockPrice;
        this.strikePrice = strikePrice;
        this.maturity = maturity;
    }

    public SecurityType getSecurityType() {
        return securityType;
    }

    public Ticker getMainSymbol() {
        return mainSymbol;
    }

    public Ticker getCallOrPutOrStockOption() {
        return callOrPutOrStockOption;
    }

    public BigDecimal getCallOrPutOptionOrStockPrice() {
        return callOrPutOptionOrStockPrice;
    }

    public BigDecimal getStrikePrice() {
        return strikePrice;
    }

    public LocalDateTime getMaturity() {
        return maturity;
    }

    public void setCallOrPutOptionOrStockPrice(BigDecimal callOrPutOptionOrStockPrice) {
        this.callOrPutOptionOrStockPrice = callOrPutOptionOrStockPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Security security = (Security) o;
        return mainSymbol == security.mainSymbol && callOrPutOrStockOption == security.callOrPutOrStockOption;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mainSymbol, callOrPutOrStockOption);
    }

    @Override
    public String toString() {
        if(this.mainSymbol==this.callOrPutOrStockOption) {
            return "Security{" +
                    ", callOrPutOrStockOption=" + callOrPutOrStockOption +
                    ", callOrPutOptionOrStockPrice=" + callOrPutOptionOrStockPrice.setScale(2, RoundingMode.HALF_UP) +
                    '}';
        }
        return "";
    }
}
