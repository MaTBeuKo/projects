package net.automator.fixer.Events;

import net.automator.Chain;
import net.automator.Events.BlockBreakEvent;
import net.automator.fixer.Exception.CpmException;
import net.automator.fixer.Exception.MacroException;
import net.automator.modules.Cpm;
import net.automator.modules.MacrosPlayer;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class PlayMacro extends Event {
    private MacrosPlayer player = MacrosPlayer.getInstance();
    private Chain chain;


    @Override
    public void execute() throws MacroException {
        if (state == 0) {
            if (player.isToggled()) {
                player.disable();
            }
            player.setup(chain);
            player.enable();
            state++;
        } else if (state == 1) {
            if (!player.isToggled()) {
                isReady = true;
                return;
            }
            if (Cpm.getInstance().getCpm() != -1 && Cpm.getInstance().getCpm() < 600) {
                long cpm = Cpm.getInstance().getCpm();
                player.disable();
                throw new CpmException("Too low cpm: " + cpm);
            }
            BlockBreakEvent event = player.lastBlockBreak;
            if (event == null) {
                return;
            }
            ItemStack item = event.getItem();
            if (item == null) {
                return;
            }
            Block block = event.getBlock();
            String blockName = block.getUnlocalizedName().toLowerCase();
            String toolName = item.hasDisplayName() ? item.getDisplayName().toLowerCase() : "unnamed";
            if ((toolName.contains("pumpkin dicer") && !blockName.equals("tile.pumpkin")) ||
                    (toolName.contains("melon dicer") && !blockName.equals("tile.melon"))) {
                if (player.isToggled()) {
                    player.disable();
                }
                isReady = true;
                throw new MacroException("Macros: \"" + player.getMacros().getName() + "\" broke" + blockName);
            }
        }
        return;
    }

    public PlayMacro(Chain chain) {
        this.chain = chain;
    }
}
