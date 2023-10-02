package net.automator.Events;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class BlockBreakEvent extends GameEvent{
    private ItemStack item;
    private Block block;

    public BlockBreakEvent(ItemStack item, Block block) {
        this.item = item;
        this.block = block;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }
}
