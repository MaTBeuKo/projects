package net.automator.modules;

import net.automator.Events.AbstractEvent;
import net.automator.Events.BlockBreakEvent;

import java.util.ArrayDeque;

public class Cpm extends AbstractModule {

    private static Cpm instance;
    MacrosPlayer player = MacrosPlayer.getInstance();

    public static Cpm getInstance() {
        if (instance == null) {
            instance = new Cpm();
        }
        return instance;
    }

    @Override
    public void onEvent(AbstractEvent event) {
        if (!(event instanceof BlockBreakEvent)) {
            return;
        }
        blockBreaks.addLast(System.currentTimeMillis());
    }

    public Long getCpm() {
        if (player.isToggled() && System.currentTimeMillis() - player.getStartTime() > 240000) {
            return cpm;
        } else {
            return (long) -1;
        }
    }

    public void onTick() {
        if (getCpm() != -1) {
            mincpm = Long.min(mincpm, getCpm());
        }
        while (!blockBreaks.isEmpty() && System.currentTimeMillis() - blockBreaks.getFirst() > 60000) {
            blockBreaks.removeFirst();
        }
        cpm = blockBreaks.size();
    }

    private ArrayDeque<Long> blockBreaks = new ArrayDeque<>();
    private long cpm = 0;

    public long getMincpm() {
        return mincpm;
    }

    private long mincpm = Integer.MAX_VALUE;
}
