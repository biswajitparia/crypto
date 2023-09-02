import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class TradingApplicationTest {
    @Test
    public void startTrading() {
        TradingApplication tradingApplication = new TradingApplication();
        tradingApplication.start();
        try {
            TimeUnit.MINUTES.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        tradingApplication.stop();
    }

}