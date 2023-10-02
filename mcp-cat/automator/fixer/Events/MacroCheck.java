package net.automator.fixer.Events;

import net.automator.io.Bot;
import net.automator.fixer.Exception.CatException;

public class MacroCheck extends Event {
    @Override
    public void execute() throws CatException {
        if (state == 0) {
            t0 = System.currentTimeMillis();
            Bot.sendMessage("Macro check!!!");
            java.awt.Toolkit.getDefaultToolkit().beep();
            state++;
        } else if (state == 1) {
            if (System.currentTimeMillis() > t0 + 2000) {
                state = 0;
            }
        }
    }
}
