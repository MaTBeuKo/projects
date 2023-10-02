package net.automator.fixer.Events;

public abstract class Event implements Executable {
    protected long t0 = 0;

    protected int state;
    protected boolean isReady;
    protected Object result;
    public Object getResult(){
        return result;
    }
    @Override
    public boolean ready(){
        return isReady;
    }
}
