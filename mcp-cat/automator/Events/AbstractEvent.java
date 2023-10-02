package net.automator.Events;

import java.io.Serializable;

public class AbstractEvent implements Serializable {
	protected EventType type = EventType.BAD_TYPE;
	protected long time = -123;
	
	public EventType getType() {
		return type;
	}
	
	public long getTime() {
		return time;
	}
}
