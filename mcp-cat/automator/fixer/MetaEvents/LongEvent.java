package net.automator.fixer.MetaEvents;

import net.automator.fixer.Events.Event;
import net.automator.fixer.Manager;
import net.automator.utility.Time;

public class LongEvent {
    public static Object perform(Event event) throws InterruptedException {
        synchronized (event) {
            synchronized (Manager.class) {
                Manager.add(event);
            }
            while (!event.ready()) {
                event.wait();
            }
            Thread.sleep(Time.tiny());
            return event.getResult();
        }
    }
}
