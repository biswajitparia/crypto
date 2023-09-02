import com.crypto.engine.MarketDataEngine;
import com.crypto.engine.PortfolioEngine;
import com.crypto.engine.PortfolioListener;
import com.crypto.model.Portfolio;
import com.crypto.model.Security;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.*;

public final class TradingApplication {
    private final BlockingQueue<List<Security>> marketDataQueue = new ArrayBlockingQueue<>(1);
    private final BlockingQueue<Portfolio> portfolioQueue = new ArrayBlockingQueue<>(1);
    private final ExecutorService executorService = Executors.newFixedThreadPool(3);

    public void start() {
        System.out.println("Starting Trading Application....");
        executorService.execute(new MarketDataEngine(marketDataQueue));
        executorService.execute(new PortfolioEngine(marketDataQueue, portfolioQueue));
        executorService.execute(new PortfolioListener(portfolioQueue));
        System.out.println(LocalDateTime.now()+" Trading Application Started");
    }

    public void stop() {
        System.out.println("Stopping Trading Application....");
        executorService.shutdownNow();
        System.out.println(LocalDateTime.now()+" Trading Application Stopped");
    }
}