package com.crypto.engine;

import com.crypto.model.Portfolio;

import java.time.LocalDateTime;
import java.util.concurrent.BlockingQueue;

public class PortfolioListener implements Runnable {
    private final BlockingQueue<Portfolio> portfolioQueue;

    public PortfolioListener(BlockingQueue<Portfolio> portfolioQueue) {
        this.portfolioQueue = portfolioQueue;
    }


    /*
    6. Implement a portfolio result subscriber.
    - Listener the above result
    - print it into console (pretty print)
     */
    @Override
    public void run() {
        try {
            System.out.println(LocalDateTime.now() + " PortfolioListener started");
            while (!Thread.interrupted()) {
                Portfolio portfolio = portfolioQueue.take();
                //System.out.println(LocalDateTime.now()+" PortfolioEngine: portfolio RECEIVED");
                portfolio.getLines().forEach(System.out::println);
            }
        } catch (Exception ex) {
            System.out.println(LocalDateTime.now() + " PortfolioListener stopped");
        }
    }

}
