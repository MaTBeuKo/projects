package net.automator.Events;

public class MessageReceivedEvent extends AbstractEvent{
    public String getMessage() {
        return message;
    }

    String message;

    public MessageReceivedEvent(String message) {
        this.message = message;
    }
}
