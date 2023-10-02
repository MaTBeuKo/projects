package net.automator.modules;

import net.automator.Events.*;

public abstract class AbstractModule {
	private boolean toggled;
	protected int key;
	
	public abstract void onEvent(AbstractEvent event);
	protected AbstractModule() {
	}

	public void setKey(int key){
		this.key = key;
	}

	public boolean isToggled() {
		return toggled;
	}
	
	public int getKey() {
		return key;
	}
	
	public void enable() {
		toggled = true;
	}
	
	public void disable() {
		toggled = false;
	}
}
