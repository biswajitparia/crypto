package com.crypto.data;

import com.crypto.enums.SecurityType;
import com.crypto.enums.Ticker;
import com.crypto.model.Security;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public enum SecurityMockForDatabase {
        /*
         2. Get the security definitions from an embedded database.
        - Design a schema with small embedded database (H2 or SQLite) to store the security definitions (three supported types: Stock, Call, Put)
        - Each security in this database will have a ticker (identifier) and will have some static (e.g. strike, maturity)
        */
    INSTANCE;
    private final List<Security> securities = new ArrayList<>();
    private final ReentrantLock reentrantLock = new ReentrantLock();

    public List<Security> getSecurities() {
        try {
            reentrantLock.lock();
            if (securities.isEmpty()) {
                securities.addAll(Arrays.asList(
                        new Security(SecurityType.Stock, Ticker.AAPL, Ticker.AAPL, new BigDecimal("100.00"), new BigDecimal("100.00"), LocalDateTime.now().plusDays(30)),
                        new Security(SecurityType.Call, Ticker.AAPL, Ticker.AAPL_OCT_2020_110_C, new BigDecimal("5.00"), new BigDecimal("5.00"), LocalDateTime.now().plusDays(30)),
                        new Security(SecurityType.Put, Ticker.AAPL, Ticker.AAPL_OCT_2020_110_P, new BigDecimal("1.20"), new BigDecimal("1.20"), LocalDateTime.now().plusDays(30)),
                        new Security(SecurityType.Stock, Ticker.TELSA, Ticker.TELSA, new BigDecimal("400.00"), new BigDecimal("400.00"), LocalDateTime.now().plusDays(30)),
                        new Security(SecurityType.Call, Ticker.TELSA, Ticker.TELSA_NOV_2020_400_C, new BigDecimal("4.01"), new BigDecimal("4.01"), LocalDateTime.now().plusDays(30)),
                        new Security(SecurityType.Put, Ticker.TELSA, Ticker.TELSA_DEC_2020_400_P, new BigDecimal("5.30"), new BigDecimal("5.30"), LocalDateTime.now().plusDays(30))
                ));
            }
            return securities;
        } finally {
            reentrantLock.unlock();
        }
    }

    public void setSecurities(List<Security> updatedSecurities) {
        try {
            reentrantLock.lock();
            securities.clear();
            securities.addAll(updatedSecurities);
        } finally {
            reentrantLock.unlock();
        }
    }

}
