package com.nono.skinfx;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by wl on 2019/2/15.
 */
public class GlobalValueManager {

    private AtomicInteger taskCount = new AtomicInteger();

    public static GlobalValueManager getInstance() {
        return GlobalValueManager.SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final GlobalValueManager INSTANCE = new GlobalValueManager();
    }

    public void plugTaskCount() {
        taskCount.incrementAndGet();
    }

    public void decreaseTaskCount() {
        taskCount.decrementAndGet();
    }

    public void setTaskCount(int taskCount) {
        this.taskCount.set(taskCount);
    }

    public int getTaskCount() {
        return taskCount.get();
    }
}
