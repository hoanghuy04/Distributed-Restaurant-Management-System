/*
 * @ (#) TableLockManager.java      1.0      4/26/2025
 *
 * Copyright (c) 2025 IUH. ALL rights reserved.
 */
package util;

/*
 * @description:
 * @author: Hoang Huy
 * @date: 4/26/2025
 * @version: 1.0
 */
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class TableLockManager {
    private static TableLockManager instance;
    private final Map<String, ReentrantLock> tableLocks;

    private TableLockManager() {
        tableLocks = new HashMap<>();
    }

    public static synchronized TableLockManager getInstance() {
        if (instance == null) {
            instance = new TableLockManager();
        }
        return instance;
    }

    public boolean acquireLock(String tableId, long timeoutMillis) {
        ReentrantLock lock = tableLocks.computeIfAbsent(tableId, k -> new ReentrantLock());
        try {
            return lock.tryLock(timeoutMillis, java.util.concurrent.TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    public void releaseLock(String tableId) {
        ReentrantLock lock = tableLocks.get(tableId);
        if (lock != null && lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }
}