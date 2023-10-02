package net.automator.Events;

import java.io.Serializable;

public class KeyPressEvent extends IOEvent implements Serializable {
	private int key;
	public KeyPressEvent(EventType type, long time, int key) {
		this.type = type;
		this.time = time;
		this.key = key;

	}
	
	public int getKey() {
		return key;
	}

}
