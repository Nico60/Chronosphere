package com.nico60.tickmanipulatormod.item;

import com.nico60.tickmanipulatormod.block.BlockHistoryManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class BlockRestorerItem extends Item {
    public BlockRestorerItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (!level.isClientSide) {
            ServerLevel serverLevel = (ServerLevel) level;
            BlockHistoryManager.restoreBlocks(serverLevel);
        }
        return InteractionResult.SUCCESS;
    }
}
