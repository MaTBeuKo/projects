package net.automator.fixer.Schemes;

import net.automator.utility.Time;

import java.util.concurrent.Callable;

public class Wait implements Callable<String> {
    private final long waitTime;

    public Wait() {
        waitTime = Time.big();
    }

    @Override
    public String call() throws Exception {
        Thread.sleep(waitTime);
        return "slept for " + waitTime;
    }
}
