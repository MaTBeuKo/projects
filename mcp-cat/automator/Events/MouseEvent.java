package net.automator.Events;

import java.io.Serializable;

public class MouseEvent extends IOEvent implements Serializable {
    float yaw;
    float pitch;
    int mouseKey;

    boolean state;

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public int getMouseKey() {
        return mouseKey;
    }

    public MouseEvent(int mouseKey, boolean state, float yaw, float pitch, long time){
        this.mouseKey = mouseKey;
        this.state = state;
        this.yaw = yaw;
        this.pitch = pitch;
        this.time = time;
    }

    public boolean getState() {
        return state;
    }
}
