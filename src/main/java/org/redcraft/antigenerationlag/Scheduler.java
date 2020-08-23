package org.redcraft.antigenerationlag;

public class Scheduler implements Runnable {

    public void run() {
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        LocationSpeedUtils.checkFrozenPlayers();
    }
}