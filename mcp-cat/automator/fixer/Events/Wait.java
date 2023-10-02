package net.automator.fixer.Events;

import net.automator.fixer.Exception.CatException;

public class Wait extends Event {
    long time;

    @Override
    public void execute() throws CatException {
        if (state == 0) {
            t0 = System.currentTimeMillis();
            state++;
        } else if (state == 1) {
            if (System.currentTimeMillis() > t0 + time) {
                isReady = true;
            }
        }
    }

    public Wait(long time) {
        this.time = time;
    }
}
