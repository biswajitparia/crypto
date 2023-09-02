package com.crypto.data;

import com.crypto.enums.Ticker;
import com.crypto.model.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public enum PositionMockForCVSFile {
    INSTANCE;
    private final List<Position> positions = new ArrayList<>();
    private final ReentrantLock reentrantLock = new ReentrantLock();

    public List<Position> getPositions() {
        try {
            reentrantLock.lock();
            if (positions.isEmpty()) {
                positions.addAll(Arrays.asList(
                        new Position(Ticker.AAPL, 1000),
                        new Position(Ticker.AAPL_OCT_2020_110_C, -20000),
                        new Position(Ticker.AAPL_OCT_2020_110_P, 20000),
                        new Position(Ticker.TELSA, -500),
                        new Position(Ticker.TELSA_NOV_2020_400_C, 10000),
                        new Position(Ticker.TELSA_DEC_2020_400_P, -10000)
                ));
            }
            return positions;
        } finally {
            reentrantLock.unlock();
        }
    }

    public void setPositions(List<Position> updatedPosition) {
        try {
            reentrantLock.lock();
            positions.clear();
            positions.addAll(updatedPosition);
        } finally {
            reentrantLock.unlock();
        }
    }
}
