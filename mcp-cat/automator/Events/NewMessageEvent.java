package net.automator.Events;

public class NewMessageEvent extends AbstractEvent{
    public String getMessage() {
        return message;
    }

    String message;
    boolean cancelled;
    public boolean isCancelled(){
        return cancelled;
    }
    public void cancel(){
        cancelled = true;
    }
    public NewMessageEvent(String message){
        this.message = message;
    }
}
