package net.automator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Chain implements Serializable {
    ArrayList<Event> chain = new ArrayList<>();
    int chainRepeatCount = 1;
    int chainRepeatCounter = 0;

    public Chain(int chainRepeatCount) {
        this.chainRepeatCount = chainRepeatCount;
    }

    public enum EventType {
        RANDOM_MACROS, MACROS, INTERACTION

    }

    public static class Event implements Serializable {
        public EventType type;
        public Macros macros;
        public MacrosGroup group;
        public String callableName;
        int repeatCount;

        public Event(EventType type, Macros macros, MacrosGroup group, int repeatCount) {
            this.type = type;
            this.macros = macros;
            this.group = group;
            this.repeatCount = repeatCount;
        }

        public Event(EventType type, String callableName, int repeatCount) {
            this.type = type;
            this.callableName = callableName;
            this.repeatCount = repeatCount;
        }

        @Override
        public String toString() {
            return "Event{" +
                    "type=" + type.toString() +
                    ", macros=" + macros +
                    ", group=" + group +
                    ", repeatCount=" + repeatCount +
                    ", callable=" + callableName + '}';
        }
    }

    public void addMacros(Macros macros, int repeatCount) {
        chain.add(new Event(EventType.MACROS, macros, null, repeatCount));
    }

    public void addRandomMacros(MacrosGroup group, int repeatCount) {
        chain.add(new Event(EventType.RANDOM_MACROS, null, group, repeatCount));
    }

    public void addInteraction(String callableName, int repeatCount){
        chain.add(new Event(EventType.INTERACTION, callableName, repeatCount));
    }

    int i = 0;
    int repeatCounter = 0;

    public Object getNextMacros() {
        if (chain.size() == 0) {
            return null;
        }
        if (i >= chain.size()) {
            chainRepeatCounter++;
            if (chainRepeatCounter >= chainRepeatCount) {
                Reset();
                return null;
            } else {
                i = 0;
            }
        }
        Event event = chain.get(i);
        if (event.type == EventType.MACROS) {
            if (repeatCounter < event.repeatCount) {
                repeatCounter++;
                return event.macros;
            } else {
                repeatCounter = 0;
                i++;
                return getNextMacros();
            }
        } else if (event.type == EventType.RANDOM_MACROS) {
            if (repeatCounter < event.repeatCount) {
                repeatCounter++;
                return event.group.get(ThreadLocalRandom.current().nextInt(0, event.group.size()));
            } else {
                repeatCounter = 0;
                i++;
                return getNextMacros();
            }
        } else if (event.type == EventType.INTERACTION) {
            if (repeatCounter < event.repeatCount) {
                repeatCounter++;
                return event.callableName;
            } else {
                repeatCounter = 0;
                i++;
                return getNextMacros();
            }
        }
        Reset();
        return null;
    }

    public void Reset() {
        i = 0;
        chainRepeatCounter = 0;
        repeatCounter = 0;
    }

    public void removeEvent(int id) {
        if (0 <= id && id < chain.size()) {
            chain.remove(id);
        } else {
            System.out.println("Incorrect id");
        }
    }


    public void setRepeatCount(int chainRepeatCount) {
        if (chainRepeatCount != 13) {
            this.chainRepeatCount = chainRepeatCount;
        } else {
            this.chainRepeatCount = Integer.MAX_VALUE;
        }
    }

    @Override
    public String toString() {
        return "Chain{" +
                "chain=" + chain +
                ", chainRepeatCount=" + chainRepeatCount +
                ", chainRepeatCounter=" + chainRepeatCounter +
                ", i=" + i +
                ", repeatCounter=" + repeatCounter +
                '}';
    }
}
