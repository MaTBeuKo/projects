package net.automator.modules;

import net.automator.Events.*;
import net.automator.Macros;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;

public class MacrosRecorder extends AbstractModule {
    String name = "untitled";

    ArrayList<IOEvent> events;
    private static MacrosRecorder instance = null;

    private Minecraft mc = Minecraft.getMinecraft();


    public static MacrosRecorder getInstance() {
        if (instance == null) {
            instance = new MacrosRecorder();
        }
        return instance;
    }

    double startX;
    double startY;
    double startZ;
    float startYaw;
    float startPitch;

    double endX;
    double endY;
    double endZ;
    float endYaw;
    float endPitch;

    @Override
    public void enable() {
        startX = mc.thePlayer.posX;
        startY = mc.thePlayer.posY;
        startZ = mc.thePlayer.posZ;
        startYaw = mc.thePlayer.rotationYaw;
        startPitch = mc.thePlayer.rotationPitch;
        events = new ArrayList<>();
        System.out.println("Macros enabled!");
        super.enable();
    }

    @Override
    public void disable() {
        endX = mc.thePlayer.posX;
        endY = mc.thePlayer.posY;
        endZ = mc.thePlayer.posZ;
        endYaw = mc.thePlayer.rotationYaw;
        endPitch = mc.thePlayer.rotationPitch;
        System.out.println("Macros disabled!");
        super.disable();
    }

    @Override
    public void onEvent(AbstractEvent event) {

        if (!(event instanceof IOEvent)) {
            return;
        }
        if (event instanceof KeyPressEvent && ((KeyPressEvent) event).getKey() == key) {
            if (event.getType() == EventType.KEY_RELEASE) {
                return;
            }
            if (isToggled()) {
                disable();
            } else {
                enable();
            }
            return;
        }
        if (!isToggled()) {
            return;
        }
        if (event.getType() == EventType.KEY_PRESS) {
            System.out.println("Key: " + ((KeyPressEvent) event).getKey() + " pressed at: " + event.getTime());
        }
        if (event instanceof MouseEvent) {
            System.out.println("Mouse event: button " + ((MouseEvent) event).getMouseKey() + " pitch: " +
                    ((MouseEvent) event).getPitch() + " yaw: " + ((MouseEvent) event).getYaw());
        }
        events.add((IOEvent) event);
    }

    public Macros getMacros() {
        Macros macros = new Macros(name, events, startX, startY, startZ, endX, endY, endZ, startYaw, startPitch, endYaw, endPitch);
        return macros;
    }
}
