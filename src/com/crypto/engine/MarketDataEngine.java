package com.crypto.engine;

import com.crypto.data.SecurityMockForDatabase;
import com.crypto.model.Security;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class MarketDataEngine implements Runnable {
    private final BlockingQueue<List<Security>> marketDataQueue;

    public MarketDataEngine(BlockingQueue<List<Security>> marketDataQueue) {
        this.marketDataQueue = marketDataQueue;
    }

    /*
     3. Implement a mock market data provider that publishes stock prices.
    - The stock prices move according to either • Random pricing
    • or Preferable a discrete time geometric Brownian motion (see appendix) randomly between 0.5 – 2 seconds.

     4. Calculate the real time option price with the underlying price
     */

    @Override
    public void run() {
        try {
            System.out.println(LocalDateTime.now()+" MarketDataEngine started");

            while (!Thread.interrupted()) {
                List<Security> updatedSecurities = updatePrice(SecurityMockForDatabase.INSTANCE.getSecurities());
                //System.out.println(LocalDateTime.now() + " MarketDataEngine: securities SENDING: " + updatedSecurities);
                marketDataQueue.put(updatedSecurities);
                TimeUnit.SECONDS.sleep(5);
            }
        } catch (Exception e) {
            System.out.println(LocalDateTime.now()+" MarketDataEngine stopped");
        }
    }

    private List<Security> updatePrice(List<Security> inputSecurities) {
        List<Security> outputSecurities = new ArrayList<>();
        List<Security> stockSysmbols = inputSecurities.stream().filter(s -> s.getMainSymbol() == s.getCallOrPutOrStockOption()).collect(Collectors.toList());
        /*
         Randomly decide what are the Symbols will be updated.
         e.g if only AAPL or only TELSA or both or other Symbols that needs to be updated - all those will be decided randomly
         */
        Random random = new Random();
        int nextNumber = random.nextInt(stockSysmbols.size());
        for (int i = 0; i <= nextNumber; i++) {
            Security mainStock = stockSysmbols.get(i);
             /*
             Randomly decided price update for Stock(e.g AAPL)
             */
            BigDecimal updatedMainStockPriceValue = getUpdatedPrice(true);
            mainStock.setCallOrPutOptionOrStockPrice(updatedMainStockPriceValue);
            outputSecurities.add(mainStock);

             /*
             Randomly decided price update for call and put options for the above Stock
             */
            List<Security> optionsSymbols = inputSecurities.stream()
                    .filter(s -> (mainStock.getMainSymbol() == s.getMainSymbol() && mainStock.getCallOrPutOrStockOption() != s.getCallOrPutOrStockOption()))
                    .collect(Collectors.toList());
            for (Security option : optionsSymbols) {
                BigDecimal updatedOptionPriceValue = getUpdatedPrice(false);
                option.setCallOrPutOptionOrStockPrice(updatedOptionPriceValue);
                outputSecurities.add(option);
            }
        }

        //System.out.println(LocalDateTime.now() + " MarketDataEngine: updatedSecurities MADE READY: " + outputSecurities);
        return outputSecurities;
    }


    private BigDecimal getUpdatedPrice(boolean mainStock) {
        Random random = new Random();
        int nextNumber = random.nextInt(mainStock ? 500 : 10);
        double nextDouble = random.nextDouble();
        return new BigDecimal(nextNumber).multiply(new BigDecimal(nextDouble));
    }
}