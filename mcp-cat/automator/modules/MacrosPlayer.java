package net.automator.modules;

import net.automator.Chain;
import net.automator.Client;
import net.automator.Events.*;
import net.automator.Macros;
import net.automator.io.Chat;
import net.automator.io.Settings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class MacrosPlayer extends AbstractModule {

    public Macros getMacros() {
        return macros;
    }

    public Chain getChain() {
        return chain;
    }

    private Macros macros;
    Chain chain;
    Minecraft mc = Minecraft.getMinecraft();
    int i;
    long diff;
    long startTime;

    public long getStartTime() {
        return startTime;
    }

    public BlockBreakEvent lastBlockBreak;

    @Override
    public void onEvent(AbstractEvent event) {
        if (event.getType() == EventType.KEY_PRESS && ((KeyPressEvent) event).getKey() == key) {
            if (isToggled()) {
                disable();
            } else {
                enable();
            }
        }
        if (event instanceof BlockBreakEvent) {
            lastBlockBreak = (BlockBreakEvent) event;
        }
    }

    private static MacrosPlayer instance = null;


    public static MacrosPlayer getInstance() {
        if (instance == null) {
            instance = new MacrosPlayer();
        }
        return instance;
    }

    boolean chainMode = false;

    public void setup(Macros macros) {
        this.macros = macros;
        chainMode = false;
    }

    public void setup(Chain chain) {
        macros = null;
        this.chain = chain;
        chainMode = true;
    }

    public Future<?> executingFuture = null;

    @Override
    public void enable() {
        if (!mc.thePlayer.hasPotionEffect("jump") && Settings.get("REQUIRE_POTION").equals("true")) {
            Client.notify("No jump potion effect!");
            java.awt.Toolkit.getDefaultToolkit().beep();
            return;
        }
        //mc.inGameHasFocus = true;
        super.enable();
        i = 0;
        diff = -1;
        KeyBinding.unPressAllKeys();
        startTime = System.currentTimeMillis();
        if (chainMode) {
            setNextMacrosOrRunnable();
            if (macros == null && executingFuture == null) {
                Client.notify("First macros is null and no thread is running");
                disable();
                return;
            }
        }
        //mc.thePlayer.inventory.currentItem = 0;

        Chat.print("Player enabled");
    }

    @Override
    public void disable() {
        KeyBinding.unPressAllKeys();
        breaking = false;
        if (chainMode) {
            if (executingFuture != null) {
                try {
                    executingFuture.cancel(true);
                } catch (Exception ex) {
                    Client.notify("can't interrupt " + ex.getMessage());
                }
                executingFuture = null;
            }
            chain.Reset();
        }
        super.disable();
        lastBlockBreak = null;
        Chat.print("Player disabled");
    }

    boolean breaking = false;

    public boolean isBreaking() {
        return breaking;
    }

    void setNextMacrosOrRunnable() {
        Object t = chain.getNextMacros();
        if (t instanceof Macros) {
            macros = (Macros) t;
        } else {
            if (t == null) {
                disable();
                return;
            }
            String callableName = (String) t;
            try {
                Class<?> clazz = Class.forName("net.automator.fixer.Schemes." + callableName);
                Constructor<?> ctor = clazz.getConstructor();
                Callable<Object> callable = (Callable<Object>) ctor.newInstance();
                executingFuture = Client.executorService.submit(callable);
            } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                     InvocationTargetException ex) {
                Client.notify("Exception while adding task in macrosPlayer" + ex.getMessage());
            }
        }
    }

    public void onTick() {
        if (executingFuture != null && executingFuture.isDone()) {
            executingFuture = null;
            setNextMacrosOrRunnable();
        } else if (executingFuture != null) {
            return;
        }
        if (macros == null) {
            return;
        }
        ArrayList<IOEvent> events = macros.getEvents();
        if (!isToggled() || events == null || events.size() == 0) {
            return;
        }
        long currentTick = Client.getTick();
        IOEvent event = events.get(i);
        if (i == 0) {
            diff = currentTick - event.getTime();
        }
        long relativeTime = currentTick - diff;
        while (event.getTime() == relativeTime) {
            if (event instanceof MouseEvent) {
                int mouseKey = ((MouseEvent) event).getMouseKey();
                float pitch = ((MouseEvent) event).getPitch();
                float yaw = ((MouseEvent) event).getYaw();
                boolean state = ((MouseEvent) event).getState();
                mc.thePlayer.rotationPitch = pitch;
                mc.thePlayer.rotationYaw = yaw;
                KeyBinding.setKeyBindState(mouseKey, state);
                if (mouseKey == mc.gameSettings.keyBindAttack.getKeyCode()) {
                    breaking = state;
                }
                if (state && mouseKey != mc.gameSettings.keyBindPickBlock.getKeyCode()) {
                    KeyBinding.onTick(mouseKey);
                }
            } else if (event instanceof KeyPressEvent) {
                KeyBinding.setKeyBindState(((KeyPressEvent) event).getKey(), event.getType() == EventType.KEY_PRESS);
            }
            i++;
            if (i >= events.size()) {
                if (!chainMode) {
                    disable();
                    break;
                } else {
                    setNextMacrosOrRunnable();
                    i = 0;
                    diff = -1;
                }
            }
            event = events.get(i);
        }
    }
}
