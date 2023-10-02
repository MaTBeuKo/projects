package net.automator.ui;

import net.automator.io.Bot;
import net.automator.io.Settings;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class CatMenu extends GuiScreen {
    @Override
    public void initGui() {
        this.buttonList.clear();

        this.buttonList.add(new SwitchButton(
                11, this.width / 2 - 100, this.height / 4 + 24, "DEBUG", "Debug", Settings.get("DEBUG")));
        this.buttonList.add(new SwitchButton(
                12, this.width / 2 - 100, this.height / 4 + 44, "TELEGRAM_SUPPORT", "Telegram notify", Settings.get("TELEGRAM_SUPPORT")));
        this.buttonList.add(new SwitchButton(
                12, this.width / 2 - 100, this.height / 4 + 64, "REQUIRE_POTION", "Require potion", Settings.get("REQUIRE_POTION")));
        this.buttonList.add(new SwitchButton(
                13, this.width / 2 - 100, this.height / 4 + 84, "MELODY", "Melody", Settings.get("MELODY")));
        this.buttonList.add(new SwitchButton(
                13, this.width / 2 - 100, this.height / 4 + 104, "CHECK_ITEM_DROP", "Check item drop", Settings.get("CHECK_ITEM_DROP")));
        super.initGui();
    }


    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
//        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
//        GL11.glEnable(3042);
//        ResourceLocation res = new ResourceLocation("/picBig.png");
//
//        GL11.glBindTexture(3553, mc.getTextureManager().getTexture(res).getGlTextureId());
//        drawModalRectWithCustomSizedTexture(width / 2 - 100, height / 4 + 64 - 170, 0, 0, 200, 200, 200, 200);
//        GL11.glDisable(3042);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        SwitchButton b = (SwitchButton) button;
        b.setState(Settings.toggle(b.setting));
        if (button.id == 12) {
            if (Settings.get(b.setting).equals("true")) {
                Bot.startListening();
            } else {
                Bot.stopListening();
            }
        }
        super.actionPerformed(button);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_INSERT) {
            mc.displayGuiScreen(null);
        }
        super.keyTyped(typedChar, keyCode);
    }
}
