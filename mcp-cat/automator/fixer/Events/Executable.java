package net.automator.fixer.Events;

import net.automator.fixer.Exception.CatException;

public interface Executable {
    void execute() throws CatException;
     boolean ready();
}
