package com.crypto.engine;

import com.crypto.data.PositionMockForCVSFile;
import com.crypto.enums.Ticker;
import com.crypto.model.Portfolio;
import com.crypto.model.Security;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class PortfolioEngine implements Runnable {
    private static final int MAX_SPACE = 20;
    private final AtomicInteger counter = new AtomicInteger(1);
    private final DecimalFormat formatter = new DecimalFormat("#,##0.00");
    private final List<Security> allSecurities = new ArrayList<>();
    private final BlockingQueue<List<Security>> marketDataQueue;
    private final BlockingQueue<Portfolio> portfolioQueue;

    public PortfolioEngine(BlockingQueue<List<Security>> marketDataQueue, BlockingQueue<Portfolio> portfolioQueue) {
        this.marketDataQueue = marketDataQueue;
        this.portfolioQueue = portfolioQueue;
    }

    /*
    5. Publishes following details in real-time:
    - Each position’s market value.
    - Total portfolio’s NAV.
     */
    @Override
    public void run() {
        try {
            System.out.println(LocalDateTime.now()+" PortfolioEngine started");
            while (!Thread.interrupted()) {
                List<Security> securities = marketDataQueue.take();
                //System.out.println(LocalDateTime.now()+" PortfolioEngine: securities RECEIVED" + securities );

                Portfolio portfolio = createPortfolio(securities);
                //System.out.println(LocalDateTime.now()+" PortfolioEngine: portfolio SENDING");
                portfolioQueue.put(portfolio);
            }
        } catch (Exception e) {
            System.out.println(LocalDateTime.now()+" PortfolioEngine stopped");
        }
    }

    private Portfolio createPortfolio(List<Security> updatedSecurities) {
        List<String> lines = new ArrayList<>();
        lines.add(format("## " + counter.getAndIncrement() + " Market Data update", true));

        for (Security security : updatedSecurities) {
            if (security.getMainSymbol() == security.getCallOrPutOrStockOption()) {
                lines.add(format(security.getMainSymbol() + " changes to " + security.getCallOrPutOptionOrStockPrice().setScale(2, RoundingMode.HALF_UP), true));
            }
            allSecurities.remove(security);
        }

        allSecurities.addAll(updatedSecurities);

        Map<Ticker, Integer> tickerAndSizeMap = PositionMockForCVSFile.INSTANCE.getPositions()
                .stream()
                .collect(Collectors.toMap(e -> e.getTicker(), e -> e.getPositionSize()));

        BigDecimal totalPortfolio = allSecurities.stream().map(s -> s.getCallOrPutOptionOrStockPrice().setScale(2, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal(tickerAndSizeMap.get(s.getCallOrPutOrStockOption())).setScale(2, RoundingMode.HALF_UP)))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        lines.add(format("", true));
        lines.add(format("## Portfolio", true));
        lines.add(format("Symbol", true)
                + format("Price", false)
                + format("Qty", false)
                + format("Value", false));

        allSecurities.stream().forEach(s -> {
            lines.add(format(s.getCallOrPutOrStockOption().getName(), true)
                    + format(formatter.format(s.getCallOrPutOptionOrStockPrice().setScale(2, RoundingMode.HALF_UP)), false)
                    + format(String.valueOf(tickerAndSizeMap.get(s.getCallOrPutOrStockOption())), false)
                    + format(formatter.format(s.getCallOrPutOptionOrStockPrice().setScale(2, RoundingMode.HALF_UP).multiply(new BigDecimal(tickerAndSizeMap.get(s.getCallOrPutOrStockOption())))), false));
        });

        lines.add(format("", true));
        lines.add(format("## Total portfolio", true)
                + format("", false)
                + format("", false)
                + format(formatter.format(totalPortfolio), false));

        lines.add(format("", true));
        lines.add(format("", true));

        return new Portfolio(lines);
    }

    private String format(String name, boolean leftAlignment) {
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < MAX_SPACE - name.length(); j++) {
            sb.append(" ");
        }
        return leftAlignment ? name + sb : sb + name;
    }
}
