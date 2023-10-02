package net.automator.fixer.Events;

import net.automator.fixer.Exception.CatException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;

public class Disconnect extends Event{
    private Minecraft mc = Minecraft.getMinecraft();
    @Override
    public void execute() throws CatException {
        if (NetHandlerPlayClient.isConnected() || mc.isSingleplayer()) {
            mc.displayGuiScreen(new GuiIngameMenu());
            mc.theWorld.sendQuittingDisconnectingPacket();
            mc.loadWorld((WorldClient) null);
            mc.displayGuiScreen(new GuiMainMenu());
        }
        isReady = true;
    }
}
