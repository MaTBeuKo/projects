package net.automator.utility;

import java.util.concurrent.ThreadLocalRandom;

public class Time {

    public static long tiny() {
        return ThreadLocalRandom.current().nextInt(500, 1000);
    }

    public static long small() {
        return ThreadLocalRandom.current().nextInt(3000, 5000);
    }

    public static long medium() {
        return ThreadLocalRandom.current().nextInt(6000, 11000);
    }

    public static long big() {
        return ThreadLocalRandom.current().nextInt(30000, 60000);
    }

    public static long huge() {
        return ThreadLocalRandom.current().nextInt(180000, 300000);
    }
}
