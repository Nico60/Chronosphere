package com.nico60.tickmanipulatormod.handler;

import com.nico60.tickmanipulatormod.TickManipulatorMod;
import com.nico60.tickmanipulatormod.block.BlockHistoryManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = TickManipulatorMod.MOD_ID)
public class BlockEventHandler {

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (!event.getPlayer().level().isClientSide && event.getPlayer().level() instanceof ServerLevel serverLevel) {
            BlockHistoryManager.saveBlockState(serverLevel, event.getPos());
        }
    }

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (!Objects.requireNonNull(event.getEntity()).level().isClientSide && event.getEntity().level() instanceof ServerLevel serverLevel) {
            BlockHistoryManager.saveBlockState(serverLevel, event.getPos());
        }
    }
}
